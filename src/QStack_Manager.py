import re
import sys
import random
import pathlib
import sqlite3
import datetime

from PyQt6.QtCore import Qt, QStandardPaths

from notifypy import Notify

from PyQt6.QtWidgets import QApplication
from PyQt6.QtWidgets import QMessageBox, QWidget, QPushButton, QLayout, QVBoxLayout,QApplication

from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from libs.color import Color

from libs.QClasses.QDragWidget import DragWidget
from libs.QClasses.QProgressBar import StackActivityBar
from libs.QClasses.QScrollArea import DragScrollArea

MONTH_YEAR = datetime.datetime.now().strftime("%B_%Y")

SAVE_FILE=pathlib.Path(QStandardPaths.writableLocation(QStandardPaths.StandardLocation.AppLocalDataLocation),'TIME_STACK','user_data','db', f'{MONTH_YEAR}_stack.db')
PATH_TO_SOUNDS = pathlib.Path(BASE_DIR, 'src', 'ui_files', 'sounds')

class StackManager():

    def __init__(self, layout: QLayout) -> None:
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
        # Define the top item of the stack by enforcing type
        self.stack_top_item = None
        self.stack_items = []
        self.notifications_enabled = True
        self.notification_sound_general=""
        self.notification_sound_midway=""
        self.notification_sound_quarterly=""
        self.notification_sound_end=""
        self.warningmsg = QMessageBox()
        self.warningmsg.setIcon(QMessageBox.Icon.Warning)
        self.warningmsg.setWindowTitle("Error")
        self.warningmsg.setStandardButtons(QMessageBox.StandardButton.Close)
        # Align items to horizontal center
        self.layout.setAlignment(Qt.AlignmentFlag.AlignHCenter)
        self.check_db()
        self.load_stack()
        self.notification_obj = Notify(default_notification_application_name = "Time Stack",
                                       default_notification_icon=pathlib.Path(BASE_DIR,'src', 'ui_files', 'icon', 'window_icon_wob_s.png'))
             
        
    def welcome(self)->None:
        if self.notifications_enabled:
            self.notification_obj.application_name = "Time Stack"
            self.notification_obj.title = ""
            self.notification_obj.message="Welcome to Time Stack"

            self.notification_obj.audio =str(PATH_TO_SOUNDS / self.notification_sound_general)
            self.notification_obj.send()         
        else:
            self.notification_obj.application_name = "Time Stack"
            self.notification_obj.title = ""
            self.notification_obj.message="Welcome to Time Stack"
            self.notification_obj.send()
        
    def notification(self, title: str='Your activity name', message: str='Good job the progress is',type_notify:str='general') -> None:
        '''
        This function is used to send notifications to the user.

        Args:
            title (str, optional): Brief description of the notification. Defaults to 'Your activity name'.
            message (str, optional): Message to be displayed in the notification. Defaults to 'Good job the progress is'.
            type (str, optional): Type of the notification. Defaults to 'general'.
        '''
   
        if self.notifications_enabled:
            if type_notify=="midway":
                self.notification_obj.title = title
                self.notification_obj.message = message
                self.notification_obj.audio =str(PATH_TO_SOUNDS / self.notification_sound_midway)
            elif type_notify=="quarter":
                self.notification_obj.title = title
                self.notification_obj.message = message
                self.notification_obj.audio = str(PATH_TO_SOUNDS / self.notification_sound_quarterly)
            elif type_notify=="end":
                self.notification_obj.title = title
                self.notification_obj.message = message
                self.notification_obj.audio = str(PATH_TO_SOUNDS / self.notification_sound_end)
            elif type_notify=="general":
                self.notification_obj.title = title
                self.notification_obj.message = message
                self.notification_obj.audio = str(PATH_TO_SOUNDS / self.notification_sound_general)
            self.notification_obj.send()
            logger.info(f"{Color.CGREEN} NOTIFICATION SENT, Audio used {self.notification_obj.audio}{Color.CEND}")
      
            
        else:
            logger.info(f"{Color.CBLACK} NOTIFICATION ARE DISABLED {Color.CEND}")

    def load_stack(self)->None:
        '''
        Loads the stack from the database.
        '''
        connection,cursor=self.connect_db()
       
        # Fetching the data from the database
        cursor.execute(f"SELECT * FROM {MONTH_YEAR}_stack")
        result = cursor.fetchall()
        insert_map = dict()
        for row in result:  
            if row[5]==0:
                insert_map[row[8]]={'name':row[1],
                                    'activity_mode':row[2],
                                    'start_time':row[3],
                                    'stop_time':row[4],
                                    'activity_original_size':row[6],
                                    'activity_latest_delta':row[7]
                                    }
        if len(insert_map)>0:
            for i in range(0,len(insert_map)):
         
                self.add_stack(activity_name=insert_map[i]['name'],
                               mode=insert_map[i]['activity_mode'],
                               start_time=insert_map[i]['start_time'],
                               stop_time=insert_map[i]['stop_time'],
                               max_size=insert_map[i]['activity_original_size'],
                               load=True,
                               load_progress=insert_map[i]['activity_latest_delta'])
        
        self.disconnect_db(connection,cursor)

    def generate_random_number(self)->int:
        '''
        This function generates a random number between 1 and 10000.

        Returns:
            int:A random number between 1 and 10000. 
        '''        
        rand_no = random.randint(1, 10000)
        return rand_no

    def save_stack(self)->None:
        '''
        Pauses the current stack and saves it to the database.
        ''' 
        if len(self.stack_items)>0:       
            connection,cursor=self.connect_db()
            if self.stack_top_item is not None and self.stack_top_item._thread._is_running:
                self.stack_top_item._thread.pause(self.stack_top_item.value())   
            counting=0  
            for i in range(0,len(self.stack_items)):
                try:
                    counting+=1
                    cursor.execute(f"UPDATE {MONTH_YEAR}_stack  SET activity_name=?, activity_mode=?, activity_start=?, activity_stop=?, activity_completed=?, activity_original_size=?,activity_latest_delta=?,position=? WHERE activity_id=?",
                                    (
                                    self.stack_items[i].objectName(),
                                    self.stack_items[i].activity_mode,
                                    self.stack_items[i].activity_start,
                                    self.stack_items[i].activity_stop,
                                    0,
                                    self.stack_items[i].activity_original_size,
                                    self.stack_items[i]._thread.current_value,
                                    i,self.stack_items[i].activity_id,
                                    ))
                except Exception as e:
                    logger.error(f'{Color.CRED} Error while saving the stack to the database. {e}{Color.ENDC}')
            logger.info(f'{Color.CGREEN} Saved {counting} items to the database{Color.ENDC}')
            self.disconnect_db(connection,cursor)

    def clean_input_name(self,name:str)->str:
        '''
        A Clean function to clean the name of the activity.

        Args:
            name (str): A string that is to be cleaned.

        Returns:
            str: A cleaned string.
        ''' 
        cleaned_name = name.strip()
        cleaned_name = re.sub(r'[^a-zA-Z0-9_]', '_', cleaned_name)
        # Limit the length of the name to 50 characters
        cleaned_name = cleaned_name[:50]
        return cleaned_name
    
    def connect_db(self)->tuple:
        '''
        This function connects to the database and returns the connection and cursor objects.

        Returns:
            tuple: (connection, cursor)
        '''              
        connection = sqlite3.connect(SAVE_FILE)
        cursor = connection.cursor()
        return connection, cursor
    
    def disconnect_db(self,connection:sqlite3.Connection,cursor:sqlite3.Cursor)->None:
        '''
        This function disconnects from the database.

        Args:
            connection (sqlite3.Connection): A connection object to the database.
            cursor (sqlite3.Cursor): A cursor object to the database.
        '''        
        connection.commit()
        connection.close()         

    def check_db(self)->None:
        '''
        This function checks if the database file exists. If it does not exist, it creates a new database file.
            # 0 - activity_id , 1 - activity_name , 2 - activity_mode ,
            # 3 - activity_start , 4 - activity_stop , 5 - activity_completed ,
            # 6 - activity_original_size , 7 - activity_latest_delta , 8 - position
        '''        
        logger.info(f'{Color.GREEN}Checking if {SAVE_FILE} exists{Color.ENDC}')
        if not pathlib.Path(SAVE_FILE).exists():
            logger.info(f'{Color.RED}File {SAVE_FILE} does not exist{Color.ENDC}')
            logger.info(f'{Color.GREEN}Creating file {SAVE_FILE}{Color.ENDC}')

            pathlib.Path(SAVE_FILE).parent.mkdir(parents=True, exist_ok=True)
            connection,cursor=self.connect_db()
            cursor.execute(f'''CREATE TABLE {MONTH_YEAR}_stack
                            ( activity_id INTEGER PRIMARY KEY , 
                            activity_name TEXT NOT NULL,
                            activity_mode TEXT NOT NULL,
                            activity_start DATETIME NOT NULL,
                            activity_stop DATETIME NOT NULL,
                            activity_completed INT NOT NULL,
                            activity_original_size INT NOT NULL,
                            activity_latest_delta INT NOT NULL,
                            position INT NOT NULL );''')

            self.disconnect_db(connection,cursor)
        else:
            logger.info(f'{Color.GREEN}File {SAVE_FILE} exists{Color.ENDC}')

    def _start_thread(self) -> None:
        '''
        Starts the thread of the top progress bar in the stack.
        '''

        if self.stack_top_item is not None :       
            if self.stack_top_item._thread.maxsize==self.stack_top_item._thread.current_value:
                logger.info(f"{Color.GREEN}Starting Thread - {self.stack_top_item.objectName()} from {self.stack_top_item._thread.current_value}{Color.ENDC}")
                self.stack_top_item._thread.start()
            else:
                logger.info(f"{Color.GREEN}Resuming Thread - {self.stack_top_item.objectName() } from {self.stack_top_item._thread.current_value}{Color.ENDC}")
                self._resume_thread()
        else :
            logger.info(f"{Color.RED}No stack bar in the stack{Color.ENDC}")
            self.warningmsg.setText("No stack bar in the stack")
            self.warningmsg.exec()

    def _resume_thread(self) -> None:
        '''
        Resumes the thread of the top progress bar in the stack.
        '''
        if self.stack_top_item is not None:
            if not self.stack_top_item._thread.isRunning():
                self.stack_top_item._thread.start() # This is to start the thread if it is not running        
            self.stack_top_item._thread.resume()
            

    def _pause_thread(self) -> None:
        if self.stack_top_item is not None:
            if self.stack_top_item._thread._is_running and self.stack_top_item._thread.current_value!=self.stack_top_item._thread.maxsize:
                self.stack_top_item._thread.pause(self.stack_top_item.value())
                connection,cursor=self.connect_db()
                connection.execute(f"UPDATE {MONTH_YEAR}_stack SET activity_latest_delta=? WHERE activity_name=?",
                   (self.stack_top_item.value(), self.stack_top_item.objectName()))   
                self.disconnect_db(connection,cursor)
                   
    def add_stack(self, activity_name: str,mode:str,start_time:datetime.datetime,stop_time:datetime.datetime, max_size: int,load:bool=False,load_progress:int=0) -> StackActivityBar:
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
 
        connection,cursor=self.connect_db()
        if load:
            self.stack_bar = StackActivityBar(name=f"{activity_name}",
                                              progress_bar_size=max_size,
                                              mode=mode,                                              
                                              activity_start=start_time,
                                              activity_stop=stop_time,
                                              set_time=load_progress)
            
            logger.info(f'{Color.GREEN}Loading {activity_name} from database{Color.ENDC}')
            self.stack_bar._remove_signal.connect(self.pop_top_stack)
            self.layout.addWidget(self.stack_bar)
            if self.stack_top_item is None:
                self.stack_top_item = self.stack_bar
        else:
            activity_name=self.clean_input_name(activity_name)
            activity_id=self.generate_random_number()
            activity_name=f'{activity_name}_{activity_id}'
            my_stack_items=cursor.execute("SELECT * FROM {}_stack WHERE activity_id=?".format(MONTH_YEAR), (activity_id,)).fetchall()
            # If the activity_id is already present in the database, then generate a new activity_id
            while len(my_stack_items)!=0:
                activity_id=self.generate_random_number()   
                activity_name=f'{activity_name}_{activity_id}'
                my_stack_items=cursor.execute("SELECT * FROM {}_stack WHERE activity_id=?".format(MONTH_YEAR), (activity_id,)).fetchall()
                if len(my_stack_items)==0:
                    break
            self.stack_bar = StackActivityBar(name=f"{activity_name}", progress_bar_size=max_size,mode=mode,activity_start=start_time,activity_stop=stop_time)
            self.stack_bar._remove_signal.connect(self.pop_top_stack)
            self.layout.addWidget(self.stack_bar)
    
            # self.notification(title="Stack Generator",message=f'Stack {self.stack_bar.objectName()} created successfully',type_notify="general")
            if self.stack_top_item is None:
                self.stack_top_item = self.stack_bar
                connection.execute(f"INSERT INTO {MONTH_YEAR}_stack VALUES (?,?,?,?,?,?,?,?,?)",
                        (activity_id,activity_name, 'Casual', start_time, stop_time, 0, max_size,max_size,0))
            else:
               connection.execute(f"INSERT INTO {MONTH_YEAR}_stack VALUES (?,?,?,?,?,?,?,?,?)",
                        (activity_id,activity_name, 'Casual', start_time, stop_time, 0, max_size,max_size,len(self.stack_items)))
    
        self.stack_bar._thread._midway_signal.connect(lambda: self.notification(title=f"{self.stack_bar.name} is midway done",message=f"Great job {self.stack_bar.name} is half way done",type_notify="midway"))
        self.stack_bar._thread._quarter_signal.connect(lambda: self.notification(title=f"{self.stack_bar.name} is three quarters done",message=f"Almost there {self.stack_bar.name} is three quarters done",type_notify="quarter"))
        self.disconnect_db(connection,cursor)
        self.stack_items.append(self.stack_bar)
        return self.stack_bar

    def pop_top_stack(self) -> None:
        '''
        Removes the top progress bar in the stack.
        The progress bar is removed from the layout and the stack_items list.
        The stack_top_item is set to the progress bar that is currently at the top of the stack.
        '''
     
        if self.stack_top_item is not None:
            logger.info(f"{Color.RED}Removing Thread - {self.stack_top_item.objectName()}{Color.ENDC}")
            connection,cursor=self.connect_db()
            # If the activity is not completed then set the activity completed to -1
            if self.stack_top_item._thread.current_value!=0:
                connection.execute(f"UPDATE {MONTH_YEAR}_stack SET activity_completed=?,position=? WHERE activity_name=?",
                     (-1,-1, self.stack_top_item.objectName()))
                logger.info(f"{Color.YELLOW}Activity forcefully removed - {self.stack_top_item.objectName()}{Color.ENDC}")
            # If the activity is completed then set the activity completed to 1
            elif self.stack_top_item._thread.current_value==0:
                connection.execute(f"UPDATE {MONTH_YEAR}_stack SET activity_completed=?,activity_latest_delta=?,position=? WHERE activity_name=?",
                     (1,0,-1,self.stack_top_item.objectName()))
                logger.info(f"{Color.YELLOW}Activity completed - {self.stack_top_item.objectName()}{Color.ENDC}")
                self.notification(title=f"{self.stack_top_item.name} is completed",message=f"Nice Job!! {self.stack_top_item.name} is completed",type_notify="end")

            self.layout.removeWidget(self.stack_top_item)
            self.stack_top_item.deleteLater()
            self.stack_top_item._thread.terminate()
            self.stack_items.remove(self.stack_top_item)
            if len(self.stack_items) > 0:
                self.stack_top_item = self.stack_items[0]
            else:
                self.stack_top_item = None
            
            for index,stack_item in enumerate(self.stack_items):
                connection.execute(f"UPDATE {MONTH_YEAR}_stack SET position=? WHERE activity_name=?",
                     (index, stack_item.objectName()))
            self.disconnect_db(connection,cursor)
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
            if self.stack_top_item._thread._is_running and self.stack_top_item._thread.current_value!=self.stack_top_item._thread.maxsize:
                self.stack_top_item._thread.pause(self.stack_top_item.value())
            self.stack_items=new_order
            self.stack_top_item = self.stack_items[0]
            logger.info(f"{Color.GREEN}Updating Stack Top Item - {self.stack_top_item.objectName()}{Color.ENDC}")
            connection,cursor=self.connect_db()
            # Update the position of the stack items in the database
            for index,stack_item in enumerate(self.stack_items):
                connection.execute(f"UPDATE {MONTH_YEAR}_stack SET position=? WHERE activity_name=?",
                     (index, stack_item.objectName()))
            self.disconnect_db(connection,cursor)
            
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
        self.manager.add_stack(f"mygenerictask_{rand}", mode="casual",start_time=start_time, stop_time=end_time, max_size=self.progress_end)

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
