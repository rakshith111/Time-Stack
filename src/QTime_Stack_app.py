import sys
import pathlib
import datetime
from PyQt6.QtCore import Qt
from PyQt6 import QtGui, QtWidgets, QtCore
from PyQt6.QtGui import QCloseEvent,QAction
from PyQt6.QtWidgets import QApplication, QSystemTrayIcon, QMenu, QMessageBox, QVBoxLayout

from ui_generated.time_stack import Ui_MainWindow
from QTheme_Manager import ThemeManager

from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from libs.color import Color
from libs.QClasses.QDragWidget import DragWidget
from libs.QClasses.QScrollArea import DragScrollArea

from QStack_Generator import StackSpace
from QStack_Generator import StackGenerator
from QStack_Settings import SettingsWindow
from QStack_Manager import StackManager

class TimeStack(QtWidgets.QMainWindow):

    def __init__(self, parent=None) -> None:
        '''
        Initializes the Stackgen class and sets up the UI.
        Sets up the scroll area and the stack manager.
        Adds icons to the buttons.     

        Args:
            parent (optional):  Defaults to None.
        '''
        
        super().__init__(parent=parent)                
        self.time_stack_ui = Ui_MainWindow()
        self.time_stack_ui.setupUi(self)
        self.setAcceptDrops(True)
        self.current_theme = "dark"
        self.close_to_tray=True
        self.setWindowIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'ui_files' / 'icon' / 'window_icon_wob_s.png')))
  
        self.theme_manager = ThemeManager(time_stack_ui=self.time_stack_ui, parent=self)

        self._init_stack_space()
        self._init_stack_generator()
    
    def _init_stack_generator(self) -> None:
        logger.info(f"{Color.HEADER}Stack Generator initializing.{Color.ENDC}")
        self.time_stack_ui.start_time_input.setTime(QtCore.QTime.currentTime())
        self.time_stack_ui.end_time_input.setTime(QtCore.QTime.currentTime().addSecs(60))
        self.time_stack_ui.total_time_output.setText("00:00")
        self.time_stack_ui.total_time_output.setAlignment(
            QtCore.Qt.AlignmentFlag.AlignCenter)
        self.time_stack_ui.stack_name_input.setAlignment(
            QtCore.Qt.AlignmentFlag.AlignCenter)
        self.time_stack_ui.create_stack_button.clicked.connect(
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

    def create_stack(self) -> None:
        '''
        This function is called when the create stack button is clicked.
        Converts the start and end time to datetime.datetimeobjects and then calculates the total time.
        has to be done this way because the time objects are not compatible with the datetime module.
        Calls the `add_stack` function in the `StackSpace` class to add the stack to the scroll area.

        '''
        self.activity_stack_name = self.time_stack_ui.stack_name_input.toPlainText()
        self.start_time_input = self.time_stack_ui.start_time_input.time()
        self.end_time_input = self.time_stack_ui.end_time_input.time()
        dt_start_time = datetime.datetime.now().replace(hour=self.start_time_input.hour(),
                                                        minute=self.start_time_input.minute(), second=0, microsecond=0)
        dt_stop_time = datetime.datetime.now().replace(hour=self.end_time_input.hour(),
                                                       minute=self.end_time_input.minute(), second=0, microsecond=0)
        delta = dt_stop_time-dt_start_time
        temp = datetime.datetime.combine(
            datetime.date.today(), datetime.time()) + delta
        self.time_stack_ui.total_time_output.setText(temp.strftime("%H:%M"))
        self.time_stack_ui.total_time_output.setAlignment(
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

        self.add_stack_activity(
            self.activity_stack_name, dt_start_time, dt_stop_time)
        self.time_stack_ui.stack_name_input.clear()
        self.time_stack_ui.start_time_input.setTime(
            QtCore.QTime.currentTime())
        self.time_stack_ui.end_time_input.setTime(
            QtCore.QTime.currentTime().addSecs(60))
        self.time_stack_ui.total_time_output.setText("00:00")
        self.time_stack_ui.total_time_output.setAlignment(
            QtCore.Qt.AlignmentFlag.AlignCenter)
        #  switch tab to stack space
        self.time_stack_ui.main_tab_widget.setCurrentIndex(1)

    def _init_stack_space(self) -> None:
            # STACK SPACE
            # Create DragWidget and DragScrollArea
            logger.info(f"{Color.HEADER}Stack Space initializing.{Color.ENDC}")
            self.drag_widget = DragWidget()
            self.drag_scroll_area = DragScrollArea()
            
            self.drag_scroll_area.setAlignment(Qt.AlignmentFlag.AlignHCenter)
            self.drag_scroll_area.setWidgetResizable(True)
            self.drag_scroll_area.setWidget(self.drag_widget)
            self.time_stack_ui.stack_area_scrollable.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff)

            # Set DragScrollArea directly to the layout
            layout = QtWidgets.QVBoxLayout(self.time_stack_ui.stack_area_scrollable)
            layout.setAlignment(Qt.AlignmentFlag.AlignHCenter)
            layout.addWidget(self.drag_scroll_area)

            self.manager=StackManager(self.drag_widget.layout())
            self.drag_widget._order_changed_singal.connect(self.manager.update_top_item)

            self.time_stack_ui.start_btn.clicked.connect(self.manager._start_thread)
            self.time_stack_ui.pause_btn.clicked.connect(self.manager._pause_thread)
            self.time_stack_ui.remove_btn.clicked.connect(self.manager.pop_top_stack)

        
    def add_stack_activity(self, name: str, dt_start_time: datetime.datetime, dt_stop_time: datetime.datetime) -> None:
            '''      
            This function is called when the create button is clicked.
            Converts the start and end time to datetime.datetime objects and then calculates the total time.
            has to be done this way because the time objects are not compatible with the datetime module.
            delta: `datetime.timedelta` object used to get total seconds.

            Args:
                name (str): Name of the stack
                dt_start_time (datetime.datetime): Start time of the stack
                dt_stop_time (datetime.datetime): End time of the stack
            '''

            delta = dt_stop_time-dt_start_time
            total_seconds = int(delta.total_seconds())
            self.manager.add_stack(activity_name=f"{name}",mode='Casual',start_time=dt_start_time, stop_time=dt_stop_time, max_size=total_seconds)
            logger.info(
                f"{Color.GREEN} Contents stack_name: {name},total_seconds: {total_seconds} start_time_input: {dt_start_time}, end_time_input: {dt_stop_time}{Color.ENDC}")
        


if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = TimeStack()
    window.show()
    sys.exit(app.exec())