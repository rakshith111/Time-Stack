import random
import sys

from PyQt6.QtWidgets import *

from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from libs.color import Color

from libs.QClasses.QDragWidget import DragWidget
from libs.QClasses.QProgressBar import StackActivityBar
from libs.QClasses.QScrollArea import DragScrollArea
from PyQt6.QtCore import Qt

class StackManager():
    '''
    StackManager class manages the stack of progress bars.
    Currently supoorted functions for the STACK:
        - Add a progress bar to the stack.
        - Start the thread of the top progress bar in the stack.
        - Pause the thread of the top progress bar in the stack.
        - Remove the top progress bar in the stack.
    - `print_stack()` is used for debugging purposes.

    '''

    def __init__(self, layout: QLayout) -> None:
        '''
        The layout is set to the layout that is passed to the class. i.e Stack Space layout.
        stack_bar: The progress bar that is currently being added to the stack.
        stack_top_item: The progress bar that is currently at the top of the stack.
        stack_items: A list of all the progress bars in the stack.


        Args:
            layout (QLayout): The layout that the progress bars will be added to.
        '''
        self.layout = layout
        self.stack_top_item = None
        self.stack_items = []
        self.warningmsg = QMessageBox()
        self.warningmsg.setIcon(QMessageBox.Icon.Warning)
        self.warningmsg.setWindowTitle("Error")
        self.warningmsg.setStandardButtons(QMessageBox.StandardButton.Close)
        # Align items to horizontal center
        self.layout.setAlignment(Qt.AlignmentFlag.AlignHCenter)

    def add_stack(self, name: str, progress_end: int) -> StackActivityBar:
        '''
        This function creates a new progress bar and adds it to the stack. Also the signal is connected to the remove_top_stack function.
        The progress bar is added to the layout and the stack_items list.
        The stack_top_item is set to the progress bar that is currently at the top of the stack.

        Args:
            name (str): The name of the progress bar.
            progress_end (int):  The max size of the progress bar.

        Returns:
            StackActivityBar (StackActivityBar): The progress bar that is currently being added to the stack.

        '''
        self.stack_bar = StackActivityBar(f"{name}", progress_end)
        self.stack_bar._remove_signal.connect(self.pop_top_stack)
        self.layout.addWidget(self.stack_bar)
        if self.stack_top_item is None:
            self.stack_top_item = self.stack_bar
        self.stack_items.append(self.stack_bar)
        return self.stack_bar

    def start_thread(self) -> None:
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
                self.resume_thread()
        else :
            logger.info(f"{Color.RED}No stack bar in the stack{Color.ENDC}")
            self.warningmsg.setText("No stack bar in the stack")
            self.warningmsg.exec()
    def resume_thread(self) -> None:
        '''
        Resumes the thread of the top progress bar in the stack.
        '''
        if self.stack_top_item is not None:
            logger.info(f"{Color.GREEN}Resuming Thread - {self.stack_top_item.objectName()}{Color.ENDC}")
            self.stack_top_item._thread.resume()

    def pause_thread(self) -> None:
        if self.stack_top_item is not None:
            if self.stack_top_item._thread._is_running and self.stack_top_item._thread.current_value!=self.stack_top_item._thread.maxsize:
                logger.info(f"{Color.RED}Pausing Thread - {self.stack_top_item.objectName()}{Color.ENDC}")
                self.stack_top_item._thread.pause(self.stack_top_item.value())
    
    def pop_top_stack(self) -> None:
        '''
        Removes the top progress bar in the stack.
        The progress bar is removed from the layout and the stack_items list.
        The stack_top_item is set to the progress bar that is currently at the top of the stack.
        '''
        if self.stack_top_item is not None:
            self.layout.removeWidget(self.stack_top_item)
            self.stack_top_item.deleteLater()
            self.stack_top_item._thread.terminate()
            self.stack_items.remove(self.stack_top_item)
            if len(self.stack_items) > 0:
                self.stack_top_item = self.stack_items[0]
            else:
                self.stack_top_item = None
        else:
            self.warningmsg.setText("No stack bar in the stack")
            self.warningmsg.exec()
            

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
        self.pause_btn.clicked.connect(self.manager.pause_thread)

       
        self.progress_end = 30

    def add_stack_bar(self):
        '''
        Adds a progress bar to the stack.
        '''
        rand = random.randint(1, 100)
        self.manager.add_stack(f"mygenerictask_{rand}", self.progress_end)

    def start_thread(self):
        '''
        Starts the thread of the top progress bar in the stack.
        '''
        self.manager.start_thread()

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
