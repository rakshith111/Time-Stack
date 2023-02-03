import uvicorn
import sys
import subprocess
import json
import time

from PyQt6 import QtWidgets
from PyQt6.QtCore import QThread, QObject, QTimer

from datetime import datetime
from fastapi import FastAPI, APIRouter
from ui import Ui_TimeStackServer


class MainWindow(QtWidgets.QMainWindow):

    def __init__(self, parent=None):

        super(MainWindow, self).__init__(parent=parent)

        self.ui = Ui_TimeStackServer()
        self.ui.setupUi(self)
        self.ui.submit_btn.clicked.connect(self.post_data)
        self.ui.refresh_btn.clicked.connect(self.update_response_label)
        self.server_thread = QThread()
        self.server_worker = ServerWorker()
        self.server_worker.moveToThread(self.server_thread)
        self.server_thread.started.connect(self.server_worker._start)
        self.server_thread.start()
        self.timer = QTimer(self)
        self.timer.timeout.connect(self.update_response_label)
        self.timer.start(1000)

    def post_data(self):
        '''
        A function that posts data to the server
        '''

        data = {"message": str(self.ui.message_field.text())}
        data = json.dumps(data)
        subprocess.run(['curl', '-X', 'POST', '-H', 'Content-Type: application/json',  '-H',
                       'accept: application/json', '-d', f'{data}', 'http://192.168.1.2:8000/receive'])

        self.ui.message_field.setText('')

    def update_response_label(self):
        '''
        A function that updates the response label with the data from dumpstore        
        '''
        dumpstore = self.server_worker.dumpstore
        if dumpstore is not None:
            text_data = ''
            for key, value in dumpstore.items():
                text_data += (f'\n{key} : {value}\n\n')
                for k, v in value.items():
                    text_data += (f'    {k} : {v}\n\n')
            self.ui.response_label.setText(str(text_data))


class ServerWorker(QObject):
    def __init__(self):
        super().__init__()
        self.count = 0
        self.dumpstore = {'dump': {}}
        self.router = APIRouter()
        self.router.add_api_route('/receive', self.receive, methods=['POST'])
        self.router.add_api_route('/dump', self.dump, methods=['GET'])

    def _current_time(self):
        '''
        A function that returns the current time
        '''
        now_timestamp = time.time()
        offset = datetime.fromtimestamp(
            now_timestamp) - datetime.utcfromtimestamp(now_timestamp)
        return (datetime.utcnow() + offset).strftime('%H:%M:%S')

    def receive(self, data: dict):
        '''
        A function that receives data and stores it in a dictionary with a timestamp
        uses a count to keep track of the number of messages received
        '''
        print('[+] [POST] /receive')
        convert = self._current_time()
        self.dumpstore['dump'][f'data{self.count}'] = data['message'] + \
            ' @' f' {convert}'
        self.count += 1
        return 'OK'

    def dump(self):
        '''
        A function that returns dumpstore data
        '''
        print('[+] [GET] /dump')
        return self.dumpstore

    def _start(self):
        '''
        A function that starts the server
        '''
        app = FastAPI()
        app.include_router(self.router)
        uvicorn.run(app, host='192.168.1.2', port=8000)


if __name__ == '__main__':
    app = QtWidgets.QApplication(sys.argv)
    w = MainWindow()
    w.show()
    sys.exit(app.exec())
