import os
import sys
from PyQt6.QtWidgets import QScrollArea
from PyQt6.QtCore import QTimer,  QEvent,QObject
from PyQt6.QtWidgets import QScrollArea


sys.path.append(os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__)))))

from libs._base_logger import logger
from libs.color import Color


class DragScrollArea(QScrollArea):
 
    def __init__(self,):
        '''
        DragScrollArea class inherits from QScrollArea class and adds the ability to scroll the content by dragging the mouse.
        Inherits from QScrollArea class.
        '''        
        super().__init__()
        self.setAcceptDrops(True)
        self.dragging = False

        # Install an event filter to capture mouse events
        self.viewport().installEventFilter(self)

        # Scroll variables
        self.scroll_timer = QTimer()
        self.scroll_timer.setInterval(50)
        self.scroll_timer.timeout.connect(self.scroll_content)
        # STOP SCROLLING
        self.scroll_timer.stop()
        self.scroll_direction = None

    def eventFilter(self, obj: QObject, event: QEvent) -> bool:
        '''
        Filters the events to capture the mouse events.

        Args:
            obj (QObject): Object that triggered the event.
            event (QEvent): Event triggered.

        Returns:
            bool: Returns True if the event was filtered, otherwise returns False.
        '''        
   
        if event.type() == QEvent.Type.DragEnter:
            event.accept()
            self.dragging = True
            self.scroll_timer.start()
        elif event.type() == QEvent.Type.DragLeave:
            self.dragging = False
            self.scroll_timer.stop()
            self.scroll_direction = None
        elif event.type() == QEvent.Type.DragMove and self.dragging:
            pos = event.position()
           
            viewport_rect = self.viewport().rect()
            if pos.y() < viewport_rect.top() + viewport_rect.height() // 3:
                self.scroll_direction = "down"
            # Scroll down if the cursor is closer to the bottom
            elif pos.y() > viewport_rect.bottom() - viewport_rect.height() // 3:
                self.scroll_direction = "up"
            else:
                self.scroll_direction = None
                self.scroll_timer.stop()
   
            
        elif event.type() == QEvent.Type.Drop:
            self.dragging = False
            self.scroll_timer.stop()
            self.scroll_direction = None
        elif event.type() == QEvent.Type.MouseButtonRelease:
            self.dragging = False
            self.scroll_timer.stop()
            self.scroll_direction = None

        return super().eventFilter(obj, event)

    def scroll_content(self):
        '''
        For smooth scrolling, this function is called by the scroll timer to scroll the content.
        '''        
        scroll_step = 10
        scroll_value = self.verticalScrollBar().value()

        if self.scroll_direction == "up":
            scroll_value += scroll_step  # Scroll up by subtracting the scroll_step
            scroll_value = max(scroll_value, self.verticalScrollBar().minimum())
        elif self.scroll_direction == "down":
            scroll_value -= scroll_step  # Scroll down by adding the scroll_step
            scroll_value = min(scroll_value, self.verticalScrollBar().maximum())

        if scroll_value != self.verticalScrollBar().value():
            self.verticalScrollBar().setValue(scroll_value)

            

    def dropEvent(self, event: QEvent) -> None:
        '''
        Stops the scroll timer when a drop event occurs.

        Args:
            event (QEvent): The drop event.
        '''        
        self.dragging = False
        self.scroll_timer.stop()
        self.scroll_direction = None
        logger.info(f"{Color.YELLOW} IS RUNNING? {self.scroll_timer.isActive()} {Color.ENDC}")
        super().dropEvent(event)

    def wheelEvent(self, event:QEvent) -> None:
        '''
        Stops the scroll timer when a wheel event occurs.

        Args:
            event (QEvent): Event triggered.
        '''        
        # Stop the scroll timer when a wheel event occurs
        self.scroll_timer.stop()
        super().wheelEvent(event)
        # Restart the scroll timer after the wheel event is processed
        self.scroll_timer.start()
    def dragLeaveEvent(self, event: QEvent) -> None:
        '''
        Stops the scroll timer when a drag leave event occurs.

        Args:
            event (QEvent): Event triggered.
        '''
        self.scroll_timer.stop()
  
    