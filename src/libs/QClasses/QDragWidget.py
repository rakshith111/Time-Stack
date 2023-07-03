import os
import sys
from PyQt6.QtGui import QColor
from PyQt6.QtCore import QEvent
from PyQt6.QtCore import pyqtSignal
from PyQt6.QtWidgets import QWidget, QVBoxLayout, QSizePolicy, QLabel


sys.path.append(os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__)))))

from libs._base_logger import logger
from libs.color import Color


class DragWidget(QWidget):

    _order_changed_singal = pyqtSignal(list)

    def __init__(self):
        '''
        Class for the drag widget that is used to drag and drop the progress bars.
        Initializes the class and sets the layout to a vertical layout.
        _order_changed_singal is a signal that is emitted when the order of the progress bars is changed.
        '''        
        super().__init__()
        self.setAcceptDrops(True)
        self.drag_layout = QVBoxLayout()
        self.drag_layout.setObjectName("drag_layout")
        self.setLayout(self.drag_layout)
        self.setSizePolicy(QSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Expanding) )

        self.insert_indicator = None
        self.highlight_color = QColor(0, 0, 255, 100)  # Blue color with transparency
        

    def dragEnterEvent(self,event: QEvent) -> None:
        '''
        Event that is triggered when the drag widget is entered.

        Args:
            event (QEvent): Event that is triggered when the drag widget is entered.
        '''        
        event.accept()

    def dragMoveEvent(self, event: QEvent) -> None:
        '''
        Inserts a highliter to indicate where the progress bar will be dropped.
        
        Here's the list of things that happen when the drag widget is moved:
            - The position of the drag widget is set to the position of the mouse.
            - Obtains references to the first and last widgets within the drag layout
            - Special cases are handled when the dragged item is at the top or bottom of the drag layout.
            - If the target position falls between the first and last widgets, it iterates through all the widgets in the drag layout.

                For each widget, it calculates whether the target position is above the midpoint of the widget's height.
                If so, it calls the showInsertIndicator method to display an insertion indicator above that widget and breaks the loop.
                If the loop reaches the last widget without finding a suitable position, it calls the showInsertIndicator method to display an insertion indicator below the last widget.

        Args:
            event (QEvent): Event that is triggered when the drag widget is moved.
        '''        
        target_pos = event.position()
        first_widget = self.drag_layout.itemAt(0).widget()
        last_widget = self.drag_layout.itemAt(self.drag_layout.count() - 1).widget()

        if target_pos.y() < first_widget.y() + first_widget.size().height() // 2:
            self.showInsertIndicator(first_widget, above=True)
        elif target_pos.y() >= last_widget.y() + last_widget.size().height() // 2:
            self.showInsertIndicator(last_widget, above=False)
        else:
            for item_index in range(self.drag_layout.count()):
                loop_widget = self.drag_layout.itemAt(item_index).widget()
                drop_here = target_pos.y() < loop_widget.y() + loop_widget.size().height() // 2
                if drop_here:
                    self.showInsertIndicator(loop_widget, above=True)
                    break
                elif item_index == self.drag_layout.count() - 1:
                    self.showInsertIndicator(loop_widget, above=True)  # Display above the last widget

        event.accept()
    def dragLeaveEvent(self, event: QEvent) -> None:
        '''
        Event that is triggered when the drag widget is left.

        Args:
            event (QEvent): Event that is triggered when the drag widget is left.
        '''        
        self.hideInsertIndicator()

    def showInsertIndicator(self, target_widget: QWidget, above: bool = True) -> None:
        '''
        Displays an insertion indicator above or below the target widget.

        Args:
            target_widget (QWidget): Specifies the target widget.
            above (bool, optional): A boolean that indicates whether the insertion indicator should be displayed above or below the target widget. Defaults to True.
        '''        
        if not self.insert_indicator:
            self.insert_indicator = QLabel(self)
            self.insert_indicator.setObjectName("insert_indicator")
            self.insert_indicator.setFixedHeight(5)
            self.insert_indicator.setStyleSheet(f"background-color: {self.highlight_color.name()};")

        try:
            if above:
                self.drag_layout.insertWidget(self.drag_layout.indexOf(target_widget), self.insert_indicator)
            else:
                self.drag_layout.insertWidget(self.drag_layout.indexOf(target_widget) - 1, self.insert_indicator)
        except Exception as E:
            logger.error(f"Error in showInsertIndicator: {E}")
        

    def hideInsertIndicator(self):
        '''
        Hides the insertion indicator.
        '''        
        if self.insert_indicator:
            self.drag_layout.removeWidget(self.insert_indicator)
            self.insert_indicator.deleteLater()
            self.insert_indicator = None

    def dropEvent(self, event: QEvent) -> None:
        '''
        Handles the drop event.
        Here's the list of things that happen when the drag widget is moved:
            - The position of the drag widget is set to the position of the mouse.
            - Obtains references to the first and last widgets within the drag layout
            - Special cases are handled when the dragged item is at the top or bottom of the drag layout.
        - If the target position falls between the first and last widgets, it iterates through all the widgets in the drag layout.
            - It identifies the index of the dragged widget and the index of the widget that the dragged widget is being dropped on.
            - If the dragged widget is being dropped on a widget that is above it, it inserts the dragged widget at the index of the widget that it is being dropped on.
            - If the dragged widget is being dropped on a widget that is below it, it inserts the dragged widget at the index of the widget that it is being dropped on plus one.


        Args:
            event (QEvent): Event that is triggered when the drag widget is dropped.
        '''        
        self.get_item_data()
        self.hideInsertIndicator()

        target_position = event.position()
        current_widget = event.source()            
        logger.debug(f"{Color.GREEN} Grabbed{current_widget.objectName()} index :{self.drag_layout.indexOf(current_widget)}  {current_widget.pos()=}  {target_position=}{Color.ENDC}")
            
        first_widget = self.drag_layout.itemAt(0).widget()
        last_widget = self.drag_layout.itemAt(self.drag_layout.count() - 1).widget()

        try:

            if target_position.y() < first_widget.y() + first_widget.size().height() // 2:
                # Dragged item should be inserted at the top position
                logger.debug(f"{Color.GREEN} Dropped at the top position{Color.ENDC}")
                self.drag_layout.insertWidget(0, current_widget)
                self._order_changed_singal.emit(self.get_item_data())
            elif target_position.y() >= last_widget.y() + last_widget.size().height() // 2:
                # Dragged item should be inserted at the bottom position
                logger.debug(f"{Color.GREEN} Dropped at the bottom position{Color.ENDC}")
                self.drag_layout.insertWidget(self.drag_layout.count() - 1, current_widget)
                self._order_changed_singal.emit(self.get_item_data())
            else:
                for n in range(self.drag_layout.count()):
                    # Get the widget at each index in turn.
                    loop_widget = self.drag_layout.itemAt(n).widget()
                    # Drag drop vertically.
                    drop_here = target_position.y() < loop_widget.y() + loop_widget.size().height() // 2
                    logger.debug(f"{Color.GREEN} Vars used in dropEvent {target_position.y()=} < {loop_widget.y()=} {loop_widget.size().height()=} {drop_here=}{Color.ENDC}")
                    if drop_here:
                        # Identify if the widget is being dropped from below or above
                        if self.drag_layout.indexOf(current_widget) > n:
                            logger.debug(f"{Color.GREEN} Dropping from below {n=}{Color.ENDC} ")
                            self.drag_layout.insertWidget(n, current_widget)
                        else:
                            logger.debug(f"{Color.GREEN} Dropping from above {n-1=}{Color.ENDC}")
                            self.drag_layout.insertWidget(n-1, current_widget)
                        self._order_changed_singal.emit(self.get_item_data())
                        # Update the top in manager [here]
                        break
        except Exception as E:
            logger.error(f"Error in dropEvent: {E}")
        event.accept()


    def get_item_data(self)->list:
        '''
        Gets the data of the items in the drag layout.
        only for debugging purposes

        Returns:
            list: List of the data of the items in the drag layout.
        '''        
        data = []
      
        for n in range(self.drag_layout.count()):
            loop_widget = self.drag_layout.itemAt(n).widget()
            data.append(loop_widget)
            logger.debug(f"{Color.GREEN} Widget {loop_widget.objectName()} at index {n} pos {loop_widget.pos()}{Color.ENDC}")
         
     
        logger.debug(f"{Color.GREEN} {data=}{Color.ENDC}")
        return data


