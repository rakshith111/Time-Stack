import time
import random
import sys

import PyQt6.QtCore as QtCore
from PyQt6.QtCore import QThread, pyqtSignal
from PyQt6.QtWidgets import QApplication, QProgressBar, QWidget, QVBoxLayout, QMenu, QPushButton
from PyQt6.QtGui import QAction

from os import path
from copy import copy

from _base_logger import logger
from _base_logger import CURRENT_DIR


qss_file = open(path.join(CURRENT_DIR, 'ui_files', 'stack.qss'), 'r').read()


class Thread(QThread):

    _signal = pyqtSignal(int)

    def __init__(self, maxsize, name):
        super(Thread, self).__init__()
        self.name = name
        self.maxsize = maxsize
        self._is_running = True
        self.currentvalue = copy(self.maxsize)

    def __del__(self):
        self.wait()

    def pause(self, value):
        self.currentvalue = value
        self._is_running = False
        logger.info(
            f"Pause {self.name} @  {self.currentvalue} Is running? ={self._is_running}")

    def resume(self):
        self._signal.emit(self.currentvalue)
        self._is_running = True
        logger.info(
            f"Resume {self.name}@  {self.currentvalue}  Is running? ={self._is_running}")

    def run(self):
        while True:
            if self._is_running:
                self.currentvalue -= 1
                if self.currentvalue <= 0:
                    self.currentvalue = self.maxsize
                self._signal.emit(self.currentvalue)
            time.sleep(1)


class StackBar(QProgressBar):
    def __init__(self, name, progress_end, parent=None):
        super(StackBar, self).__init__(parent)
        self.setFixedSize(180, 250)
        self.setOrientation(QtCore.Qt.Orientation.Vertical)
        self.setAlignment(QtCore.Qt.AlignmentFlag.AlignCenter)
        self.setTextVisible(True)
        self.setFormat(f"{name} task @ %p%")
        self.setRange(0, progress_end)
        self.setValue(progress_end)
        self.setObjectName(f"{name}")
        self.setStyleSheet(qss_file)

        self._thread = Thread(progress_end, f"Thread_{name}")
        self._thread._signal.connect(self.setValue)

    def closeEvent(self, event):
        self._thread.terminate()

    def contextMenuEvent(self, event):
        menu = QMenu(self)

        if self._thread._is_running:
            pause_action = QAction("Pause", self)
            pause_action.triggered.connect(
                lambda: self._thread.pause(self.value()))
            menu.addAction(pause_action)
        elif not self._thread._is_running:
            resume_action = QAction("Resume", self)
            resume_action.triggered.connect(self._thread.resume)
            menu.addAction(resume_action)

        menu.exec(event.globalPos())


class StackManager():
    def __init__(self, layout):
        self.layout = layout

    def add_stack(self, name, progress_end):

        self.stack_bar = StackBar(f"{name}", progress_end)
        self.layout.addWidget(self.stack_bar)
        return self.stack_bar

    def remove_top_stack(self, stack_bar):
        logger.info(f"Removing {stack_bar.objectName()}")
        self.layout.removeWidget(stack_bar)
        stack_bar.deleteLater()


class Stack(QWidget):
    def __init__(self, parent=None):
        '''
        Initializes the Stack class and sets up the UI.

        '''
        super().__init__(parent)
        self.stack_ui = QVBoxLayout()
        self.add_btn = QPushButton("Add")
        self.start_btn = QPushButton("Start")
        self.remove_btn = QPushButton("Remove")
        self.debug_btn = QPushButton("Debug")

        self.stack_ui.addWidget(self.remove_btn)
        self.stack_ui.addWidget(self.add_btn)
        self.stack_ui.addWidget(self.start_btn)
        self.stack_ui.addWidget(self.debug_btn)

        self.add_btn.clicked.connect(self.add_stack_bar)
        self.remove_btn.clicked.connect(self.pop_top_acive)
        self.start_btn.clicked.connect(self.start_thread)
        self.debug_btn.clicked.connect(self.printer)

        self.setLayout(self.stack_ui)
        self.stack_top_item = None
        self.stack_items = []
        self.manager = StackManager(self.layout())
        self.progress_end = 100

    def add_stack_bar(self):
        rand = random.randint(1, 100)
        stack_bar_item = self.manager.add_stack(
            f"test_{rand}", self.progress_end)
        self.stack_items.append(stack_bar_item)
        if self.stack_top_item is None:
            self.stack_top_item = stack_bar_item

    def start_thread(self):
        if self.stack_top_item is not None:
            self.stack_top_item._thread.start()
        # Add error msg for if there is no stack bar in the stack

    def pop_top_active(self):
        if self.stack_top_item is not None:
            self.manager.remove_top_stack(self.stack_top_item)
            self.stack_items.remove(self.stack_top_item)
            if len(self.stack_items) > 0:
                self.stack_top_item = self.stack_items[0]
            else:
                self.stack_top_item = None

    def printer(self):
        print(self.stack_items)
        if self.stack_top_item is not None:
            print("now top is " + self.stack_top_item.objectName())


if __name__ == '__main__':

    from _base_logger import logger

    app = QApplication(sys.argv)
    app.setStyle('Fusion')
    w = Stack()
    w.show()
    sys.exit(app.exec())
