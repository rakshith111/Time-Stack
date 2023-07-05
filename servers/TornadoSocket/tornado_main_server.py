import tornado.ioloop
import tornado.web
import tornado.websocket
import socket

class SocketHandler(tornado.websocket.WebSocketHandler):
    def open(self):
        print("New connection opened")

    def on_message(self, message):
        print("Received message:", message)
        # Process the received message and send a response
        response = "Response to: " + message
        self.write_message(response)

    def on_close(self):
        print("Connection closed")

def make_app():
    return tornado.web.Application([
        (r"/socket", SocketHandler),
    ])
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
if __name__ == "__main__":
    app = make_app()
    # Set ip
    app.listen(8888,address=get_local_ip())
    print("Server started on port 8888")
    tornado.ioloop.IOLoop.current().start()
