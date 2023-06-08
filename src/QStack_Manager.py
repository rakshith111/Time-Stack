import time
import random
import sys

import PyQt6.QtCore as QtCore
from PyQt6.QtCore import QThread, pyqtSignal
from PyQt6 import QtWidgets
from PyQt6.QtWidgets import QApplication, QProgressBar, QWidget, QVBoxLayout, QMenu, QPushButton
from PyQt6.QtGui import QAction

from os import path
from copy import copy

from libs._base_logger import logger
from libs._base_logger import BASE_DIR


QSS_FILE = open(path.join(BASE_DIR, 'ui_files', 'stack.qss'), 'r').read()


class Thread(QThread):
    '''   
    Thread class that handles the timer for the progress bar.

    Args:
        QThread (_type_): Inherits from QThread class.
    '''

    _signal = pyqtSignal(int)

    def __init__(self, maxsize: int, name: str) -> None:
        '''
        Initializes the thread class. Sets the name of the thread, the maxsize of the progress bar, and the current value of the progress bar.
        _is_running: A boolean that determines if the thread is running.
        currentvalue: The current value of the progress bar. Starts at maxsize. Stores the value when paused and resumes from that value.

        Args:
            maxsize (int): The max size of the progress bar.
            name (str): The name of the thread.
        '''

        super(Thread, self).__init__()
        self.name = name
        self.maxsize = maxsize
        self._is_running = True
        self.current_value = copy(self.maxsize)

    def __del__(self) -> None:
        '''
        When the thread is deleted, it will terminate the thread.
        '''
        self.terminate()

    def pause(self, value: int) -> None:
        '''    
        When the thread is paused, it will set the current value of the progress bar to the current_value attribute.
        It will also set the _is_running attribute to False to stop the thread from running.
        Args:
            value (int): The current value of the progress bar.
        '''
        self.current_value = value
        self._is_running = False
        logger.info(
            f"Pausing {self.name} @ {self.current_value} Is running?={self._is_running}")

    def resume(self) -> None:
        '''
        When the thread is resumed,it will emit the current_value attribute to the progress bar. So the progress bar will resume from the current_value attribute from when it was paused.
        After that, it will set the _is_running attribute to True to start the thread.
        '''
        self._signal.emit(self.current_value)
        self._is_running = True
        logger.info(
            f"Resuming  {self.name} @ {self.current_value} Is running?={self._is_running}")

    def run(self) -> None:
        '''
        Private method that runs the thread. This function is called when the start() method is invoked, it will subtract 1 from the current_value attribute.
        It will emit the current_value attribute to the progress bar. So the progress bar will update.
        If the current_value attribute is less than or equal to 0, it will set the current_value attribute to 0 and set the _is_running attribute to False to stop the thread.
        '''
        while True:
            if self._is_running:
                self.current_value -= 1
                if self.current_value <= 0:
                    self.current_value = 0
                    self._is_running = False
                self._signal.emit(self.current_value)
            time.sleep(1)


class StackActivityBar(QProgressBar):
    '''
    StackActivityBar class modifies the QProgressBar class to add a context menu and a thread to handle the timer.

    Args:
        QProgressBar (_type_): Inherits from QProgressBar class.
    '''

    _remove_signal = QtCore.pyqtSignal()

    def __init__(self, name: str, progress_end: int, parent=None) -> None:
        '''
        The QProgressBar class is initialized with the following parameters:
            - parent: The parent widget.
            - setFixedSize: Sets the fixed size of the progress bar. (Currently fixed But will be changed to dynamic in the future.)
            - setOrientation: Sets the orientation of the progress bar. (Vertical)
            - setAlignment: Sets the alignment of the progress bar. (Center)
            - setTextVisible: Sets the text visible of the progress bar. (True)
            - setFormat: Sets the format of the progress bar. (f"{name} task @ %p%") 
            - setRange: Sets the range of the progress bar. (0, progress_end)
            - setValue: Sets the value of the progress bar. (progress_end)
            - setObjectName: Sets the object name of the progress bar. (f"{name}")
            - setStyleSheet: Sets the stylesheet of the progress bar. (A basic stylesheet is used for now. Will be changed to a dynamic stylesheet in the future.)
        _thread: The thread that handles the timer for the progress bar.
        _remove_signal: A signal that is emitted when the progress bar is removed.


        Args:
            name (str): The name of the progress bar.
            progress_end (int): The max size of the progress bar.
            parent (_type_, optional): Defaults to None.

        '''
        super(StackActivityBar, self).__init__(parent)
        self.setFixedSize(180, 250)
        self.setOrientation(QtCore.Qt.Orientation.Vertical)
        self.setAlignment(QtCore.Qt.AlignmentFlag.AlignCenter)
        self.setTextVisible(True)
        self.setFormat(f"{name} task @ %p%")
        self.setRange(0, progress_end)
        self.setValue(progress_end)
        self.setObjectName(f"{name}")
        self.setStyleSheet(QSS_FILE)

        self._thread = Thread(progress_end, f"Thread_{name}")
        self._thread._signal.connect(self.setValue)
        self._remove_signal.connect(self.deleteLater)

    def closeEvent(self, event):
        '''
        When the progress bar is closed, it will terminate the thread.
        '''
        self._thread.terminate()

    def contextMenuEvent(self, event) -> None:
        '''
        When the progress bar is right clicked, it will create a context menu with the following options:
            - Pause: When the pause option is clicked, it will pause the thread.
            - Resume: When the resume option is clicked, it will resume the thread.
        If the thread is paused, the resume option will be shown and vice versa.
        Currently the resume option only works from here and not from the `StackManager` class.
        '''
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

    def setValue(self, value: int) -> None:
        '''
        This is a modified version of the setValue method from the QProgressBar class.
        When the value of the progress bar is set, it will check if the value is 0.
        If the value is 0, it will emit the _remove_signal signal to remove the progress bar from the stack.

        Args:
            value (int): The value of the progress bar.
        '''

        super().setValue(value)
        if value == 0:
            self._remove_signal.emit()
            logger.info(f"Removing via Expired timer - {self.objectName()}")


class StackManager():
    '''
    StackManager class manages the stack of progress bars.
    Currently supoorted functions for the STACK:
        - Add a progress bar to the stack.
        - Start the thread of the top progress bar in the stack.
        - Pause the thread of the top progress bar in the stack.
        - Remove the top progress bar in the stack.
    - `print_stack()` is used for debugging purposes.

    '''

    def __init__(self, layout: QtWidgets.QLayout) -> None:
        '''
        The layout is set to the layout that is passed to the class. i.e Stack Space layout.
        stack_bar: The progress bar that is currently being added to the stack.
        stack_top_item: The progress bar that is currently at the top of the stack.
        stack_items: A list of all the progress bars in the stack.


        Args:
            layout (QtWidgets.QLayout): The layout that the progress bars will be added to.
        '''
        self.layout = layout
        self.stack_top_item = None
        self.stack_items = []

    def add_stack(self, name: str, progress_end: int) -> StackActivityBar:
        '''
        This function creates a new progress bar and adds it to the stack. Also the signal is connected to the remove_top_stack function.
        The progress bar is added to the layout and the stack_items list.
        The stack_top_item is set to the progress bar that is currently at the top of the stack.

        Args:
            name (str): The name of the progress bar.
            progress_end (int):  The max size of the progress bar.

        Returns:
            StackActivityBar (StackActivityBar): The progress bar that is currently being added to the stack.

        '''
        self.stack_bar = StackActivityBar(f"{name}", progress_end)
        self.stack_bar._remove_signal.connect(self.pop_top_stack)
        self.layout.addWidget(self.stack_bar)
        if self.stack_top_item is None:
            self.stack_top_item = self.stack_bar
        self.stack_items.append(self.stack_bar)
        return self.stack_bar

    def start_thread(self) -> None:
        '''
        Starts the thread of the top progress bar in the stack.
        '''
        logger.info(f"Starting thread - {self.stack_top_item.objectName()}")
        if self.stack_top_item is not None:
            self.stack_top_item._thread.start()
        # Add error msg for if there is no stack bar in the stack

    def pause_thread(self) -> None:
        if self.stack_top_item is not None:
            self.stack_top_item._thread.pause(self.stack_top_item.value())

    def pop_top_stack(self) -> None:
        '''
        Removes the top progress bar in the stack.
        The progress bar is removed from the layout and the stack_items list.
        The stack_top_item is set to the progress bar that is currently at the top of the stack.
        '''

        if self.stack_top_item is not None:

            logger.info(
                f"Removing via btn - {self.stack_top_item.objectName()}")
            self.layout.removeWidget(self.stack_top_item)
            self.stack_top_item.deleteLater()
            self.stack_items.remove(self.stack_top_item)
            if len(self.stack_items) > 0:
                self.stack_top_item = self.stack_items[0]
            else:
                self.stack_top_item = None
        # Add error msg for if there is no stack bar in the stack

    def printer(self):
        '''
        This function is used for debugging purposes.
        Logs the name of the progress bar that is currently at the top of the stack.
        Logs the names of all the progress bars in the stack.

        '''
        if len(self.stack_items) > 0:
            logger.debug("Current top is " + self.stack_top_item.objectName())
            logger.debug("Stack Items")
            for item in self.stack_items:
                logger.debug(f"Item - {item.objectName()}")


class Stack(QWidget):
    def __init__(self, parent=None):
        '''
        :warning: This class is temporary and will be removed in the future. Only used for testing purposes.
        Initializes the Stack class and sets up the UI.
        Temporary class for testing the StackManager class.

        Args:
            parent (_type_, optional): Defaults to None.
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
        self.remove_btn.clicked.connect(self.pop_top_active)
        self.start_btn.clicked.connect(self.start_thread)

        self.setLayout(self.stack_ui)

        self.manager = StackManager(self.layout())
        self.progress_end = 30
        self.debug_btn.clicked.connect(self.manager.printer)

    def add_stack_bar(self):
        '''
        Adds a progress bar to the stack.
        '''
        rand = random.randint(1, 100)
        self.manager.add_stack(f"test_{rand}", self.progress_end)

    def start_thread(self):
        '''
        Starts the thread of the top progress bar in the stack.
        '''
        self.manager.start_thread()

    def pop_top_active(self):
        '''
        Removes the top progress bar in the stack.
        '''
        self.manager.pop_top_stack()


if __name__ == '__main__':

    app = QApplication(sys.argv)
    app.setStyle('Fusion')
    w = Stack()
    w.show()
    sys.exit(app.exec())
