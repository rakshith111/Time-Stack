from PyQt6 import QtGui, QtWidgets, QtCore
from PyQt6.QtGui import QCloseEvent
from os import path
from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from libs.color import Color
from ui_generated.main_menu import Ui_MainMenu

from QStack_Generator import StackSpace
from QStack_Generator import StackGenerator

class QTimeStackMain(QtWidgets.QMainWindow):
    def __init__(self, parent=None) -> None:
        '''
        Initializes the QTimeStackMainMenu class and sets up the UI.

        Args:
            parent (optional):  Defaults to None.
        '''        
        super().__init__(parent=parent)
        
        self.main_menu_ui = Ui_MainMenu()
        self.main_menu_ui.setupUi(self)
        self.main_menu_ui.toggle_theme.clicked.connect(self.toggle_theme)
        self.current_theme = 'light'
        QtWidgets.QApplication.setStyle('Fusion')
        self.setWindowIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'window_icon_wob_s.png')))
        self.main_menu_ui.toggle_theme.setIconSize(QtCore.QSize(42, 42))
        self.main_menu_ui.logo_label.setScaledContents(True)
        self.main_menu_ui.logo_label.setFixedSize(400,300)
        self.main_menu_ui.logo_label.setAlignment(QtCore.Qt.AlignmentFlag.AlignCenter)
        self.main_menu_ui.view_stack_m.setIconSize(QtCore.QSize(42, 42))
        self.stack_space = StackSpace()
        self.stack_generator = StackGenerator(self.stack_space)
        self.stack_space.stack_space_ui.add_btn.clicked.connect(self.stack_generator.show)
        self.main_menu_ui.add_activity_m.clicked.connect(self.stack_generator.show)
        self.main_menu_ui.view_stack_m.clicked.connect(self.stack_space.show)     
        self.toggle_theme() 

    def closeEvent(self, a0: QCloseEvent) -> None:
        # Close all windows, subwindows and stop all threads before closing the application  
        self.stack_space.close()
        self.stack_generator.close()
        a0.accept()
        
    def toggle_theme(self)-> None:
        '''
        Toggles the theme of the application.
        
        '''
        if self.current_theme == "dark":
            logger.info(f"{Color.GREEN}Current theme is dark switching to light {Color.ENDC}")
            light_palette = QtGui.QPalette()
            light_palette.setColor(QtGui.QPalette.ColorRole.Window, QtGui.QColor(240, 240, 240))
            light_palette.setColor(QtGui.QPalette.ColorRole.WindowText, QtCore.Qt.GlobalColor.black)
            light_palette.setColor(QtGui.QPalette.ColorRole.Base, QtGui.QColor(240, 240, 240))
            light_palette.setColor(QtGui.QPalette.ColorRole.AlternateBase, QtGui.QColor(240, 240, 240))
            light_palette.setColor(QtGui.QPalette.ColorRole.ToolTipBase, QtCore.Qt.GlobalColor.white)
            light_palette.setColor(QtGui.QPalette.ColorRole.ToolTipText, QtCore.Qt.GlobalColor.black)
            light_palette.setColor(QtGui.QPalette.ColorRole.Text, QtCore.Qt.GlobalColor.black)
            light_palette.setColor(QtGui.QPalette.ColorRole.Button, QtGui.QColor(240, 240, 240))
            light_palette.setColor(QtGui.QPalette.ColorRole.ButtonText, QtCore.Qt.GlobalColor.black)
            light_palette.setColor(QtGui.QPalette.ColorRole.BrightText, QtCore.Qt.GlobalColor.red)
            light_palette.setColor(QtGui.QPalette.ColorRole.Link, QtGui.QColor(42, 130, 218))
            light_palette.setColor(QtGui.QPalette.ColorRole.Highlight, QtGui.QColor(42, 130, 218))
            light_palette.setColor(QtGui.QPalette.ColorRole.HighlightedText, QtCore.Qt.GlobalColor.black)
            self.setPalette(light_palette)
            self.main_menu_ui.logo_label.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'time_stack_white_black.png')))
            self.main_menu_ui.view_stack_m.setIcon(
            QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'hourglass_black.png')))
            self.main_menu_ui.add_activity_m.setIcon(
            QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'add_black.png')))
           
    
            
            self.main_menu_ui.toggle_theme.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'moon.png')))
            self.current_theme = "light"
            for child in [self.stack_space, self.stack_generator]:
                try:
                    child.setPalette(light_palette)
                except Exception as E:
                    logger.error(f"{Color.RED}Error setting palette for {child} {E}{Color.ENDC}")
            self.stack_space.stack_space_ui.start_btn.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'play_black.png')))
            self.stack_space.stack_space_ui.remove_btn.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'remove_black.png')))
            self.stack_space.stack_space_ui.pause_btn.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'pause_black.png')))

            
        elif self.current_theme == "light":
           
            logger.info(f"{Color.GREEN}Current theme is light switching to dark {Color.ENDC}")
            dark_palette = QtGui.QPalette()
            dark_palette.setColor(QtGui.QPalette.ColorRole.Window, QtGui.QColor(53, 53, 53))
            dark_palette.setColor(QtGui.QPalette.ColorRole.WindowText, QtCore.Qt.GlobalColor.white)
            dark_palette.setColor(QtGui.QPalette.ColorRole.Base, QtGui.QColor(25, 25, 25))
            dark_palette.setColor(QtGui.QPalette.ColorRole.AlternateBase, QtGui.QColor(53, 53, 53))
            dark_palette.setColor(QtGui.QPalette.ColorRole.ToolTipBase, QtCore.Qt.GlobalColor.white)
            dark_palette.setColor(QtGui.QPalette.ColorRole.ToolTipText, QtCore.Qt.GlobalColor.white)
            dark_palette.setColor(QtGui.QPalette.ColorRole.Text, QtCore.Qt.GlobalColor.white)
            dark_palette.setColor(QtGui.QPalette.ColorRole.Button, QtGui.QColor(53, 53, 53))
            dark_palette.setColor(QtGui.QPalette.ColorRole.ButtonText, QtCore.Qt.GlobalColor.white)
            dark_palette.setColor(QtGui.QPalette.ColorRole.BrightText, QtCore.Qt.GlobalColor.red)
            dark_palette.setColor(QtGui.QPalette.ColorRole.Link, QtGui.QColor(42, 130, 218))
            dark_palette.setColor(QtGui.QPalette.ColorRole.Highlight, QtGui.QColor(42, 130, 218))
            dark_palette.setColor(QtGui.QPalette.ColorRole.HighlightedText, QtCore.Qt.GlobalColor.black)
            self.setPalette(dark_palette)
            self.main_menu_ui.logo_label.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'time_stack_white_small.png')))
            self.main_menu_ui.view_stack_m.setIcon(
            QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'hourglass_white.png')))
            self.main_menu_ui.add_activity_m.setIcon(
            QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'add_white.png')))
           
            self.main_menu_ui.toggle_theme.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'sun.png')))
            
            self.current_theme = "dark"
            for child in [self.stack_space, self.stack_generator]:
                try:
                    child.setPalette(dark_palette)
                except Exception as E:
                    logger.error(f"{Color.RED}Error setting palette for {child} {E}{Color.ENDC}")
            self.stack_space.stack_space_ui.start_btn.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'play_white.png')))
            self.stack_space.stack_space_ui.remove_btn.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'remove_white.png')))    
            self.stack_space.stack_space_ui.pause_btn.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'pause_white.png')))          
            

if __name__ == '__main__':
    import sys
    app = QtWidgets.QApplication(sys.argv)
    main_menu = QTimeStackMain()
    main_menu.show()
    sys.exit(app.exec())
