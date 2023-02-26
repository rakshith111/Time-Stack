import datetime
import logging
import logging.config
import sys
import time
from os import path
import random
import PyQt6.QtCore as QtCore
from PyQt6 import QtWidgets
from PyQt6.QtCore import QObject, QThread, QTimer, pyqtSignal
from PyQt6.QtGui import QCloseEvent
from PyQt6.QtWidgets import (QApplication, QMessageBox, QProgressBar,
                             QPushButton, QVBoxLayout, QWidget)

from ui_modules.Stack_gen import Ui_stack_gen
from ui_modules.Stack_space import Ui_stack_space
from QStack_Manager import StackManager


class StackSpace(QtWidgets.QWidget):

    def __init__(self, parent=None) -> None:
        '''
        Initializes the Stackgen class and sets up the UI.

        '''

        super(StackSpace, self).__init__(parent=parent)
        self.stack_gen_space_ui = Ui_stack_space()
        self.stack_gen_space_ui.setupUi(self)
        self.custom_widget = Stack()
        scroll_layout = QtWidgets.QVBoxLayout()
        self.vertical_scrollbar = self.stack_gen_space_ui.stack_area_scrollable.verticalScrollBar()
        self.stack_gen_space_ui.stack_area_scrollable.widget().setLayout(scroll_layout)
        self.manager = StackManager(
            self.stack_gen_space_ui.stack_area_scrollable.widget().layout())
        self.stack_top_item = None
        self.stack_items = []

    def printer(self):
        if len(self.stack_items) > 0:
            logger.debug("Stack Items")
            for item in self.stack_items:
                logger.debug(f"Item - {item.objectName()}")
        if self.stack_top_item is not None:
            logger.debug("Current top is " + self.stack_top_item.objectName())

    def add_stack_bar(self, name, dt_start_time, dt_stop_time):
        logger.debug(
            f'Scrollbar Status  is  {self.vertical_scrollbar.isVisible()}')
        delta = dt_stop_time-dt_start_time
        total_seconds = int(delta.total_seconds())

        rand = random.randint(1, 100)
        stack_bar_item = self.manager.add_stack(
            f"{name}_{rand}", total_seconds)
        logger.info(
            f'Contents stack_name: {name},total_seconds: {total_seconds} start_time_input: {dt_start_time}, end_time_input: {dt_stop_time}, ')
        self.stack_items.append(stack_bar_item)

        if self.stack_top_item is None:
            self.stack_top_item = stack_bar_item

        self.printer()

    def add_stack(self, name, start_time, end_time) -> None:
        '''
        Adds a custom widget to the scroll area.

        Currently creates a new label and adds it to the scroll area.
        This function is called when the create stack button is clicked.
        For now it just creates a label with the name of the stack.

        '''

        label = self.custom_widget.create_label(name=name)
        scroll_layout = self.stack_gen_space_ui.stack_area_scrollable.widget().layout()
        scroll_layout.addWidget(label)
        logger.info(f'Label created with name {name}')
        logger.debug(
            f'Parms name: {name}, start_time: {start_time}, end_time: {end_time}')
        logger.debug(
            f'Scrollbar Status  is  {self.vertical_scrollbar.isVisible()}')

    def closeEvent(self, event: QCloseEvent):
        '''
        Overrides the closeEvent function to hide the window instead of closing it.
        '''
        # Override the closeEvent and hide the window instead of closing it
        event.ignore()
        logger.info('StackSpace Hide event called')
        self.hide()


class StackGen(QtWidgets.QWidget):

    def __init__(self, parent=None) -> None:
        '''
        Initializes the Stackgen class and sets up the UI.
        '''

        super(StackGen, self).__init__(parent=parent)
        self.stack_gen_ui = Ui_stack_gen()
        self.stack_gen_ui.setupUi(self)
        logger.info('Stackgen UI initialized')
        self.stack_gen_ui.total_time_output.setText("00:00")
        self.stack_gen_ui.total_time_output.setAlignment(
            QtCore.Qt.AlignmentFlag.AlignCenter)
        self.stack_gen_ui.stack_name_input.setAlignment(
            QtCore.Qt.AlignmentFlag.AlignCenter)
        self.stack_gen_ui.create_stack_button.clicked.connect(
            self.create_stack)

        self.warningmsg = QMessageBox()
        self.warningmsg.setIcon(QMessageBox.Icon.Warning)
        self.warningmsg.setWindowTitle("Error")
        self.warningmsg.setStandardButtons(QMessageBox.StandardButton.Close)

        self.informationmsg = QMessageBox()
        self.informationmsg.setIcon(QMessageBox.Icon.Information)
        self.informationmsg.setGeometry(QtCore.QRect(800, 600, 651, 300))
        self.informationmsg.setWindowTitle("Information")
        self.informationmsg.setStandardButtons(QMessageBox.StandardButton.Ok)
        self.stack_space = StackSpace()

    def create_stack(self) -> None:
        '''

        This function is called when the create stack button is clicked.
        Converts the start and end time to datetime.time objects and then calculates the total time.
        has to be done this way because the time objects are not compatible with the datetime module.
        Calls the `add_stack` function in the `StackSpace` class to add the stack to the scroll area.


        '''
        self.stack_name = self.stack_gen_ui.stack_name_input.toPlainText()
        self.start_time_input = self.stack_gen_ui.start_time_input.time()
        self.end_time_input = self.stack_gen_ui.end_time_input.time()
        dt_start_time = datetime.datetime.now().replace(hour=self.start_time_input.hour(),
                                                        minute=self.start_time_input.minute(), second=0, microsecond=0)
        dt_stop_time = datetime.datetime.now().replace(hour=self.end_time_input.hour(),
                                                       minute=self.end_time_input.minute(), second=0, microsecond=0)
        delta = dt_stop_time-dt_start_time
        temp = datetime.datetime.combine(
            datetime.date.today(), datetime.time()) + delta
        self.stack_gen_ui.total_time_output.setText(temp.strftime("%H:%M"))
        self.stack_gen_ui.total_time_output.setAlignment(
            QtCore.Qt.AlignmentFlag.AlignCenter)

        if self.stack_name == "":
            self.warningmsg.setText("Please enter a stack name")
            self.warningmsg.exec()
            return
        if dt_start_time > dt_stop_time:
            self.warningmsg.setText(
                "End time cannot be before start time")
            self.warningmsg.exec()
            return
        if dt_start_time == dt_stop_time:
            self.warningmsg.setText(
                "End time cannot be the same as start time")
            self.warningmsg.exec()
            return
        self.informationmsg.setText(
            f"Stack {self.stack_name} created with start time {dt_start_time} and end time {dt_stop_time}")
        self.informationmsg.exec()
        self.stack_space.add_stack_bar(
            self.stack_name, dt_start_time, dt_stop_time)
        # self.stack_space.add_stack(
        # self.stack_name, dt_start_time, dt_stop_time)
        self.stack_space.show()

    def closeEvent(self, event: QCloseEvent):
        '''
        Overrides the closeEvent function to close subwindows then close itself .
        '''
        self.stack_space.close()
        logger.info('Stackgen closed')


class Stack(QtWidgets.QWidget):
    def __init__(self, parent=None):
        '''
        Initializes the Stack class and sets up the UI.

        '''
        super().__init__(parent)

    def create_label(self, name) -> QtWidgets.QLabel:
        '''
        :param str name: The name of the stack
        :return: A label with the name of the stack
        Creates a label with the name of the stack.
        For now it just creates a label with the name of the stack.
        later it will create a custom widget with the name of the stack and the start and end time.
        '''

        label = QtWidgets.QLabel(name)
        label.setFixedSize(200, 100)  # Set the label's fixed size
        return label


if __name__ == '__main__':

    from _base_logger import logger

    app = QtWidgets.QApplication(sys.argv)
    w = StackGen()
    w.show()
    sys.exit(app.exec())
