import sys
import re
import datetime
import pathlib
from random import randint

from PyQt6 import QtCore
from PyQt6.QtWidgets import QProgressBar,QMenu
from PyQt6.QtGui import QDrag, QPixmap,QAction 
from PyQt6.QtCore import Qt, QMimeData, pyqtSignal,QEvent

sys.path.append(pathlib.Path(__file__).parent.parent.parent.absolute())

from libs._base_logger import logger,BASE_DIR
from libs.color import Color

try:
    from .QThread import TimerThread
except ImportError:
    from QThread import TimerThread

QSS_FILE = open(pathlib.Path(BASE_DIR, 'src', 'ui_files', 'stack.qss'), 'r').read()
PAT_BG=r'stop: 0.0000 #e3938c,\nstop: 0.3333 #714946,\nstop: 0.6667 #714946,\nstop: 1.0000 #e3938c'
PAT_STACK=r' stop: 0.0000 #e7b5e7,\nstop: 0.3333 #735a73,\nstop: 0.6667 #735a73,\nstop: 1.0000 #e7b5e7'
STEPS = 4

def generate_random_color()->tuple:
    '''
    Generates a random RGB color (brighter colors)

    Returns:
        tuple: A tuple of RGB values.
    '''    
    r = randint(100, 255)
    g = randint(100, 255)
    b = randint(100, 255)
    return r, g, b

def generate_gradient_hex(color:tuple, steps:int)->list:
    '''
    Generates a gradient of colors from the given color.

    Args:
        color (tuple): A tuple of RGB values.
        steps (int): The number of colors to generate.

    Returns:
        list: A list of hex color strings.
    '''    

    # Calculate the middle color by averaging the start and end colors
    middle_color = tuple(c // 2 for c in color)
    # Initialize the list to hold gradient colors
    gradient_colors = []
    
    # Generate steps/2 colors towards the middle
    for i in range(steps // 2):
        ratio = i / ((steps // 2) - 1)
        grad_color = tuple(int(color[j] * (1 - ratio) + middle_color[j] * ratio) for j in range(3))
        gradient_colors.append(grad_color)
    
    # Generate steps/2 colors away from the middle
    for i in range(steps // 2):
        ratio = i / ((steps // 2) - 1)
        grad_color = tuple(int(middle_color[j] * (1 - ratio) + color[j] * ratio) for j in range(3))
        gradient_colors.append(grad_color)
    
    # Convert RGB tuples to hex color strings
    gradient_hex = ['#' + ''.join(f'{c:02x}' for c in color) for color in gradient_colors]
    
    return gradient_hex

def string_content(colors:list)->str:
    '''
    Makes a string of the colors for the QSS content.
    Args:
        colors (list): A list of hex color strings. 

    Returns:
        str: A string of the colors for the QSS content.
    '''    
    text_colors=''
    for i, color in enumerate(colors):
        text_colors+=f"stop: {i / (len(colors) - 1):.4f} {color},\n"
    return text_colors[:-2]

def randomize_progress_bar_colors(qss_content: str) -> str:
    '''
    Randomizes the colors in the QSS content.

    Args:
        qss_content (str): The QSS content.

    Returns:
        str: Altered QSS content.
    '''    
    progress_bar_colors=generate_gradient_hex(generate_random_color(), STEPS)
    background_color=generate_gradient_hex(generate_random_color(), STEPS)
    qss_content=re.sub(PAT_BG,string_content(background_color),qss_content)
    qss_content=re.sub(PAT_STACK,string_content(progress_bar_colors),qss_content)
    return qss_content

def get_name_from_text(text: str) -> tuple:
    '''
    Gets the name and idfrom the text.

    Args:
        text (str): Input text.

    Returns:
        tuple(str,str): Cleaned text for label.
    '''     
    full_name=text.split("_")
    id,name=full_name[-1],full_name[:-1]
    return (id," ".join(name))

class StackActivityBar(QProgressBar):

    _remove_signal = pyqtSignal()

    def __init__(self, name: str, progress_bar_size: int,mode:str,
                 activity_start:datetime.datetime,activity_stop:datetime.datetime,
                 set_time:int=0, parent=None) -> None:
        '''
        The QProgressBar class is initialized with the following parameters:
            - parent: The parent widget.
            - setFixedSize: Sets the fixed size of the progress bar. (Currently fixed But will be changed to dynamic in the future.)
            - setStyleSheet: Sets the stylesheet of the progress bar. (A basic stylesheet is used for now. Will be changed to a dynamic stylesheet in the future.)
        _thread: The thread that handles the timer for the progress bar.
        _remove_signal: A signal that is emitted when the progress bar is removed.

        Args:
            name (str): The name of the progress bar.
            progress_bar_size (int): The max size of the progress bar.
            mode (str): Casual or Habit
            activity_start (datetime.datetime): Start time of the progress bar.
            activity_stop (datetime.datetime): Stop time of the progress bar.
            set_time (int, optional):Sets previous progress, only used for the progress bar that is created from the saved data. Defaults to 0.
            parent (_type_, optional): Defaults to None.
        '''    
   
               
        super(StackActivityBar, self).__init__(parent)
        self.setFixedSize(180, 250)
        self.setOrientation(QtCore.Qt.Orientation.Vertical)
        self.setAlignment(QtCore.Qt.AlignmentFlag.AlignCenter)
        self.setTextVisible(True)
        self.setObjectName(f"{name}")
        set_time=int(set_time)
        self.activity_mode=mode
        self.activity_id,name=get_name_from_text(name)
        self.name=name
        self.setFormat(f"{name} |  %p%")
        self.setRange(0, progress_bar_size)
        
        if set_time:
            if type(activity_start)==str:
                self.activity_stop=datetime.datetime.strptime(activity_stop,"%Y-%m-%d %H:%M:%S")
                self.activity_start=datetime.datetime.strptime(activity_start,"%Y-%m-%d %H:%M:%S")
            self.setValue(set_time)
            self.activity_original_size=int((self.activity_stop-self.activity_start).total_seconds())
            self._thread = TimerThread(progress_bar_size, f"Thread_{name}",set_progress=set_time)
            self._thread.current_value=set_time
            self._thread._set_progress_signal.connect(self.setValue)
        else:
            self._thread = TimerThread(progress_bar_size, f"Thread_{name}")
            self.activity_original_size=progress_bar_size
            self.setValue(progress_bar_size)        
            self._thread._set_progress_signal.connect(self.setValue)
            self.activity_start=activity_start
            self.activity_stop=activity_stop
        # Randomize the colors in the QSS content
        self.setStyleSheet(randomize_progress_bar_colors(str(QSS_FILE)))
        self._remove_signal.connect(self.deleteLater)
                
    def closeEvent(self, event: QEvent) -> None:
        '''
        When the progress bar is closed, it will terminate the thread.
        '''
        self._thread.terminate()

    def contextMenuEvent(self,event: QEvent) -> None:
        '''
        When the progress bar is right clicked, it will create a context menu with the following options:
            - Pause: When the pause option is clicked, it will pause the thread.
            - Resume: When the resume option is clicked, it will resume the thread.
        If the thread is paused, the resume option will be shown and vice versa.

        Args:
            event (QEvent): The context menu event.
        '''        
    
        menu = QMenu(self)

        if self._thread._is_running:
            pause_action = QAction("Pause", self)
            pause_action.triggered.connect(
                lambda: self._thread.pause(self.value()))
            menu.addAction(pause_action)
        elif not self._thread._is_running:
            resume_action = QAction("Resume", self)
            resume_action.triggered.connect(self._thread.resume)
            menu.addAction(resume_action)

        menu.exec(event.globalPos())

    def mouseMoveEvent(self, event: QEvent) -> None:
        '''
        When the progress bar is dragged, it will create a drag object with the following properties:
            - setMimeData: Sets the mime data of the drag object.
            - setPixmap: Sets the pixmap of the drag object.
            - exec: Executes the drag object.


        Args:
            event (QEvent): The mouse event.
        '''        
        if event.buttons() == Qt.MouseButton.LeftButton:
            drag = QDrag(self)
            mime = QMimeData()
            drag.setMimeData(mime)

            pixmap = QPixmap(self.size())
            self.render(pixmap)
            drag.setPixmap(pixmap)

            drag.exec(Qt.DropAction.MoveAction)


    def setValue(self, value: int) -> None:
        '''
        This is a modified version of the setValue method from the QProgressBar class.
        When the value of the progress bar is set, it will check if the value is 0.
        If the value is 0, it will emit the _remove_signal signal to remove the progress bar from the stack.

        Args:
            value (int): The value of the progress bar.
        '''

        super().setValue(value)
        if value == 0:
            self._remove_signal.emit()
            logger.info(f"{Color.MAGENTA} Progress bar {self.objectName()} has been removed.{Color.ENDC}")