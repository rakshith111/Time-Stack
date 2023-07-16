import sys
import datetime
from PyQt6.QtCore import Qt
from PyQt6 import QtGui, QtWidgets, QtCore
from PyQt6.QtGui import QCloseEvent,QAction
from PyQt6.QtWidgets import QApplication, QSystemTrayIcon, QMenu


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
        self.time_stack_ui.logo_placeholder.setPixmap(QtGui.QPixmap(r"src\ui_files\icon\horizontal_white.png"))
        # Fit the image to the label
        self.time_stack_ui.logo_placeholder.setScaledContents(True)
        self.time_stack_ui.tabWidget.setTabPosition(QtWidgets.QTabWidget.TabPosition.West)

if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)
    app.setStyle("Fusion")
    window = TimeStack()
    window.show()
    sys.exit(app.exec())