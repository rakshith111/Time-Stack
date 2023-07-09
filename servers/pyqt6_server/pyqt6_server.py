import os
import sys
import socket
import json
import base64
import random
import string
from PyQt6.QtWidgets import QApplication, QMainWindow, QTextEdit, QLineEdit, QPushButton, QLabel, QVBoxLayout, QWidget
from PyQt6.QtGui import QPixmap

from PyQt6.QtWebSockets import QWebSocketServer 
from PyQt6.QtNetwork import QHostAddress
from pyqrcode import QRCode
from PyQt6.QtCore import QByteArray

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
        self.new_clients = {}
        self._auth_clients = {}
        self.text_edit = text_edit

    def on_new_connection(self):
        socket = self.nextPendingConnection()
            
        socket.textMessageReceived.connect(self.process_text_message)
        socket.disconnected.connect(self.client_disconnected)

        self.challenge_code=self.generate_challenge_code()
        print(f"Challenge code: {self.challenge_code}")
        self.qr_code_window = QRCodeWindow(self.generate_qr_code(self.challenge_code))
        self.new_clients[socket] = self.challenge_code
        self.qr_code_window.show()
  
    def convert_str_to_json(self, data):

        try:
            data=json.loads(data)
            return data
    
        except Exception as e:
            print(e)

    
    def process_text_message(self, message):
        sender = self.sender()
        print(f"Received message from client: {message}")
        json_data=self.convert_str_to_json(message)

        if sender in self.new_clients:
            challenge_code = self.new_clients[sender]
            received_challenge_code = json_data["challenge_code"]
            if received_challenge_code == challenge_code:
                self.text_edit.append("Challenge code verified")
                self.text_edit.append("Client authenticated")
                self.text_edit.append("Client IP: " + sender.peerAddress().toString())

              
            else:
                self.text_edit.append("Invalid challenge code")
        else:
            self.text_edit.append("Unknown client")


    def generate_challenge_code(self, length=6):
        return ''.join(random.choices(string.ascii_uppercase + string.digits, k=length))


    def generate_qr_code(self, data):
        '''Secure QR code generator

        Args:
            data (str): The data to be encoded in the QR code

        Returns:
            str: Base64 encoded QR code image
        '''
        qr = QRCode(data, error="H")
        temp_qr_image_path = os.path.join(os.path.dirname(__file__), "temp_qr_image.png")
        qr.png(temp_qr_image_path,scale=8)
        with open(temp_qr_image_path, "rb") as f:
            base64_encoded = base64.b64encode(f.read())
        os.remove(temp_qr_image_path)
        return base64_encoded

    def client_disconnected(self):
        sender = self.sender()
        if sender in self.new_clients:
            self.text_edit.append("Client disconnected")
            del self.new_clients[sender]

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
        sender = "Server"
        content = self.line_edit.text()
        self.text_edit.append(f"Sent message: {content}")
        for client, challenge_code in self.server.new_clients.items():
            client.sendTextMessage(content)

        self.line_edit.clear()

class QRCodeWindow(QWidget):
    def __init__(self, qr_code):
        super().__init__()
        self.setWindowTitle("QR Code")
        self.setGeometry(400, 400, 300, 300)
        
        layout = QVBoxLayout()

        label = QLabel(self)
        pixmap = QPixmap()
        # Load the image from path
        pixmap.loadFromData(QByteArray.fromBase64(qr_code))
        label.setPixmap(pixmap)
        layout.addWidget(label)
        
        self.setLayout(layout)

if __name__ == "__main__":
    app = QApplication(sys.argv)
    main_window = MainWindow()
    main_window.show()
    
    
    
    sys.exit(app.exec())