import time
import PyQt6.QtCore as QtCore
from PyQt6 import QtWidgets
from PyQt6.QtCore import QThread, pyqtSignal
from _base_logger import logger
from _base_logger import CURRENT_DIR
from os import path
qss_file = open(path.join(CURRENT_DIR, 'ui_files', 'stack.qss'),'r').read()

class Thread(QThread):

    _signal = pyqtSignal(int)

    def __init__(self, maxsize):

        super(Thread, self).__init__()
        self.maxsize = maxsize

    def __del__(self):
        self.wait()

    def run(self):
        for count in range(self.maxsize+1):
            self._signal.emit(self.maxsize-count)
            time.sleep(1)

class Stack(QtWidgets.QWidget):
    def __init__(self, parent=None):
        '''
        Initializes the Stack class and sets up the UI.

        '''
        super().__init__(parent)
        self.stack_ui = QtWidgets.QVBoxLayout()
        self.btn = QtWidgets.QPushButton("Add")
        self.btn.clicked.connect(self.add_stack)
        self.stack_ui.addWidget(self.btn)
        self.start_btn=QtWidgets.QPushButton("Start")
        self.start_btn.clicked.connect(self.start_thread)
        self.stack_ui.addWidget(self.start_btn)
        self.setLayout(self.stack_ui)

    def start_thread(self):
        self.thread = Thread(self.progress_end)
        self.thread._signal.connect(self.signal_accept)
        self.thread.start()

    def add_stack(self):
        self.progress_end=100
        self.stack_ui.addWidget(self.return_stack_bar("test", self.progress_end))

    def return_stack_bar(self, task_name, progress_end):
        self.progressBar = QtWidgets.QProgressBar()
        self.progressBar.setRange(0, progress_end)
        self.progressBar.setStyleSheet(qss_file)
        self.progressBar.setFixedSize(180,250)
        self.progressBar.setFormat(f"{task_name} task @ %p%")
        self.progressBar.setOrientation(QtCore.Qt.Orientation.Vertical)
        self.progressBar.setAlignment(QtCore.Qt.AlignmentFlag.AlignCenter)
        self.progressBar.setTextVisible(True)
        self.progressBar.setValue(progress_end)

        return self.progressBar
    
    def signal_accept(self, progress):
        print(int(progress))
        self.progressBar.setValue(int(progress))
        if self.progressBar.value() == 0:
            self.progressBar.setValue(0)
            #self.progressBarsetEnabled(True)


if __name__ == '__main__':

    import sys
    app = QtWidgets.QApplication(sys.argv)
    app.setStyle('Fusion')
    w = Stack()
    w.show()
    sys.exit(app.exec())

