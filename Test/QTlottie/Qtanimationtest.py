
from PyQt6 import QtCore, QtWidgets, QtQuickWidgets
import os
import subprocess
import sys
from pathlib import Path

from PyQt6 import QtCore, QtQuickWidgets, QtWidgets


class MyWidget(QtWidgets.QWidget):
    def __init__(self):
        super().__init__()
        # Create the input field
        self.input_field = QtWidgets.QLineEdit(self)
        self.input_field.setPlaceholderText("Enter a JSON file URL")
        self.input_field.setText("http://127.0.0.1:8000/full_loading.json")

        # Create the submit button
        self.submit_button = QtWidgets.QPushButton("Submit", self)
        self.submit_button.clicked.connect(self.submit)
        label_widget = QtWidgets.QLabel(
            "Animation", alignment=QtCore.Qt.AlignmentFlag.AlignCenter)
        box_layout = QtWidgets.QVBoxLayout(self)
        box_layout.addWidget(label_widget, stretch=0)
        box_layout.addWidget(self.input_field)
        box_layout.addWidget(self.submit_button)

        # Start a local HTTP server to serve the QML file
        subprocess.Popen([sys.executable, "-m", "http.server",
                         "-b", "127.0.0.1", "-d", "test/animation"])

        # Create the QML widget
        self.animation_widget = QtQuickWidgets.QQuickWidget(
            self, resizeMode=QtQuickWidgets.QQuickWidget.ResizeMode.SizeRootObjectToView)
        filename = os.fspath(CURRENT_DIRECTORY / "player.qml")
        url = QtCore.QUrl.fromLocalFile(filename)
        self.animation_widget.setSource(url)
        box_layout.addWidget(self.animation_widget, stretch=2)

        self.resize(600, 600)
        self.show()

        # Set a property on the root QML object to pass data from Python to QML
        self.root_object = self.animation_widget.rootObject()

    def submit(self):
        # Get the value of the input field
        input_value = self.input_field.text()

        # Use the value to update the QML animation

        self.root_object.setProperty("Src", input_value)


if __name__ == "__main__":

    CURRENT_DIRECTORY = Path(__file__).resolve().parent
    app = QtWidgets.QApplication(sys.argv)
    widget = MyWidget()
    sys.exit(app.exec())
