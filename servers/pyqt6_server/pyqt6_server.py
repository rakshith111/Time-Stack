import sys
import socket
from PyQt6.QtWidgets import QApplication, QMainWindow, QTextEdit, QLineEdit, QPushButton
from PyQt6.QtCore import Qt
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
    def __init__(self, address, port, text_edit):
        super().__init__("WebSocket Server", QWebSocketServer.SslMode.NonSecureMode)
        self.listen(QHostAddress(address), port)
        self.newConnection.connect(self.on_new_connection)
        self.clients = []
        self.text_edit = text_edit

    def on_new_connection(self):
        socket = self.nextPendingConnection()
        socket.textMessageReceived.connect(self.process_text_message)
        socket.disconnected.connect(self.client_disconnected)
        self.clients.append(socket)
        client_address = socket.peerAddress().toString()
        self.text_edit.append(f"New client connected {client_address}")

    def process_text_message(self, message):
        sender = self.sender()
        self.text_edit.append(f"Received message from client: {message}")
        # Process the message (e.g., broadcast to all clients)
        for client in self.clients:
            if client != sender:
                client.sendTextMessage(message)

    def client_disconnected(self):
        sender = self.sender()
        if sender in self.clients:
            self.text_edit.append("Client disconnected")
            self.clients.remove(sender)

    def closeEvent(self, event):
        self.close()
        event.accept()

class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("WebSocket Server")
        self.setGeometry(100, 100, 400, 300)
        
        self.text_edit = QTextEdit(self)
        self.text_edit.setReadOnly(True)
        self.text_edit.setGeometry(10, 10, 380, 200)
        
        self.line_edit = QLineEdit(self)
        self.line_edit.setGeometry(10, 220, 250, 30)
        
        self.send_button = QPushButton("Send", self)
        self.send_button.setGeometry(270, 220, 120, 30)
        self.send_button.clicked.connect(self.send_message)
        
        local_ip = get_local_ip()
        if local_ip:
            self.server = WebSocketServer(local_ip, 8888, self.text_edit)
            if self.server.isListening():
                self.text_edit.append(f"WebSocket server is running on {local_ip}:8888.")
            else:
                self.text_edit.append("Failed to start WebSocket server.")
        else:
            self.text_edit.append("Failed to retrieve local IP address.")
    
    def send_message(self):
        message = self.line_edit.text()
        if self.server.clients:
            for client in self.server.clients:
                client.sendTextMessage(message)
            self.text_edit.append(f"Sent message: {message}")
        self.line_edit.clear()

if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = MainWindow()
    window.show()
    sys.exit(app.exec())
