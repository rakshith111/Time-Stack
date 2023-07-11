import os
import sys
import socket
import json
import base64
import random
import string
from PyQt6.QtWidgets import QApplication, QMainWindow, QTextEdit, QLineEdit, QPushButton, QLabel, QVBoxLayout, QWidget
from PyQt6.QtGui import QPixmap


from PyQt6.QtWebSockets import QWebSocketServer, QWebSocket
from PyQt6.QtNetwork import QHostAddress
from pyqrcode import QRCode
from PyQt6.QtCore import QByteArray
from PyQt6.QtNetwork import QAbstractSocket

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
        self.text_edit = text_edit
        self.any_client_connected = []
        self.authed_clients = {}
        self.challenge_code=None
        self.newConnection.connect(self.on_new_connection)
        if not os.path.exists("challenge_code.json"):
            with open("challenge_code.json", "w") as f:
                json.dump({}, f)
        self.load_authed_clients()
    def load_authed_clients(self):
        
        with open("challenge_code.json", "r") as f:
            self.authed_clients = json.load(f)

    def save_authed_clients(self):
        with open("challenge_code.json", "w") as f:
            json.dump(self.authed_clients, f)
            
    def update_challenge_code(self,codes):
        self.challenge_code = codes[0]
        print(f"Challenge code: {self.challenge_code}")
    
    def on_new_connection(self):
        sender = self.sender()
        sender_ip = sender.peerAddress().toString()
        # previously authed client

        if sender_ip in self.authed_clients.values():
            print("Processing previously authed client")
            self.socket = self.nextPendingConnection()
            self.socket.textMessageReceived.connect(self.process_auth_text_message)
            self.socket.disconnected.connect(self.client_disconnected)
            self.any_client_connected.append(self.socket)
        # new client
        elif self.challenge_code:
            print("Processing new client")
            self.socket = self.nextPendingConnection()
            self.socket.textMessageReceived.connect(self.process_text_message)
            self.socket.disconnected.connect(self.client_disconnected)
            self.any_client_connected.append(self.socket)        
            for i in self.any_client_connected:
                print(i.peerAddress().toString())
            self.save_authed_clients()
        else:
            print("Disconnecting client as no challenge code is set")
            # Disconnect the client after sending error message
            self.socket = self.nextPendingConnection()
            self.socket.disconnect()
            self.socket.deleteLater()
            self.socket = None


    def process_auth_text_message(self, message):
        sender = self.sender()
        json_data = self.convert_str_to_json(message)
        sender_ip = sender.peerAddress().toString()
        if sender_ip in self.authed_clients.values():
            print("Verifying previously authed client ")
            # Reverse lookup key from value
            key = list(self.authed_clients.keys())[list(self.authed_clients.values()).index(sender_ip)]
            if key== json_data["challenge_code"]:
                self.text_edit.append("Challenge code matched")
                self.text_edit.append("welcome old user")
                self.text_edit.append("Client connected")
                # Further processing --------- for send and receive
            else:
                self.text_edit.append("Challenge code did not match")
                self.text_edit.append("Client not authenticated")
                self.text_edit.append("Client disconnected")
                sender.close()
                print("Client disconnected as challenge code did not match")
           

    def process_text_message(self, message):
        sender = self.sender()
        json_data = self.convert_str_to_json(message)
        sender_ip = sender.peerAddress().toString()
        # Verify if "challenge_code" key is present in the json_data
        
        if "challenge_code" in json_data.keys():
            received_challenge_code = json_data["challenge_code"]
            if received_challenge_code == self.challenge_code:
                print("Challenge code matched for new client")
                self.text_edit.append("Challenge code matched")
                self.authed_clients[received_challenge_code] = sender_ip
                self.text_edit.append("New Client authenticated")
                self.text_edit.append("Client connected")
                self.text_edit.append(f"Client address: {sender_ip}")
                self.save_authed_clients()
                self.socket.textMessageReceived.connect(self.process_auth_text_message)
            else:
                print("Challenge code did not match for new client")
                self.text_edit.append("Challenge code did not match")
                self.text_edit.append("Client not authenticated")
                self.text_edit.append("Client disconnected")
                sender.close()
        else:
            print("Challenge code not found in json data")
            self.text_edit.append("Client not authenticated")
            self.text_edit.append("Client disconnected")
            sender.close()

    
    def client_disconnected(self):
        print("Client disconnected from server")

    def convert_str_to_json(self, data):
        try:
            data=json.loads(data)
            return data
    
        except Exception as e:
            print(e)

class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("WebSocket Server")
        self.setGeometry(100, 100,700, 700)
        self.box_layout = QVBoxLayout()
        self.text_edit = QTextEdit(self)
        self.text_edit.setReadOnly(True)
        self.text_edit.setGeometry(10, 10, 480, 400)

        self.line_edit = QLineEdit(self)
        self.line_edit.setGeometry(10, 220, 250, 30)

        self.send_button = QPushButton("Send", self)
        self.send_button.setGeometry(270, 220, 120, 30)
        self.box_layout.addWidget(self.text_edit)
        self.box_layout.addWidget(self.line_edit)
        self.box_layout.addWidget(self.send_button)
        self.central_widget = QWidget()
        self.central_widget.setLayout(self.box_layout)
        self.gen_qr_code_btn = QPushButton("Generate QR Code", self)
        self.gen_qr_code_btn.setGeometry(10, 260, 380, 30)
        self.gen_qr_code_btn.clicked.connect(self.set_qr_code)
        self.label = QLabel(self)
        self.box_layout.addWidget(self.label)
        self.box_layout.addWidget(self.gen_qr_code_btn)

        self.setCentralWidget(self.central_widget)
        self.__codes = list()
        self._auth_clients = {}
        self.locale_ip = get_local_ip()
        if self.locale_ip:
            self.server = WebSocketServer(self.locale_ip, WEB_SOCKET_SERVER_PORT, self.text_edit)
            if self.server.isListening():
                self.text_edit.append(f"Server is listening on {self.locale_ip}:{WEB_SOCKET_SERVER_PORT}")
            else:
                self.text_edit.append(f"Server is not listening on {self.locale_ip}:{WEB_SOCKET_SERVER_PORT}")
        else:
            self.text_edit.append("No internet connection")
        # self.send_button.clicked.connect(self.send_message)

    def generate_challenge_code(self, length=15):
        '''Generates a random string of uppercase letters and digits
        '''
        return ''.join(random.choices(string.ascii_uppercase + 
                                    string.digits+
                                    string.ascii_lowercase,
                                     k=length))

    def generate_qr_code(self, data):
        '''Secure QR code generator

        Args:
            data (dict): The data to be encoded in the QR code

        Returns:
            str: Base64 encoded QR code image
        '''
       
        qr=QRCode(json.dumps(data))
        temp_qr_image_path = os.path.join(os.path.dirname(__file__), "temp_qr_image.png")
        qr.png(temp_qr_image_path,scale=8)
        with open(temp_qr_image_path, "rb") as f:
            base64_encoded = base64.b64encode(f.read())
        os.remove(temp_qr_image_path)
        self.label.setPixmap
        return base64_encoded
    
    def set_qr_code(self):
        '''Sets the QR code image to the label

        Args:
            data (str): Base64 encoded QR code image
        '''
        self.secret_gen_code = self.generate_challenge_code()
        self.qr_data={
            "Secret":self.secret_gen_code,
            "IP":self.locale_ip,
            "Port":WEB_SOCKET_SERVER_PORT
        }
        print(self.qr_data)
        self.qr_code = self.generate_qr_code(self.qr_data)
        pixmap=QPixmap()
        pixmap.loadFromData(base64.b64decode(self.qr_code))
        self.label.setPixmap(pixmap)
        self.label.setGeometry(10, 200, 300,300)
        self.label.show()
        self.text_edit.setGeometry(10, 10, 480, 180)
        self.__codes=[]
        self.__codes.append(self.secret_gen_code)
        self.server.update_challenge_code(self.__codes)
        
      
if __name__ == "__main__":
    WEB_SOCKET_SERVER_PORT =8888
    app = QApplication(sys.argv)
    main_window = MainWindow()
    main_window.show()    
    sys.exit(app.exec())