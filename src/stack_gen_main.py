import datetime
import sys
import logging

from PyQt6 import QtWidgets
from PyQt6.QtCore import QObject, QThread, QTimer
import PyQt6.QtCore as QtCore

from ui_modules.Stackgen import Ui_Stackgen



class Stackgenwindow(QtWidgets.QMainWindow):

    def __init__(self, parent=None) -> None:
        '''
        Initializes the Stackgen class and sets up the UI.
        '''

        super(Stackgenwindow, self).__init__(parent=parent)

        self.stackgen_ui = Ui_Stackgen()
        self.stackgen_ui.setupUi(self)
        logger.info('Stackgen UI initialized')
        self.stackgen_ui.total_time_output.setText("00:00")
        self.stackgen_ui.total_time_output.setAlignment(
            QtCore.Qt.AlignmentFlag.AlignCenter)
        self.stackgen_ui.stack_name_input.setAlignment(
            QtCore.Qt.AlignmentFlag.AlignCenter)
        self.stackgen_ui.create_stack_button.clicked.connect(self.create_stack)

    def create_stack(self) -> None:
        '''

        This function is called when the create stack button is clicked.
        Converts the start and end time to datetime.time objects and then calculates the total time.
        has to be done this way because the time objects are not compatible with the datetime module.


        '''
        self.stack_name = self.stackgen_ui.stack_name_input.toPlainText()
        self.start_time_input = self.stackgen_ui.start_time_input.time()
        self.end_time_input = self.stackgen_ui.end_time_input.time()
        dt_time1 = datetime.time(
            self.start_time_input.hour(), self.start_time_input.minute())
        dt_time2 = datetime.time(
            self.end_time_input.hour(), self.end_time_input.minute())
        delta = (datetime.datetime.combine(datetime.date.today(), dt_time2) -
                 datetime.datetime.combine(datetime.date.today(), dt_time1))
        temp = datetime.datetime.combine(
            datetime.date.today(), datetime.time()) + delta
        self.stackgen_ui.total_time_output.setText(temp.strftime("%H:%M"))
        self.stackgen_ui.total_time_output.setAlignment(
            QtCore.Qt.AlignmentFlag.AlignCenter)
        # Create a new thread to run the stack generator
        logger.debug(
            f'Contents stack_name: {self.stack_name}, start_time_input: {dt_time1}, end_time_input: {dt_time2}, total_time_output: {temp}')


if __name__ == '__main__':
    logger = logging.getLogger('QTApp')
    logger.setLevel(logging.DEBUG)
    file_handler = logging.FileHandler('QTApp.log')
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s',datefmt='%I:%M:%S %p %m/%d/%Y')
    file_handler.setFormatter(formatter)
    logger.addHandler(file_handler)
    app = QtWidgets.QApplication(sys.argv)
    w = Stackgenwindow()
    w.show()
    sys.exit(app.exec())
