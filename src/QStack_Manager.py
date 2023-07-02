import re
import sys
import datetime
import sqlite3
import random

from PyQt6.QtWidgets import QMessageBox, QWidget, QPushButton, QLayout, QVBoxLayout,QApplication
from PyQt6.QtCore import Qt
from os import path
from os import makedirs

from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from libs.color import Color

from libs.QClasses.QDragWidget import DragWidget
from libs.QClasses.QProgressBar import StackActivityBar
from libs.QClasses.QScrollArea import DragScrollArea

MONTH_YEAR = datetime.datetime.now().strftime("%B_%Y")
SAVE_FILE=path.join(BASE_DIR, 'user_data','db', f'{MONTH_YEAR}_stack.db')
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
        self.warningmsg = QMessageBox()
        self.warningmsg.setIcon(QMessageBox.Icon.Warning)
        self.warningmsg.setWindowTitle("Error")
        self.warningmsg.setStandardButtons(QMessageBox.StandardButton.Close)
        # Align items to horizontal center
        self.layout.setAlignment(Qt.AlignmentFlag.AlignHCenter)
        self.check_db()
        self.load_stack()
   
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
            if row[4]==0:
                insert_map[row[7]]={'name':row[0],'start_time':row[2],'stop_time':row[3],'activity_latest_delta':row[6],'activity_original_size':row[5]}
        if len(insert_map)>0:
            for i in range(0,len(insert_map)):
                self.add_stack(name=insert_map[i]['name'],start_time=insert_map[i]['start_time'],stop_time=insert_map[i]['stop_time'],max_size=insert_map[i]['activity_original_size'],load=True,load_progress=insert_map[i]['activity_latest_delta'])
        self.disconnect_db(connection,cursor)

    def generate_random_number(self)->int:
        '''
        This function generates a random number between 1 and 10000.

        Returns:
            int:A random number between 1 and 10000. 
        '''        
        rand_no = random.randint(1, 10000)
        return rand_no

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
        '''        
        logger.info(f'{Color.GREEN}Checking if {SAVE_FILE} exists{Color.ENDC}')
        if not path.exists(SAVE_FILE):
            logger.info(f'{Color.RED}File {SAVE_FILE} does not exist{Color.ENDC}')
            logger.info(f'{Color.GREEN}Creating file {SAVE_FILE}{Color.ENDC}')
            makedirs(path.join(BASE_DIR, 'user_data','db'), exist_ok=True)
            connection,cursor=self.connect_db()
            cursor.execute(f'''CREATE TABLE {MONTH_YEAR}_stack
                            (activity_name TEXT PRIMARY KEY NOT NULL,
                            activity_mode TEXT NOT NULL,
                            activity_start DATETIME  NOT NULL,
                            activity_stop DATETIME  NOT NULL,
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
        # Add a check to see if stack value is equal to max value if so then start else call resume
        if self.stack_top_item is not None :       
            if self.stack_top_item._thread.maxsize==self.stack_top_item._thread.current_value:
                logger.info(f"{Color.GREEN}Starting Thread - {self.stack_top_item.objectName()}{Color.ENDC}")
                self.stack_top_item._thread.start()
            else:
                logger.info(f"{Color.GREEN}Resuming Thread - {self.stack_top_item.objectName()}{Color.ENDC}")
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
            self.stack_top_item._thread.resume()

    def _pause_thread(self) -> None:
        if self.stack_top_item is not None:
            if self.stack_top_item._thread._is_running and self.stack_top_item._thread.current_value!=self.stack_top_item._thread.maxsize:
                self.stack_top_item._thread.pause(self.stack_top_item.value())
                connection,cursor=self.connect_db()
                connection.execute(f"UPDATE {MONTH_YEAR}_stack SET activity_latest_delta=? WHERE activity_name=?",
                   (self.stack_top_item.value(), self.stack_top_item.objectName()))   
                self.disconnect_db(connection,cursor)
                   
    def add_stack(self, name: str,start_time:datetime.datetime,stop_time:datetime.datetime, max_size: int,load:bool=False,load_progress:int=0) -> StackActivityBar:
        '''
        This function creates a new progress bar and adds it to the stack. Also the signal is connected to the remove_top_stack function.
        The progress bar is added to the layout and the stack_items list.
        The stack_top_item is set to the progress bar that is currently at the top of the stack.

        Args:
            name (str): The name of the progress bar.
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
            self.stack_bar = StackActivityBar(name=f"{name}", progress_bar_size=max_size,set_time=load_progress)
            logger.info(f'{Color.GREEN}Loading {name} from database{Color.ENDC}')
            self.stack_bar._remove_signal.connect(self.pop_top_stack)
            self.layout.addWidget(self.stack_bar)
            if self.stack_top_item is None:
                self.stack_top_item = self.stack_bar
            
        else:
            name=self.clean_input_name(name)
            name=f'{name}_{self.generate_random_number()}'
            my_stack_items=cursor.execute("SELECT * FROM {}_stack WHERE activity_name=?".format(MONTH_YEAR), (name,)).fetchall()
            while len(my_stack_items)!=0:
                name=f'{name}_{self.generate_random_number()}'
                my_stack_items=cursor.execute("SELECT * FROM {}_stack WHERE activity_name=?".format(MONTH_YEAR), (name,)).fetchall()
                if len(my_stack_items)==0:
                    break
            self.stack_bar = StackActivityBar(f"{name}", max_size)
            self.stack_bar._remove_signal.connect(self.pop_top_stack)
            self.layout.addWidget(self.stack_bar)
            if self.stack_top_item is None:
                self.stack_top_item = self.stack_bar
                connection.execute(f"INSERT INTO {MONTH_YEAR}_stack VALUES (?,?,?,?,?,?,?,?)",
                        (name, 'casual', start_time, stop_time, 0, max_size,max_size,0))
            else:
               connection.execute(f"INSERT INTO {MONTH_YEAR}_stack VALUES (?,?,?,?,?,?,?,?)",
                        (name, 'casual', start_time, stop_time, 0, max_size,max_size,len(self.stack_items)))
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
            logger.info(f"\t {Color.HEADER}\nStack STATS\n{Color.ENDC}")
            logger.info(f"{Color.BLUE} Stack size - {len(self.stack_items)}{Color.ENDC}")
            logger.info(f"{Color.GREEN} Stack top item - {self.stack_top_item.objectName()}{Color.ENDC}")
            logger.info(f"{Color.RED} Stack ITMES{Color.ENDC}")
            logger.info(f"{Color.RED}------------------{Color.ENDC}")

            for item in self.stack_items:

                logger.info(f"{Color.BLUE} Item - {item.objectName()}{Color.ENDC}")
                logger.info(f"{Color.BLUE} Thread - {item._thread.objectName()}{Color.ENDC}")
                logger.info(f"{Color.BLUE} Thread maxsize - {item._thread.maxsize}{Color.ENDC}")
                logger.info(f"{Color.BLUE} Thread value - {item._thread.current_value}{Color.ENDC}")
                logger.info(f"{Color.BLUE} Thread is running - {item._thread.isRunning()}{Color.ENDC}")
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
        self.manager.add_stack(f"mygenerictask_{rand}", start_time=start_time, stop_time=end_time, max_size=self.progress_end)

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
