from PyQt6.QtWidgets import QApplication
from PyQt6.QtWidgets import QMainWindow
import sys
from PyQt6 import QtGui

class ThemeManager:
    def __init__(self, time_stack_ui:QMainWindow, parent=None):
        '''
        Initializes the ThemeManager class.

        Args:
            time_stack_ui (QMainWindow): The main window of the application.
            parent (optional): Defaults to None.
        '''        
        self.parent = parent
        self.time_stack_ui = time_stack_ui
        self.set_theme()
        self.BASE_DIR_RESC = self.resource_path()
        self.load_items()

    def set_theme(self):
        '''
        Sets the theme of the application to Fusion.
        '''        
        app = QApplication.instance()
        app.setStyle("Fusion")

    def resource_path(self):
        '''
        Gets the resource path. 
        Looks for the resource path in the sys._MEIPASS variable. When the program is bundled with PyInstaller, sys._MEIPASS is set to the path of the extracted data.

        '''        
        if hasattr(sys, '_MEIPASS'):
            # Bundled with PyInstaller
            BASE_DIR_RESC = sys._MEIPASS
        else:
            from libs._base_logger import BASE_DIR
            BASE_DIR_RESC = BASE_DIR
        return BASE_DIR_RESC

    def load_items(self):
        '''
        Loads the items in the application.
        '''        
        print(self.BASE_DIR_RESC)
        self.time_stack_ui.logo_placeholder.setPixmap(QtGui.QPixmap( r"src\ui_files\icon\horizontal_dark.png"))