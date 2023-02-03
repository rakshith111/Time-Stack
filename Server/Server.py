import datetime
import time
from fastapi import FastAPI, APIRouter
from time import gmtime, strftime
import uvicorn


class Server:

    def __init__(self):
        '''
        A simple server that can be used to POST (dump) and receive (message)
        A dump function is provided to dump the data
        A receive function is provided to receive data
        '''
        self.router = APIRouter()
        self.router.add_api_route("/dump", self.dump, methods=["GET"])
        self.router.add_api_route("/receive", self.receive, methods=["POST"])
        self.count = 0
        self.dumpstore = {"dump": {}}

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
        '''
        print("[+] [GET] /receive")
        convert = self._current_time()
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

    def start(self):
        app = FastAPI()
        hello = Server()
        app.include_router(hello.router)
        uvicorn.run(app, host="127.0.0.1", port=8000)


if __name__ == "__main__":
    Server().start()
