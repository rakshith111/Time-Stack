import uvicorn

from fastapi import FastAPI
from time import gmtime, strftime
from PyQt6.QtCore import QThread, QObject,QTimer
from PyQt6.QtWidgets import QApplication, QLabel,  QVBoxLayout, QWidget
from PyQt6.QtCore import Qt

def fastapi_server():

    server = FastAPI()
    global dumpstore
    dumpstore = {"dump": {}}
    global count
    count = 0        

    @server.post("/receive")
    async def receive( data: dict):
        '''
        A function that receives data and stores it in a dictionary with a timestamp
        uses a count to keep track of the number of messages received
        '''
        print("[+] [GET] /receive")
        global count
        convert = strftime("%H:%M:%S", gmtime())
        dumpstore["dump"][f'data{count}'] =data["message"]+ " @" f" {convert}"
        count += 1
        return "OK"

    @server.post("/dump")
    async def dump():
        '''
        A function that returns dumpstore data
        '''
        print("[+] [POST] /dump")
        return dumpstore
  
    server.add_api_route("/dump", dump, methods=["GET"])
    server.add_api_route("/receive", receive, methods=["POST"])
    uvicorn.run(server, host="127.0.0.1", port=8000)


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
        self.server_thread.started.connect(self.server_worker.start_server)
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
        global dumpstore
        if dumpstore is not None:
            text_data=""
            for key, value in dumpstore.items():
                text_data+=(f"\n{key} : {value}\n\n")
                for k, v in value.items():
                    text_data+=(f"    {k} : {v}\n\n")
            self.response_label.setText(str(text_data))



class ServerWorker(QObject):
    def start_server(self):
        fastapi_server()
    
if __name__ == "__main__":
    app = QApplication([])
    window = MyApp()
    window.show()
    app.exec()
    

        