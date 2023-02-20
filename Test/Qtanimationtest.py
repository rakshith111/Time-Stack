
import os
import subprocess
import sys
from pathlib import Path

from PyQt6 import QtCore, QtQuickWidgets, QtWidgets
from PyQt6.QtCore import Qt
from PyQt6.QtWidgets import (QApplication, QLabel, QScrollArea, QVBoxLayout,
                             QWidget)

CURRENT_DIRECTORY = Path(__file__).resolve().parent
if __name__ == "__main__":


    app = QtWidgets.QApplication(sys.argv)
    label_widget = QtWidgets.QLabel("Animation", alignment=QtCore.Qt.AlignmentFlag.AlignCenter)
    widget = QtWidgets.QWidget()
    box_layout = QtWidgets.QVBoxLayout()
    subprocess.Popen([sys.executable, "-m", "http.server", "-b", "127.0.0.1", "-d","test/animation"])
    widget.setLayout(box_layout)
    animation_widget = QtQuickWidgets.QQuickWidget(resizeMode=QtQuickWidgets.QQuickWidget.ResizeMode.SizeRootObjectToView )
    filename = os.fspath(CURRENT_DIRECTORY / "player.qml")
    url = QtCore.QUrl.fromLocalFile(filename)
    animation_widget.setSource(url)
    box_layout.addWidget(label_widget, stretch=0, )
    box_layout.addWidget(animation_widget, stretch=2)
    widget.resize(600, 600)
    widget.show()

    sys.exit(app.exec())




# class ScrollableWindow(QWidget):
#     def __init__(self):
#         super().__init__()

#         # create a scroll area widget and set its properties
#         scroll_area = QScrollArea()
#         scroll_area.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOn)
#         scroll_area.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOn)
#         scroll_area.setWidgetResizable(True)

#         # create a widget to hold the content of the scroll area
#         content_widget = QWidget(scroll_area)
#         scroll_area.setWidget(content_widget)

#         # create a layout for the content widget
#         content_layout = QVBoxLayout(content_widget)
#         content_layout.setAlignment(Qt.AlignmentFlag.AlignTop)

#         # add some content to the content layout
#         animation_widget = QtQuickWidgets.QQuickWidget(
#             resizeMode=QtQuickWidgets.QQuickWidget.ResizeMode.SizeRootObjectToView
#         )
#         filename = os.fspath(CURRENT_DIRECTORY / "test.qml")
#         url = QtCore.QUrl.fromLocalFile(filename)
#         animation_widget.setSource(url)
#         content_layout.addWidget(animation_widget)
        

#         # create a layout for the scrollable window
#         main_layout = QVBoxLayout(self)
#         main_layout.addWidget(scroll_area)


# if __name__ == '__main__':
#     app = QApplication(sys.argv)

#     window = ScrollableWindow()
#     window.show()

#     sys.exit(app.exec())
