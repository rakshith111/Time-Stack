import pathlib
import random
import sys
import datetime

from notifypy import Notify
from PyQt6.QtCore import Qt
from PyQt6.QtWidgets import QApplication
from PyQt6.QtWidgets import QMessageBox, QWidget, QPushButton, QVBoxLayout,QApplication

from libs.QClasses.QDragWidget import DragWidget
from libs.QClasses.QScrollArea import DragScrollArea
from libs.QClasses.QProgressBar import StackActivityBar

from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from libs.color import Color
from libs.util import rng_gen, clean_input_name
from db_manager import connect_db, disconnect_db, check_db


PATH_TO_SOUNDS = pathlib.Path(BASE_DIR, 'src', 'ui_files', 'sounds')
MONTH_YEAR = datetime.datetime.now().strftime("%B_%Y")


class StackManager:

    def __init__(self, layout):
        '''
        StackManager class manages the stack of progress bars.
        The layout is set to the layout that is passed to the class. i.e Stack Space layout.
        stack_bar: The progress bar that is currently being added to the stack.
        stack_top_item: The progress bar that is currently at the top of the stack.
        stack_items: A list of all the progress bars in the stack.
        print_stack()` is used for debugging purposes

        Args:
            layout (QLayout): The layout that the progress bars will be managed in.
        '''
        self.layout = layout
        self.stack_top_item = None
        self.stack_items = []
        self.notifications_enabled = True
        self.notification_sound_general = ""
        self.notification_sound_midway = ""
        self.notification_sound_quarterly = ""
        self.notification_sound_end = ""
        self._network_manager = None
        self.warningmsg = QMessageBox()
        self.warningmsg.setIcon(QMessageBox.Icon.Warning)
        self.warningmsg.setWindowTitle("Error")
        self.warningmsg.setStandardButtons(QMessageBox.StandardButton.Close)
        self.layout.setAlignment(Qt.AlignmentFlag.AlignHCenter)
        check_db()
        self.load_stack()
        self.notification_obj = Notify(default_notification_application_name="Time Stack",
                                       default_notification_icon=pathlib.Path(BASE_DIR, 'src', 'ui_files', 'icon',
                                                                             'window_icon_wob_s.png'))

    def welcome(self):
        '''
        Sends a welcome notification when the application starts.
        Also initializes the notification object.

        '''        
        self.notification_obj.application_name = "Time Stack"
        self.notification_obj.title = ""
        self.notification_obj.message = "Welcome to Time Stack"
        if self.notifications_enabled:
            self.notification_obj.audio = str(PATH_TO_SOUNDS / self.notification_sound_general)
            self.notification_obj.send()
        else:
            self.notification_obj.send()

    def notification(self, title='Your activity name', message='Good job the progress is', type_notify='general'):
        
        '''
        This function is used to send notifications to the user.

        Args:
            title (str, optional): Brief description of the notification. Defaults to 'Your activity name'.
            message (str, optional): Message to be displayed in the notification. Defaults to 'Good job the progress is'.
            type (str, optional): Type of the notification. Defaults to 'general'.
        '''
        if self.notifications_enabled:
            if type_notify == "midway":
                self.notification_obj.title = title
                self.notification_obj.message = message
                self.notification_obj.audio = str(PATH_TO_SOUNDS / self.notification_sound_midway)
            elif type_notify == "quarter":
                self.notification_obj.title = title
                self.notification_obj.message = message
                self.notification_obj.audio = str(PATH_TO_SOUNDS / self.notification_sound_quarterly)
            elif type_notify == "end":
                self.notification_obj.title = title
                self.notification_obj.message = message
                self.notification_obj.audio = str(PATH_TO_SOUNDS / self.notification_sound_end)
            elif type_notify == "general":
                self.notification_obj.title = title
                self.notification_obj.message = message
                self.notification_obj.audio = str(PATH_TO_SOUNDS / self.notification_sound_general)
            self.notification_obj.send()
            logger.info(f"{Color.CGREEN} NOTIFICATION SENT, Audio used {self.notification_obj.audio}{Color.CEND}")
        else:
            logger.info(f"{Color.CBLACK} NOTIFICATION ARE DISABLED {Color.CEND}")

    def load_stack(self):
        '''
        Loads the stack from the database.
        '''        
        connection, cursor = connect_db()
        # Fetching the data from the database
        cursor.execute(f"SELECT * FROM {MONTH_YEAR}_stack")
        result = cursor.fetchall()
        for row in result:
            if row[5] == 0:
                self.add_stack(activity_id=row[0],
                               activity_name=row[1],
                               mode=row[2],
                               start_time=row[3],
                               stop_time=row[4],
                               max_size=row[6],
                               load=True,
                               load_progress=row[7])

        disconnect_db(connection, cursor)

    def save_stack(self):
        '''
        Pauses the current stack and saves it to the database.
        ''' 
        if len(self.stack_items) > 0:
            connection, cursor = connect_db()
            counting = 0
            for index, stack_item in enumerate(self.stack_items):
                try:
                    counting += 1
                    connection.execute(f"UPDATE {MONTH_YEAR}_stack SET activity_name=?, activity_mode=?, "
                                       f"activity_start=?, activity_stop=?, activity_completed=?, "
                                       f"activity_original_size=?, activity_latest_delta=?, position=? "
                                       f"WHERE activity_id=?",
                                       (
                                           stack_item.objectName(),
                                           stack_item.activity_mode,
                                           stack_item.activity_start,
                                           stack_item.activity_stop,
                                           0,
                                           stack_item.activity_original_size,
                                           stack_item._thread.current_value,
                                           index,
                                           stack_item.activity_id,
                                       ))
                except Exception as e:
                    logger.error(f'{Color.CRED} Error while saving the stack to the database. {e}{Color.ENDC}')
            logger.info(f'{Color.CGREEN} Saved {counting} items to the database{Color.ENDC}')
            disconnect_db(connection, cursor)

    def _start_thread(self):
        '''
        Starts the thread of the top progress bar in the stack.
        '''        
        if self.stack_top_item is not None:
            if self.stack_top_item._thread.maxsize == self.stack_top_item._thread.current_value:
                logger.info(f"{Color.GREEN}Starting Thread - {self.stack_top_item.objectName()} from "
                            f"{self.stack_top_item._thread.current_value}{Color.ENDC}")
                self.stack_top_item._thread.start()
            else:
                logger.info(f"{Color.GREEN}Resuming Thread - {self.stack_top_item.objectName() } from "
                            f"{self.stack_top_item._thread.current_value}{Color.ENDC}")
                self._resume_thread()
        else:
            logger.info(f"{Color.RED}No stack bar in the stack{Color.ENDC}")
            self.warningmsg.setText("No stack bar in the stack")
            self.warningmsg.exec()

    def _resume_thread(self):
        '''
        Resumes the thread of the top progress bar in the stack.
        '''        
        if self.stack_top_item is not None:
            if not self.stack_top_item._thread.isRunning():
                self.stack_top_item._thread.start()
            self.stack_top_item._thread.resume()

    def _pause_thread(self):
        '''
        Pauses the thread of the top progress bar in the stack.
        '''        
        if self.stack_top_item is not None:
            if self.stack_top_item._thread._is_running and self.stack_top_item._thread.current_value != \
                    self.stack_top_item._thread.maxsize:
                self.stack_top_item._thread.pause(self.stack_top_item.value())
                connection, cursor = connect_db()
                connection.execute(f"UPDATE {MONTH_YEAR}_stack SET activity_latest_delta=? WHERE activity_name=?",
                                   (self.stack_top_item.value(), self.stack_top_item.objectName()))
                disconnect_db(connection, cursor)

    def add_stack(self, activity_name, mode, start_time, stop_time, max_size, load=False, load_progress=0,
                  activity_id=None):
        '''
        This function creates a new progress bar and adds it to the stack. Also the signal is connected to the remove_top_stack function.
        The progress bar is added to the layout and the stack_items list.
        The stack_top_item is set to the progress bar that is currently at the top of the stack.

        Status Codes for activity_completed:         Status Codes for Position:  
        0 - Activity incomplete                     0 - Top of the stack
        1 - Activity complete                       1-N - Nth from the top
        -1 - Removed from stack                     -1 - Removed from stack

        Args:
            name (str): The name of the progress bar.
            mode (str): Casual or Habit
            start_time (datetime.datetime): Start time of the progress bar.
            stop_time (datetime.datetime): Stop time of the progress bar.
            max_size (int):  The max size of the progress bar.
            load (bool, optional): _description_.If the progress bar is being loaded from the database.Defaults to False.
            load_progress (int, optional): _description_. The progress of the progress bar that is being loaded from the database.Defaults to 0.
        
        Returns:
            StackActivityBar (StackActivityBar): The progress bar that is currently being added to the stack.

        '''        

        connection, cursor = connect_db()
        if load:

            self.stack_bar = StackActivityBar(name=f"{activity_name}",
                                              progress_bar_size=max_size,
                                              mode=mode,
                                              activity_start=start_time,
                                              activity_stop=stop_time,
                                              set_time=load_progress,
                                              )
            logger.info(f'{Color.GREEN}Loading {activity_name} from database{Color.ENDC}')
            self.stack_bar._remove_signal.connect(self.pop_top_stack)
            self.layout.addWidget(self.stack_bar)
            if self.stack_top_item is None:
                self.stack_top_item = self.stack_bar
        else:
            activity_name = clean_input_name(activity_name)
            if activity_id is None:
                activity_id = rng_gen()
                activity_name = f'{activity_name}_{activity_id}'
                my_stack_items = cursor.execute("SELECT * FROM {}_stack WHERE activity_id=?".format(MONTH_YEAR),
                                                (activity_id,)).fetchall()
                while len(my_stack_items) != 0:
                    activity_id = rng_gen()
                    activity_name = f'{activity_name}_{activity_id}'
                    my_stack_items = cursor.execute("SELECT * FROM {}_stack WHERE activity_id=?".format(MONTH_YEAR),
                                                     (activity_id,)).fetchall()
                    if len(my_stack_items) == 0:
                        break
   
            self.stack_bar = StackActivityBar(name=f"{activity_name}", progress_bar_size=max_size, mode=mode,
                                              activity_start=start_time, activity_stop=stop_time,
                                              )
            self.stack_bar._remove_signal.connect(self.pop_top_stack)
            self.layout.addWidget(self.stack_bar)

            if self.stack_top_item is None:
                self.stack_top_item = self.stack_bar
                connection.execute(f"INSERT INTO {MONTH_YEAR}_stack VALUES (?,?,?,?,?,?,?,?,?)",
                                   (activity_id, activity_name, 'Habit', start_time, stop_time, 0, max_size, max_size, 0))
            else:
                connection.execute(f"INSERT INTO {MONTH_YEAR}_stack VALUES (?,?,?,?,?,?,?,?,?)",
                                   (activity_id, activity_name, 'Habit', start_time, stop_time, 0, max_size, max_size,
                                    len(self.stack_items)))

        self.stack_bar._thread._midway_signal.connect(
            lambda: self.notification(title=f"{self.stack_bar.name} is midway done",
                                      message=f"Great job {self.stack_bar.name} is half way done",
                                      type_notify="midway"))
        self.stack_bar._thread._quarter_signal.connect(
            lambda: self.notification(title=f"{self.stack_bar.name} is three quarters done",
                                      message=f"Almost there {self.stack_bar.name} is three quarters done",
                                      type_notify="quarter"))
        disconnect_db(connection, cursor)
        self.stack_items.append(self.stack_bar)
        return self.stack_bar

    def pop_top_stack(self):
        '''
        Removes the top progress bar in the stack.
        The progress bar is removed from the layout and the stack_items list.
        The stack_top_item is set to the progress bar that is currently at the top of the stack.
        '''
     
        if self.stack_top_item is not None:
            logger.info(f"{Color.RED}Removing Thread - {self.stack_top_item.objectName()}{Color.ENDC}")
            connection, cursor = connect_db()
            if self.stack_top_item._thread.current_value != 0:
                connection.execute(f"UPDATE {MONTH_YEAR}_stack SET activity_completed=?, position=? WHERE activity_name=?",
                                   (-1, -1, self.stack_top_item.objectName()))
                logger.info(f"{Color.YELLOW}Activity forcefully removed - {self.stack_top_item.objectName()}{Color.ENDC}")
            elif self.stack_top_item._thread.current_value == 0:
                connection.execute(f"UPDATE {MONTH_YEAR}_stack SET activity_completed=?, activity_latest_delta=?, "
                                   f"position=? WHERE activity_name=?",
                                   (1, 0, -1, self.stack_top_item.objectName()))
                logger.info(f"{Color.YELLOW}Activity completed - {self.stack_top_item.objectName()}{Color.ENDC}")
                self.notification(title=f"{self.stack_top_item.name} is completed",
                                  message=f"Nice Job!! {self.stack_top_item.name} is completed",
                                  type_notify="end")

            self.layout.removeWidget(self.stack_top_item)
            self.stack_top_item.deleteLater()
            self.stack_top_item._thread.terminate()
            self.stack_items.remove(self.stack_top_item)
            if len(self.stack_items) > 0:
                self.stack_top_item = self.stack_items[0]
            else:
                self.stack_top_item = None

            for index, stack_item in enumerate(self.stack_items):
                connection.execute(f"UPDATE {MONTH_YEAR}_stack SET position=? WHERE activity_name=?",
                                   (index, stack_item.objectName()))
            disconnect_db(connection, cursor)
        else:
            self.warningmsg.setText("No stack bar in the stack")
            self.warningmsg.exec()

    def update_top_item(self, new_order):
        '''
        This function is used to update the top item in the stack.
        The stack_top_item is set to the progress bar that is currently at the top of the stack.

        Args:
            new_order (int): The new order of the progress bar that is currently at the top of the stack.

        '''
        if self.stack_items is not None:
            if self.stack_top_item._thread._is_running and self.stack_top_item._thread.current_value != \
                    self.stack_top_item._thread.maxsize:
                self.stack_top_item._thread.pause(self.stack_top_item.value())
            self.stack_items = new_order
            self.stack_top_item = self.stack_items[0]
            logger.info(f"{Color.GREEN}Updating Stack Top Item - {self.stack_top_item.objectName()}{Color.ENDC}")
            connection, cursor = connect_db()
            for index, stack_item in enumerate(self.stack_items):
                connection.execute(f"UPDATE {MONTH_YEAR}_stack SET position=? WHERE activity_name=?",
                                   (index, stack_item.objectName()))
            disconnect_db(connection, cursor)

            
    def printer(self):
        '''
        This function is used for debugging purposes.
        Logs the name of the progress bar that is currently at the top of the stack.
        Logs the names of all the progress bars in the stack.

        '''
        if len(self.stack_items) > 0:
            logger.info(f"{Color.RED}------------------{Color.ENDC}")
            logger.info(f"{Color.RED}------------------{Color.ENDC}")
            logger.info(f"\t {Color.HEADER}Stack STATS{Color.ENDC}")
            logger.info(f"{Color.CBLUE} Stack size - {len(self.stack_items)}{Color.ENDC}")
            logger.info(f"{Color.GREEN} Stack top item - {self.stack_top_item.objectName()}{Color.ENDC}")
            logger.info(f"{Color.RED} Stack ITMES{Color.ENDC}")
            logger.info(f"{Color.RED}------------------{Color.ENDC}")

            for item in self.stack_items:

                logger.info(f"{Color.CBLUE} Item - {item.objectName()}{Color.ENDC}")
                logger.info(f"{Color.CBLUE} Thread - {item._thread.objectName()}{Color.ENDC}")
                logger.info(f"{Color.CBLUE} Thread maxsize - {item._thread.maxsize}{Color.ENDC}")
                logger.info(f"{Color.CBLUE} Thread value - {item._thread.current_value}{Color.ENDC}")
                logger.info(f"{Color.CBLUE} Thread is running - {item._thread.isRunning()}{Color.ENDC}")
                logger.info(f"{Color.GREEN} ------------------------{Color.ENDC}")

class Stack(QWidget):
    def __init__(self, parent=None):
        '''
        :warning: This class is temporary and will be removed in the future. Only used for testing purposes.
        Initializes the Stack class and sets up the UI.
        Temporary class for testing the StackManager class.

        Args:
            parent (optional):  Defaults to None.
        '''
        super().__init__(parent)
        
        self.stack_ui = QVBoxLayout()   
        # Buttons
        self.add_btn = QPushButton("Add")
        self.start_btn = QPushButton("Start")
        self.pause_btn = QPushButton("Pause")
        self.remove_btn = QPushButton("Remove")
        self.debug_btn = QPushButton("Debug")


        # Add buttons to the layout
        self.stack_ui.addWidget(self.remove_btn)
        self.stack_ui.addWidget(self.add_btn)
        self.stack_ui.addWidget(self.start_btn)
        self.stack_ui.addWidget(self.pause_btn)
        self.stack_ui.addWidget(self.debug_btn)

        # Create a DragWidget instance
        self.drag_widget = DragWidget()

        # Scroll Area
        self.scroll_area = DragScrollArea()
        self.scroll_area.setObjectName("scroll_area_main")
        self.scroll_area.setFixedSize(700, 900)
        self.scroll_area.setWidgetResizable(True)
        self.scroll_area.setWidget(self.drag_widget)

        # Add the Scroll Area to the layout
        self.stack_ui.addWidget(self.scroll_area)
        self.stack_ui.addStretch()
        self.setLayout(self.stack_ui)

        # Pass the layout of the DragWidget to the StackManager class
        self.manager = StackManager(self.drag_widget.layout())

        self.progress_end = 30
                # Connect button signals to respective slots
        self.add_btn.clicked.connect(self.add_stack_bar)
        self.remove_btn.clicked.connect(self.pop_top_active)
        self.start_btn.clicked.connect(self.start_thread)
        self.debug_btn.clicked.connect(self.manager.printer)
        self.pause_btn.clicked.connect(self.manager._pause_thread)

       
        self.progress_end = 30

    def add_stack_bar(self):
        '''
        Adds a progress bar to the stack.
        '''
        rand = random.randint(1, 100)
        start_time = datetime.datetime.now()
        end_time = start_time + datetime.timedelta(seconds=self.progress_end)
        self.manager.add_stack(f"mygenerictask_{rand}", mode="Habit",start_time=start_time, stop_time=end_time, max_size=self.progress_end)

    def start_thread(self):
        '''
        Starts the thread of the top progress bar in the stack.
        '''
        self.manager._start_thread()

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
