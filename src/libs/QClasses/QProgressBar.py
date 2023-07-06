import sys
import datetime
from PyQt6.QtGui import QDrag, QPixmap,QAction 
from PyQt6.QtCore import Qt, QMimeData, pyqtSignal,QEvent
from PyQt6.QtWidgets import QProgressBar,QMenu
from PyQt6.QtGui import QColor
from PyQt6 import QtCore
from os import path
from random import randint

sys.path.append(path.dirname(path.dirname(path.dirname(path.abspath(__file__)))))

from libs._base_logger import logger,BASE_DIR
from libs.color import Color

try:
    from .QThread import TimerThread
except ImportError:
    from QThread import TimerThread

QSS_FILE = open(path.join(BASE_DIR, 'ui_files', 'stack.qss'), 'r').read()

def generate_random_gradient_colors() -> list:
   
    '''
    Generates a list of random colors.

    Returns:
        list: A list of random colors.
    '''    
    colors = [QColor(randint(0, 255), randint(0, 255), randint(0, 255)) for _ in range(4)]
    return colors


def randomize_progress_bar_colors(qss_content: str) -> str:
    '''
    Randomizes the colors in the QSS content.

    Args:
        qss_content (str): The QSS content.

    Returns:
        str: Altered QSS content.
    '''    
    colors = generate_random_gradient_colors()
    qss_content = qss_content.replace("#78d", colors[0].name()).replace("#46a", colors[1].name()).replace("#45a", colors[2].name()).replace("#238", colors[3].name())

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
        self.setStyleSheet(randomize_progress_bar_colors(QSS_FILE))
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