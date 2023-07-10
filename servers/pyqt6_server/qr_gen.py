from pyqrcode import QRCode
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

class QRCodeWindow(QWidget):
    def __init__(self, qr_code):
        super().__init__()
        print(type(qr_code))
        print(qr_code)
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
    qr_code = QRCode("http://sdfsd"
                        "sdfsdf")
    qr_code.png("qr_code.png", scale=8)
    with open("qr_code.png", "rb") as f:
        qr_code = base64.b64encode(f.read())
    print(qr_code)
    main_window = QRCodeWindow(qr_code)
    main_window.show()
    sys.exit(app.exec())
