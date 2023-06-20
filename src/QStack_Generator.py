import datetime
import sys
import random

import PyQt6.QtCore as QtCore
from PyQt6.QtCore import Qt
from PyQt6 import QtWidgets
from PyQt6.QtGui import QCloseEvent, QIcon
from PyQt6.QtWidgets import QMessageBox, QVBoxLayout, QScrollBar
from os import path

from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from libs.color import Color

from ui_generated.stack_create import Ui_stack_gen
from ui_generated.stack_space import Ui_stack_space

from libs.QClasses.QDragWidget import DragWidget
from libs.QClasses.QScrollArea import DragScrollArea

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
        self.setAcceptDrops(True)

        self.stack_space_ui.setupUi(self)
        self.stack_space_ui.start_btn.setIcon(QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'play-button.png')))
        self.stack_space_ui.pause_btn.setIcon(QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'pause-button.png')))
        self.stack_space_ui.remove_btn.setIcon(QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'remove.png')))

        # Create DragWidget and DragScrollArea
        self.drag_widget = DragWidget()
        self.drag_scroll_area = DragScrollArea()
        self.drag_scroll_area.setAlignment(Qt.AlignmentFlag.AlignHCenter)
        self.drag_scroll_area.setWidgetResizable(True)
        self.drag_scroll_area.setWidget(self.drag_widget)
        # Remove existing scrollbar
        self.stack_space_ui.stack_area_scrollable.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff)

        # Set DragScrollArea directly to the layout
        layout = QVBoxLayout(self.stack_space_ui.stack_area_scrollable)
        layout.setAlignment(Qt.AlignmentFlag.AlignHCenter)
        layout.addWidget(self.drag_scroll_area)

        # Create StackManager and pass the layout of DragWidget
        self.manager = StackManager(self.drag_widget.layout())

        # Connect button signals to respective slots
        self.stack_space_ui.start_btn.clicked.connect(self.manager.start_thread)
        self.stack_space_ui.pause_btn.clicked.connect(self.manager.pause_thread)
        self.stack_space_ui.remove_btn.clicked.connect(self.manager.pop_top_stack)
   
    def closeEvent(self, event: QCloseEvent) -> None:
        '''
        Overrides the closeEvent function to hide the window instead of closing it.

        Args:
            event (QCloseEvent): Close event object
        '''
        # Override the closeEvent and hide the window instead of closing it
        event.ignore()
        logger.info(f"{Color.HEADER}Stack Space closed.{Color.ENDC}")
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
        # logger.debug(
        #     f"{Color.GREEN} Scrollbar Status  is  {self.vertical_scrollbar.isVisible()}{Color.ENDC}"
        # )
        delta = dt_stop_time-dt_start_time
        total_seconds = int(delta.total_seconds())
        rand = random.randint(1, 100)
        self.manager.add_stack(f"{name}_{rand}", total_seconds)
        logger.info(
            f"{Color.GREEN} Contents stack_name: {name},total_seconds: {total_seconds} start_time_input: {dt_start_time}, end_time_input: {dt_stop_time}{Color.ENDC}")

class StackGenerator(QtWidgets.QWidget):

    def __init__(self,stack_space, parent=None) -> None:
        ''' Initializes the Stackgen class and sets up the UI.

        Args:
            stack_space (QtWidgets.QWidget): StackSpace object
            parent (optional):  Defaults to None.
        '''            
        super(StackGenerator, self).__init__(parent=parent)
        self.stack_gen_ui = Ui_stack_gen()
        self.setAcceptDrops(True)
        self.setWindowIcon(QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'hourglass_blackw.jpg')))
        self.stack_gen_ui.setupUi(self)
        logger.info(f"{Color.HEADER}Stack Generator initialized.{Color.ENDC}")
        self.stack_gen_ui.start_time_input.setTime(
            QtCore.QTime.currentTime())
        self.stack_gen_ui.end_time_input.setTime(QtCore.QTime.currentTime().addSecs(60))
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
    window = StackGenerator(stack_space)
    app.setStyle('Fusion')
    window.show()
    sys.exit(app.exec())
