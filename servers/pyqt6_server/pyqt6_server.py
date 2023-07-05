import sys
import socket
from PyQt6.QtCore import QCoreApplication, Qt
from PyQt6.QtWebSockets import QWebSocketServer, QWebSocket
from PyQt6.QtNetwork import QHostAddress

def get_local_ip() -> str:
    '''
    A function that returns the local IP address.
    It makes a connection to Google's DNS server and returns the local IP address.
    '''
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        local_ip = s.getsockname()[0]
        s.close()
        return local_ip
    except:
        return None

class WebSocketServer(QWebSocketServer):
    def __init__(self, address, port):
        super().__init__("WebSocket Server", QWebSocketServer.SslMode.NonSecureMode)
        self.listen(QHostAddress(address), port)
        self.newConnection.connect(self.on_new_connection)
        self.clients = []

    def on_new_connection(self):
        socket = self.nextPendingConnection()
        socket.textMessageReceived.connect(self.process_text_message)
        socket.disconnected.connect(self.client_disconnected)
        self.clients.append(socket)
        print("New client connected")

    def process_text_message(self, message):
        sender = self.sender()
        print(f"Received message from client: {message}")
        # Process the message (e.g., broadcast to all clients)
        for client in self.clients:
            if client != sender:
                client.sendTextMessage(message)

    def client_disconnected(self):
        sender = self.sender()
        if sender in self.clients:
            print("Client disconnected")
            self.clients.remove(sender)

    def closeEvent(self, event):
        self.close()
        event.accept()

if __name__ == "__main__":
    app = QCoreApplication(sys.argv)
    local_ip = get_local_ip()
    if local_ip:
        server = WebSocketServer(local_ip, 8888)
        if server.isListening():
            print(f"WebSocket server is running on {local_ip}:8888.")
        else:
            print("Failed to start WebSocket server.")
            sys.exit(1)
    else:
        print("Failed to retrieve local IP address.")
        sys.exit(1)
    sys.exit(app.exec())
