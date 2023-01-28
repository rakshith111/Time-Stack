from fastapi import FastAPI, APIRouter
from time import gmtime, strftime
import uvicorn


class Server:

    def __init__(self):
        self.router = APIRouter()
        self.router.add_api_route("/dump", self.dump, methods=["GET"])
        self.router.add_api_route("/send", self.send, methods=["POST"])
        self.count = 0
        self.dumpstore = {"dump": {}}


    def send(self, data: dict):
        print("[+] [GET] /send")
        convert = strftime("%H:%M:%S", gmtime())
        self.dumpstore["dump"][f'data{self.count}'] =data["message"]+ " @" f" {convert}"
        self.count += 1
        return "OK"

    def dump(self):
        print("[+] [POST] /dump")
        return self.dumpstore

    def start(self):
        app = FastAPI()
        hello = Server()
        app.include_router(hello.router)
        uvicorn.run(app, host="127.0.0.1", port=8000)
        
if __name__ == "__main__":
    Server().start()