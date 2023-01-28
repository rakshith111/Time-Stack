from fastapi import FastAPI, APIRouter
from time import gmtime, strftime
import uvicorn
class Server:

    def __init__(self):
        self.router = APIRouter()
        self.router.add_api_route("/dump", self.dump, methods=["GET"])
        self.router.add_api_route("/send", self.send, methods=["POST"])
        self.map={"dump":{}}

    def dump(self):
        print(self.map)
        return self.map

    def send(self, data: dict):
        print(data)
        convert = strftime("%H:%M:%S", gmtime())
        self.map["dump"][convert]=data["message"]
        return "OK"

app = FastAPI()
hello = Server()
app.include_router(hello.router)

if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8000)