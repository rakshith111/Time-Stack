import tornado.ioloop
import tornado.web
import tornado.websocket

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

if __name__ == "__main__":
    app = make_app()
    # Set ip
    app.listen(8888,address='127.0.0.1')
    print("Server started on port 8888")
    tornado.ioloop.IOLoop.current().start()
