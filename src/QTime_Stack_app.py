import sys
import pathlib
import datetime
from PyQt6.QtCore import Qt
from PyQt6 import QtGui, QtWidgets, QtCore
from PyQt6.QtGui import QCloseEvent,QAction,QMouseEvent
from PyQt6.QtWidgets import QApplication, QSystemTrayIcon, QMenu, QMessageBox

from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from libs.color import Color
from libs.QClasses.QDragWidget import DragWidget
from libs.QClasses.QScrollArea import DragScrollArea

from QStack_Manager import StackManager
from QStack_Settings import ThemeManager
from ui_generated.time_stack import Ui_MainWindow

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
        self.setWindowFlags(self.windowFlags() | Qt.WindowType.FramelessWindowHint)
        self.current_theme = "dark"
        self.close_to_tray=True
        self.showed_notification = False
        self.dragging = False
        self.offset = None
        #Tray Icon
        self.tray_icon = QSystemTrayIcon(self)
        self.tray_icon.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) /'src'/ 'ui_files' / 'icon' / 'window_icon_wob_s.png')))     
        self.tray_menu = QMenu(self)
        self.show_action = QAction("Show", self)
        self.quit_action = QAction("Quit", self)
        self.add_stack=QAction("Add Stack",self)
        self.stack_space=QAction("Stack Space",self)
        self.settings=QAction("Settings",self)
        self.network=QAction("Network",self)
        self.tray_menu.addAction(self.show_action)
        self.tray_menu.addAction(self.add_stack)
        self.tray_menu.addAction(self.stack_space)
        self.tray_menu.addAction(self.settings)
        self.tray_menu.addAction(self.network)        
        self.tray_menu.addAction(self.quit_action)
        self.show_action.triggered.connect(self.showWindow)
        self.quit_action.triggered.connect(self.quitApplication)
        self.add_stack.triggered.connect(lambda:self.time_stack_ui.main_tab_widget.setCurrentIndex(1))
        self.stack_space.triggered.connect(lambda:self.time_stack_ui.main_tab_widget.setCurrentIndex(0))
        self.settings.triggered.connect(lambda:self.time_stack_ui.main_tab_widget.setCurrentIndex(2))
        self.network.triggered.connect(lambda:self.time_stack_ui.main_tab_widget.setCurrentIndex(3))

        self.tray_icon.setContextMenu(self.tray_menu)
        self.tray_icon.show()

        self.setWindowIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) /'src'/ 'ui_files' / 'icon' / 'window_icon_wob_s.png')))
        self._init_stack_space()
        self._init_stack_generator()
        self.theme_manager = ThemeManager( time_stack_ui=self.time_stack_ui, parent=self)
        self.welcome_timer = QtCore.QTimer()
        self.welcome_timer.singleShot(100, self.manager.welcome)

        self.time_stack_ui.main_tab_widget.setIconSize(QtCore.QSize(20,20))
        self.time_stack_ui.add_btn.clicked.connect(lambda:self.time_stack_ui.main_tab_widget.setCurrentIndex(1))
        self.time_stack_ui.settings_btn.clicked.connect(lambda:self.time_stack_ui.main_tab_widget.setCurrentIndex(2))
        self.time_stack_ui.logo_placeholder.setScaledContents(True)

        self.time_stack_ui.start_btn.setFixedSize(50, 50)
        self.time_stack_ui.pause_btn.setFixedSize(50, 50)
        self.time_stack_ui.remove_btn.setFixedSize(50, 50)
        self.time_stack_ui.start_btn.setIconSize(QtCore.QSize(50, 50))
        self.time_stack_ui.pause_btn.setIconSize(QtCore.QSize(50, 50))
        self.time_stack_ui.remove_btn.setIconSize(QtCore.QSize(50, 50))

        self.time_stack_ui.minimize_btn.clicked.connect(self.showMinimized)
        self.time_stack_ui.close_btn.clicked.connect(self.closeEvent)
        self.time_stack_ui.close_btn.setFixedSize(30, 30)
        self.time_stack_ui.minimize_btn.setFixedSize(30, 30)
        self.time_stack_ui.minimize_btn.setText("")
        self.time_stack_ui.close_btn.setText("")
        

    def mousePressEvent(self, event: QMouseEvent):
        try:
            if event.button() == Qt.MouseButton.LeftButton:
                self.dragging = True
                self.offset = event.pos()
        except Exception as e:
            logger.error(f"{Color.RED}Error in mousePressEvent: {e}{Color.ENDC}")

    def mouseMoveEvent(self, event: QMouseEvent):
        try:
            if event.buttons() & Qt.MouseButton.LeftButton and self.dragging:
                new_pos = self.pos() + event.pos() - self.offset
                # Restrict the new position within the available screen geometry
                screen = QApplication.screens()[0]  # Get the first screen
                available_rect = screen.availableGeometry()
                new_pos.setX(max(available_rect.left(), min(new_pos.x(), available_rect.right() - self.width())))
                new_pos.setY(max(available_rect.top(), min(new_pos.y(), available_rect.bottom() - self.height())))
                self.move(new_pos)
        except Exception as e:
            logger.error(f"{Color.RED}Error in mouseMoveEvent: {e}{Color.ENDC}")

    def mouseReleaseEvent(self, event: QMouseEvent):
        self.dragging = False

    def keyPressEvent(self, event: QtGui.QKeyEvent) -> None:
        '''
        Function to handle key press events.

        Args:
            event (QtGui.QKeyEvent): The key press event.
        '''        
        # Ctrl + C to quit the application
        if event.modifiers() == QtCore.Qt.KeyboardModifier.ControlModifier and event.key() == QtCore.Qt.Key.Key_C:
            self.quitApplication()

        # Esc to hide the application
        elif event.key() == QtCore.Qt.Key.Key_Escape:
            self.hide()

    def showWindow(self):
        '''
        Function to show the application window.
        '''        
        self.show()
    
    def quitApplication(self):
        '''
        Function to quit the application.
        '''
        logger.info(f"{Color.HEADER}Quitting application.{Color.ENDC}")
        self.tray_icon.deleteLater()
        self.manager.save_stack()
        QApplication.quit()
    

    def closeEvent(self, event: QCloseEvent) -> None:
        '''
        Function to handle the close event.

        Args:
            event (QCloseEvent): The close event.
        '''
        if self.close_to_tray:
            if self.tray_icon.isVisible():
                self.hide()
                if not self.showed_notification:
                    self.showed_notification = True
                    self.manager.notification(
                        title="TimeStack",
                        message="The application will keep running in the system tray. To quit the application, select Quit in the menu shown by right-clicking the icon.",
                        type_notify="general")
                try:
                    event.ignore()
                except:
                    pass
        else:
            self.quitApplication()
    
    def _init_stack_space(self) -> None:
        '''
        Function to initialize the stack space.
        Creates a DragWidget and DragScrollArea. 
        Sets the DragScrollArea to the layout.
        Connects the DragWidget to the StackManager.
        Connects the DragWidget to the DragScrollArea.
        '''        
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
   
    def _init_stack_generator(self) -> None:
        '''
        Function to initialize the stack generator.
        Populates the start and end time inputs with the current time.
        Sets the total time output to 00:00.
        Connects the create stack button to the create_stack function.
        '''    
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
        self.time_stack_ui.main_tab_widget.setCurrentIndex(0)

    

        
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
        self.manager.add_stack(activity_name=f"{name}",mode='Habit',start_time=dt_start_time, stop_time=dt_stop_time, max_size=total_seconds)
        logger.info(
            f"{Color.GREEN} Contents stack_name: {name},total_seconds: {total_seconds} start_time_input: {dt_start_time}, end_time_input: {dt_stop_time}{Color.ENDC}")
    


if __name__ == "__main__":
    try:
        logger.info(f"{Color.HEADER}TimeStack initializing.{Color.ENDC}")
        app = QApplication(sys.argv)
        window = TimeStack()
        window.show()
        sys.exit(app.exec())
    except Exception as e:
        logger.error(f"{Color.RED}Error in main: {e}{Color.ENDC}")
        