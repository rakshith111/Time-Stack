import uvicorn
import sys
import subprocess
import json
import time
import socket

from PyQt6 import QtWidgets
from PyQt6.QtCore import QThread, QObject, QTimer

from datetime import datetime
from fastapi import FastAPI, APIRouter
from ui import Ui_TimeStackServer

def get_local_ip()->None:
    '''

    A function that returns the local ip address.
    It makes a connection to google's dns server and returns the local ip address.    

    '''
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        local_ip = s.getsockname()[0]
        s.close()
        return local_ip
    except:
        return None

class MainWindow(QtWidgets.QMainWindow):

    def __init__(self, parent=None) -> None:
        '''
        A class that handles the UI
        Creates a QTthread  is created for the server to run ( Might be a bad idea to run the server in a thread, need to check if processes are better or setting up priorities for the thread)
        Creates a timer that updates the response label every second (1000 milliseconds)
        
        '''

        self.ip = get_local_ip()
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



    def post_data(self) -> None:
        '''

        A function that posts data to the server when the submit button is clicked

        '''

        data = {"message": str(self.ui.message_field.text())}
        data = json.dumps(data)
        subprocess.run(['curl', '-X', 'POST', '-H', 'Content-Type: application/json',  '-H',
                       'accept: application/json', '-d', f'{data}', f'http://{self.ip}:8000/receive'])

        self.ui.message_field.setText('')

    def update_response_label(self) -> None:
        '''

        A function that updates the response label with the data from dumpstore  

        '''
        dumpstore = self.server_worker.dumpstore
        if dumpstore is not None:
            text_data = ''
            for key, value in dumpstore.items():
                text_data += (f'\n{key} : {value}\n\n')
                for message, time_stamp_data in value.items():
                    text_data += (f'    {message} : {time_stamp_data}\n\n')
            self.ui.response_label.setText(str(text_data))


class ServerWorker(QObject):
    def __init__(self):
        '''

        A class that handles the server
        handles the server's POST and GET requests

        '''
        self.ip = get_local_ip()
        super().__init__()
        self.count = 0
        self.dumpstore = {'dump': {}}
        self.router = APIRouter()
        self.router.add_api_route('/receive', self.receive, methods=['POST'])
        self.router.add_api_route('/dump', self.dump, methods=['GET'])
        self.router.add_api_route('/ping', self.ping, methods=['GET'])

    def _current_time(self) -> str:
        '''

        A function that returns the current time

        '''
        now_timestamp = time.time()
        offset = datetime.fromtimestamp(
            now_timestamp) - datetime.utcfromtimestamp(now_timestamp)
        return (datetime.utcnow() + offset).strftime('%H:%M:%S')

    def _start(self) -> None:
        '''

        A function that startsthe server

        '''
        app = FastAPI()
        app.include_router(self.router)
        uvicorn.run(app, host=self.ip, port=8000)

    def receive(self, data: dict) -> dict:
        '''

        :param data: A dictionary containing a message
        :return: A dictionary containing a message
        :rtype: dict

        CLient POST request handler
        Called when a client sends a POST request to /receive
        A function that receives data and stores it in a dictionary with a timestamp
        uses a count to keep track of the number of messages received

        '''
        print('[+] [POST] /receive')
        convert = self._current_time()
        self.dumpstore['dump'][f'data{self.count}'] = data['message'] + \
            ' @' f' {convert}'
        self.count += 1
        response = {"message": "OK"}
        return response

    def dump(self) -> None:
        '''

        **Client GET request handler**
        Called when a client sends a GET request to /dump
        A function that returns dumpstore data

        '''
        print('[+] [GET] /dump')
        return self.dumpstore

    def ping(self) -> dict:
        '''
        :return: A dictionary containing a message
        :rtype: dict

        **Client GET request handler**
        Called when a client sends a GET request to /ping
        A function that returns a ping response

        '''
        print('[+] [GET] /ping')
        response = {"message": "OK"}
        return response


if __name__ == '__main__':
    app = QtWidgets.QApplication(sys.argv)
    w = MainWindow()
    w.show()
    sys.exit(app.exec())