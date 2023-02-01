import uvicorn

from PyQt6.QtCore import Qt
from time import gmtime, strftime
from PyQt6.QtCore import QThread, QObject,QTimer
from PyQt6.QtWidgets import QApplication, QLabel,  QVBoxLayout, QWidget
from fastapi import FastAPI, APIRouter

class MyApp(QWidget):

    def __init__(self):
        super().__init__()

        # Create a label to display the response
        self.response_label = QLabel("No response yet")

        # Create a thread to run the server function
        self.setGeometry(100, 100, 800, 600)

        self.server_thread = QThread()
        self.server_worker = ServerWorker()
        self.server_worker.moveToThread(self.server_thread)
        self.server_thread.started.connect(self.server_worker._start)
        self.server_thread.start()
        self.timer = QTimer(self)
        self.timer.timeout.connect(self.update_response_label)
        self.timer.start(1000)

        # Create a layout to organize the widgets
        layout = QVBoxLayout()
        layout.addWidget(self.response_label)
        layout.addWidget(self.response_label,
                         alignment=Qt.AlignmentFlag.AlignLeft | Qt.AlignmentFlag.AlignTop)
        self.setLayout(layout)

    def update_response_label(self):
        '''
        A function that updates the response label with the data from dumpstore        
        '''
        dumpstore = self.server_worker.dumpstore
        if dumpstore is not None:
            text_data=""
            for key, value in dumpstore.items():
                text_data+=(f"\n{key} : {value}\n\n")
                for k, v in value.items():
                    text_data+=(f"    {k} : {v}\n\n")
            self.response_label.setText(str(text_data))


class ServerWorker(QObject):
    def __init__(self):
        super().__init__()
        self.count = 0
        self.dumpstore = {"dump": {}}
        self.router = APIRouter()
        self.router.add_api_route("/receive", self.receive, methods=["POST"])
        self.router.add_api_route("/dump", self.dump, methods=["GET"])
        

    def receive(self, data: dict):
        '''
        A function that receives data and stores it in a dictionary with a timestamp
        uses a count to keep track of the number of messages received
        '''
        print("[+] [GET] /receive")
        convert = strftime("%H:%M:%S", gmtime())
        self.dumpstore["dump"][f'data{self.count}'] = data["message"] + \
            " @" f" {convert}"
        self.count += 1
        return "OK"

    def dump(self):
        '''
        A function that returns dumpstore data
        '''
        print("[+] [POST] /dump")
        return self.dumpstore
    def _start(self):
        app = FastAPI()
        app.include_router(self.router)
        uvicorn.run(app, host="192.168.0.106", port=8000)
    
if __name__ == "__main__":
    app = QApplication([])
    window = MyApp()
    window.show()
    app.exec()
    

        