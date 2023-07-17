import sys
import pathlib
from PyQt6.QtWidgets import QApplication,QMainWindow
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
        self.current_theme =self.parent.current_theme
        self.toggle_theme()

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
            # Normal Python development mode
            BASE_DIR_RESC = pathlib.Path(__file__).parent.parent.absolute()
        return BASE_DIR_RESC

    def toggle_theme(self):
        '''
        Loads the items in the application.
        '''        
        self.time_stack_ui.logo_placeholder.setScaledContents(True)
        self.time_stack_ui.logo_placeholder.setPixmap(QtGui.QPixmap(str(pathlib.Path(self.BASE_DIR_RESC) / 'src' / 'ui_files' / 'icon' / 'horizontal_dark.png')))
        
        
