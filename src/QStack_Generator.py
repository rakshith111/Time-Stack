import datetime
import sys
import random

import PyQt6.QtCore as QtCore
from PyQt6 import QtWidgets
from PyQt6.QtGui import QCloseEvent, QIcon
from PyQt6.QtWidgets import QMessageBox
from os import path

from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from ui_generated.stack_create import Ui_stack_gen
from ui_generated.stack_space import Ui_stack_space

from QStack_Manager import StackManager

class StackSpace(QtWidgets.QWidget):

    def __init__(self, parent=None) -> None:
        '''
        Initializes the Stackgen class and sets up the UI.
        Sets up the scroll area and the stack manager.
        Adds icons to the buttons.     

        Args:
            parent (optional):  Defaults to None.
        '''
        
        super(StackSpace, self).__init__(parent=parent)
        self.stack_space_ui = Ui_stack_space()
        self.setWindowIcon(QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'hourglass_blackw.jpg')))
        self.setWindowTitle('Stack Space')

        self.stack_space_ui.setupUi(self)
        self.stack_space_ui.start_btn.setIcon(
            QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'play-button.png')))
        self.stack_space_ui.pause_btn.setIcon(
            QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'pause-button.png')))
        self.stack_space_ui.remove_btn.setIcon(
            QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'remove.png')))
        scroll_layout = QtWidgets.QVBoxLayout()
        scroll_layout.setAlignment(QtCore.Qt.AlignmentFlag.AlignJustify)
        self.vertical_scrollbar = self.stack_space_ui.stack_area_scrollable.verticalScrollBar()
        self.stack_space_ui.stack_area_scrollable.widget().setLayout(scroll_layout)

        self.manager = StackManager(
            self.stack_space_ui.stack_area_scrollable.widget().layout())

        self.stack_space_ui.start_btn.clicked.connect(
            self.manager.start_thread)
        self.stack_space_ui.pause_btn.clicked.connect(
            self.manager.pause_thread)
        self.stack_space_ui.remove_btn.clicked.connect(
            self.manager.pop_top_stack)

    def closeEvent(self, event: QCloseEvent) -> None:
        '''
        Overrides the closeEvent function to hide the window instead of closing it.

        Args:
            event (QCloseEvent): Close event object
        '''
        # Override the closeEvent and hide the window instead of closing it
        event.ignore()
        logger.info('StackSpace Hide event called')
        self.hide()

    def add_stack_activity(self, name: str, dt_start_time: datetime.time, dt_stop_time: datetime.time) -> None:
        '''      
        This function is called when the create button is clicked.
        Converts the start and end time to datetime.time objects and then calculates the total time.
        has to be done this way because the time objects are not compatible with the datetime module.
        delta: `datetime.timedelta` object used to get total seconds.

        Args:
            name (str): Name of the stack
            dt_start_time (datetime.time): Start time of the stack
            dt_stop_time (datetime.time): End time of the stack
        '''
        logger.debug(
            f'Scrollbar Status  is  {self.vertical_scrollbar.isVisible()}')
        delta = dt_stop_time-dt_start_time
        total_seconds = int(delta.total_seconds())
        rand = random.randint(1, 100)
        self.manager.add_stack(f"{name}_{rand}", total_seconds)
        logger.info(
            f'Contents stack_name: {name},total_seconds: {total_seconds} start_time_input: {dt_start_time}, end_time_input: {dt_stop_time}, ')


class StackGen(QtWidgets.QWidget):

    def __init__(self,stack_space, parent=None) -> None:
        ''' Initializes the Stackgen class and sets up the UI.

        Args:
            stack_space (QtWidgets.QWidget): StackSpace object
            parent (optional):  Defaults to None.
        '''            
        super(StackGen, self).__init__(parent=parent)
        self.stack_gen_ui = Ui_stack_gen()
        self.setWindowIcon(QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'hourglass_blackw.jpg')))
        self.stack_gen_ui.setupUi(self)
        logger.info('Stackgen UI initialized')
        self.stack_gen_ui.start_time_input.setTime(
            QtCore.QTime.currentTime())
        self.stack_gen_ui.end_time_input.setTime(
            QtCore.QTime.currentTime())
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
        self.informationmsg.setGeometry(QtCore.QRect(800, 600, 650, 300))
        self.informationmsg.setWindowTitle("Information")
        self.informationmsg.setStandardButtons(QMessageBox.StandardButton.Ok)
        self.stack_space = stack_space

    def closeEvent(self, event: QCloseEvent) -> None:
        '''
        Overrides the closeEvent function to close subwindows then close itself.

        Args:
            event (QCloseEvent): Close event object
        '''
        event.ignore()
        logger.info('StackGen Hide event called')
        self.hide()

    def create_stack(self) -> None:
        '''
        This function is called when the create stack button is clicked.
        Converts the start and end time to datetime.time objects and then calculates the total time.
        has to be done this way because the time objects are not compatible with the datetime module.
        Calls the `add_stack` function in the `StackSpace` class to add the stack to the scroll area.

        '''
        self.activity_stack_name = self.stack_gen_ui.stack_name_input.toPlainText()
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

        if self.activity_stack_name == "":
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
            f"Stack {self.activity_stack_name} created with start time {dt_start_time} and end time {dt_stop_time}")
        self.informationmsg.exec()
        self.stack_space.add_stack_activity(
            self.activity_stack_name, dt_start_time, dt_stop_time)
        self.stack_space.show()

if __name__ == '__main__':

    app = QtWidgets.QApplication(sys.argv)
    stack_space=StackSpace()
    window = StackGen(stack_space)
    app.setStyle('Fusion')
    window.show()
    sys.exit(app.exec())
