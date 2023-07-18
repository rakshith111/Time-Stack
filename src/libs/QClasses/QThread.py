import sys
import pathlib
from copy import copy

from PyQt6.QtCore import  pyqtSignal,QThread
pathlib.Path(__file__).parent.parent.parent.absolute()
from libs._base_logger import logger
from libs.color import Color


class TimerThread(QThread):

    _set_progress_signal = pyqtSignal(int)
    # Signal that emits when the progress bar is midway done.
    _midway_signal = pyqtSignal()
    # Signal that emits when the progress bar is 1/4 done.
    _quarter_signal = pyqtSignal()


    def __init__(self, maxsize: int, name: str,set_progress:int=0) -> None:
        '''
        Thread class that handles the timer for the progress bar.
        Inherits from QThread class. Sets the name of the thread, the maxsize of the progress bar, and the current value of the progress bar.
        _is_running: A boolean that determines if the thread is running.
        _set_progress_signal: A signal that emits the current value of the progress bar.
        currentvalue: The current value of the progress bar. Starts at maxsize. Stores the value when paused and resumes from that value.

        Args:
            maxsize (int): The max size of the progress bar.
            name (str): The name of the thread.
            set_progress (int, optional): The current value of the progress bar. Defaults to 0.
        '''

        super(TimerThread, self).__init__()
        self.name = name
        self.maxsize = maxsize
        self._is_running = True
        if set_progress:
            self.current_value = set_progress
        else:
            self.current_value = copy(self.maxsize)    

    def __del__(self) -> None:
        '''
        When the thread is deleted, it will terminate the thread.
        '''
        self.terminate()

    def pause(self, value: int) -> None:
        '''    
        When the thread is paused, it will set the current value of the progress bar to the current_value attribute.
        It will also set the _is_running attribute to False to stop the thread from running.
        Args:
            value (int): The current value of the progress bar.
        '''
        if self._is_running:
            self.current_value = value
            self._is_running = False
            logger.info(f"{Color.YELLOW} Pausing {self.name} | value@ {self.current_value} | Is running?={self._is_running} {Color.ENDC}")

    def resume(self) -> None:
        '''
        When the thread is resumed,it will emit the current_value attribute to the progress bar. So the progress bar 
        will resume from the current_value attribute from when it was paused.
        After that, it will set the _is_running attribute to True to start the thread.
        '''
        if not self._is_running:
            self._set_progress_signal.emit(self.current_value)
            self._is_running = True
            logger.info(f"{Color.YELLOW} Resuming {self.name} | value@ {self.current_value} | Is running?={self._is_running} {Color.ENDC}")
    
    def run(self):
        '''
        Private method that runs the thread. This function is called when the start() method is invoked, it will subtract 1 from the current_value attribute.
        It will emit the current_value attribute to the progress bar. So the progress bar will update.
        If the current_value attribute is less than or equal to 0, it will set the current_value attribute to 0 and set the _is_running attribute to False to stop the thread.
        Now using QThread.sleep() instead of time.sleep()
        '''        
        midway = self.maxsize // 2
        quarter = self.maxsize // 4

        while self.current_value > 0:
            if self._is_running:
                self.current_value -= 1
                self._set_progress_signal.emit(self.current_value)
                if self.current_value == midway:
                    self._midway_signal.emit()
                    logger.info(f"{Color.RED} Midway {self.name} | {Color.YELLOW}value@ {self.current_value} | Is running?={self._is_running} {Color.ENDC}")
                elif self.current_value == quarter:
                    logger.info(f"{Color.RED} Quarter {self.name} | {Color.YELLOW}value@ {self.current_value} | Is running?={self._is_running} {Color.ENDC}")
                    self._quarter_signal.emit()
                self.sleep(1)
            else:
                self.sleep(1)

    def delay(self):
        self.run()

