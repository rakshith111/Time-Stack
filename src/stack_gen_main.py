import datetime
import sys
import logging

from PyQt6 import QtWidgets
from PyQt6.QtCore import QObject, QThread, QTimer
import PyQt6.QtCore as QtCore
from PyQt6.QtGui import QCloseEvent

from ui_modules.Stack_gen import Ui_stack_gen
from ui_modules.Stack_space import Ui_stack_space


class StackSpace(QtWidgets.QWidget):

    def __init__(self, parent=None) -> None:
        '''
        Initializes the Stackgen class and sets up the UI.
        '''

        super(StackSpace, self).__init__(parent=parent)
        self.stack_gen_space_ui = Ui_stack_space()
        self.stack_gen_space_ui.setupUi(self)
        logger.info('StackSpace UI initialized')

    def closeEvent(self, event: QCloseEvent):
        # Override the closeEvent and hide the window instead of closing it
        event.ignore()
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
        self.stack_gen_ui.create_stack_button.clicked.connect(self.create_stack)
        self.stack_gen_space = StackSpace()


    def create_stack(self) -> None:
        '''

        This function is called when the create stack button is clicked.
        Converts the start and end time to datetime.time objects and then calculates the total time.
        has to be done this way because the time objects are not compatible with the datetime module.


        '''
        self.stack_name = self.stack_gen_ui.stack_name_input.toPlainText()
        self.start_time_input = self.stack_gen_ui.start_time_input.time()
        self.end_time_input = self.stack_gen_ui.end_time_input.time()
        dt_time1 = datetime.time(
            self.start_time_input.hour(), self.start_time_input.minute())
        dt_time2 = datetime.time(
            self.end_time_input.hour(), self.end_time_input.minute())
        delta = (datetime.datetime.combine(datetime.date.today(), dt_time2) -
                 datetime.datetime.combine(datetime.date.today(), dt_time1))
        temp = datetime.datetime.combine(
            datetime.date.today(), datetime.time()) + delta
        self.stack_gen_ui.total_time_output.setText(temp.strftime("%H:%M"))
        self.stack_gen_ui.total_time_output.setAlignment(
            QtCore.Qt.AlignmentFlag.AlignCenter)
        # Create a new thread to run the stack generator
        logger.debug(
            f'Contents stack_name: {self.stack_name}, start_time_input: {dt_time1}, end_time_input: {dt_time2}, total_time_output: {temp}')
        self.stack_gen_space.show()

if __name__ == '__main__':
    logger = logging.getLogger('QTApp')
    logger.setLevel(logging.DEBUG)
    file_handler = logging.FileHandler('QTApp.log')
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s',datefmt='%I:%M:%S %p %m/%d/%Y')
    file_handler.setFormatter(formatter)
    logger.addHandler(file_handler)
    app = QtWidgets.QApplication(sys.argv)
    w = StackGen()
    w.show()
    sys.exit(app.exec())
