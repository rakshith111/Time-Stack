from os import path

from PyQt6.QtCore import Qt
from PyQt6 import QtGui, QtWidgets, QtCore
from PyQt6.QtGui import QCloseEvent,QAction
from PyQt6.QtWidgets import QApplication, QSystemTrayIcon, QMenu

from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from libs.color import Color
from ui_generated.main_menu import Ui_MainMenu

from QStack_Generator import StackSpace
from QStack_Generator import StackGenerator
from QStack_Settings import SettingsWindow


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
   
        self.current_theme = None
        self.close_to_tray=True

        QtWidgets.QApplication.setStyle('Fusion')
        self.setWindowIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'window_icon_wob_s.png')))
  
        self.main_menu_ui.logo_label.setScaledContents(True)
        self.main_menu_ui.logo_label.setFixedSize(400,300)
        self.main_menu_ui.logo_label.setAlignment(QtCore.Qt.AlignmentFlag.AlignCenter)
        self.main_menu_ui.view_stack_m.setIconSize(QtCore.QSize(42, 42))
        self.stack_space = StackSpace()
        self.stack_generator = StackGenerator(self.stack_space)
        self.stack_space.stack_space_ui.add_btn.clicked.connect(self.stack_generator.show)
        self.main_menu_ui.add_activity_m.clicked.connect(self.stack_generator.show)
        self.main_menu_ui.view_stack_m.clicked.connect(self.stack_space.show)
        self.settings_window = SettingsWindow(None,self) 
        self.main_menu_ui.settings_m.clicked.connect(self.settings_window.show)
        

        # Create the system tray icon
        self.tray_icon = QSystemTrayIcon(self)     
        self.tray_menu = QMenu(self)
        self.toggle_theme() 
        self.show_action = QAction("Show", self)
        self.quit_action = QAction("Quit", self)
        self.show_action.triggered.connect(self.showWindow)
        self.quit_action.triggered.connect(self.quitApplication)
        self.tray_menu.addAction(self.show_action)
        self.tray_menu.addAction(self.quit_action)
        self.tray_icon.setContextMenu(self.tray_menu)
        self.tray_icon.show()

    def keyPressEvent(self, event: QtGui.QKeyEvent) -> None:
        # Ctrl + C to quit the application
        if event.modifiers() == QtCore.Qt.KeyboardModifier.ControlModifier and event.key() == QtCore.Qt.Key.Key_C:
            self.quitApplication()

        # Esc to hide the application
        elif event.key() == QtCore.Qt.Key.Key_Escape:
            self.hide()
            
    def closeEvent(self, event: QCloseEvent) -> None:
        if self.close_to_tray:
            event.ignore()  
            self.stack_space.hide()
            self.stack_generator.hide()
            self.settings_window.hide()
            self.hide()
        else:
            self.quitApplication()
   
    def showWindow(self):
        self.show()
    
    def quitApplication(self):
        # Quit the application when the "Quit" action is triggered
        self.tray_icon.deleteLater()
        self.stack_space.manager.save_stack()
        self.stack_space.close()
        self.stack_generator.close()
        self.settings_window.close()
        QApplication.quit()
    
        
    def toggle_theme(self)-> None:
        '''
        Toggles the theme of the application.
        
        '''
        if self.current_theme == "dark":

            logger.info(f"{Color.GREEN}Setting theme to dark {Color.ENDC}")

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
            self.settings_window.settings_ui.theme_icon_placeholder.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'sun.png')))
            self.settings_window.settings_ui.close_icon_placeholder.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'to_tray_white.png')))
            if self.settings_window.toggle_notification_btn.isChecked():
                self.settings_window.settings_ui.notification_icon_placeholder.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'active_notif_white.png')))
            else:
                self.settings_window.settings_ui.notification_icon_placeholder.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'inactive_notif_white.png')))
            self.main_menu_ui.logo_label.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'time_stack_white_small.png')))
            self.main_menu_ui.view_stack_m.setIcon(
            QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'hourglass_white.png')))
            self.main_menu_ui.settings_m.setIcon(
            QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'settings_white.png')))
            self.main_menu_ui.add_activity_m.setIcon(
            QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'add_white.png')))
            self.settings_window.settings_ui.theme_icon_placeholder.alignment=QtCore.Qt.AlignmentFlag.AlignLeft
            
            for child in [self.stack_space, self.stack_generator, self.settings_window]:
                try:
                    child.setPalette(dark_palette)
                except Exception as E:
                    logger.error(f"{Color.RED}Error setting palette for {child} {E}{Color.ENDC}")
            logger.debug(f"{Color.CBLUE} Updating toggle colors {Color.ENDC}")

            for toggle in [self.settings_window.toggle_close_to_tray_btn, self.settings_window.toggle_theme_btn,self.settings_window.toggle_notification_btn]: 
           
                toggle.setBarColor(Qt.GlobalColor.darkGray)
                toggle.setHandleColor(Qt.GlobalColor.lightGray)
                toggle.setCheckedColor(QtGui.QColor("#00b894"))
                toggle.setPulseUncheckedColor(QtGui.QColor("#ff7675"))
                toggle.setPulseCheckedColor(QtGui.QColor("#ff7675"))


            self.stack_space.stack_space_ui.start_btn.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'play_white.png')))
            self.stack_space.stack_space_ui.remove_btn.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'remove_white.png')))    
            self.stack_space.stack_space_ui.pause_btn.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'pause_white.png')))          
            self.tray_icon.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'window_icon_wob_s.png')))

            
        elif self.current_theme == "light":
           
            logger.info(f"{Color.GREEN}Setting theme to light {Color.ENDC}")
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
            self.settings_window.settings_ui.theme_icon_placeholder.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'moon.png')))
            self.settings_window.settings_ui.close_icon_placeholder.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'to_tray_black.png')))
            if self.settings_window.toggle_notification_btn.isChecked():
                self.settings_window.settings_ui.notification_icon_placeholder.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'active_notif_black.png')))
            else:
                self.settings_window.settings_ui.notification_icon_placeholder.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'inactive_notif_black.png')))
            self.settings_window.settings_ui.theme_icon_placeholder.alignment=QtCore.Qt.AlignmentFlag.AlignLeft
            
            self.main_menu_ui.logo_label.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'time_stack_white_black.png')))
            self.main_menu_ui.view_stack_m.setIcon(
            QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'hourglass_black.png')))
            self.main_menu_ui.settings_m.setIcon(
            QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'settings_black.png')))
            self.main_menu_ui.add_activity_m.setIcon(
            QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'add_black.png')))
            self.tray_icon.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'window_icon_bow_s.png')))
           
            for child in [self.stack_space, self.stack_generator, self.settings_window]:
                try:
                    child.setPalette(light_palette) 
                except Exception as E:
                    logger.error(f"{Color.RED}Error setting palette for {child} {E}{Color.ENDC}")
            logger.debug(f"{Color.CBLUE} Updating toggle colors {Color.ENDC}")
          
            for toggle in [self.settings_window.toggle_close_to_tray_btn, self.settings_window.toggle_theme_btn,self.settings_window.toggle_notification_btn]: 
            
                toggle.setBarColor(Qt.GlobalColor.lightGray)
                toggle.setHandleColor(Qt.GlobalColor.white)
                toggle.setCheckedColor(QtGui.QColor(0, 123, 255))
                toggle.setPulseUncheckedColor(QtGui.QColor('#AFAFAF'))
                toggle.setPulseCheckedColor(QtGui.QColor('#AFAFAF'))
                
            self.stack_space.stack_space_ui.start_btn.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'play_black.png')))
            self.stack_space.stack_space_ui.remove_btn.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'remove_black.png')))
            self.stack_space.stack_space_ui.pause_btn.setIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'pause_black.png')))

            
            
if __name__ == '__main__':
    import sys
    app = QtWidgets.QApplication(sys.argv)
    main_menu = QTimeStackMain()
    main_menu.show()
    sys.exit(app.exec())
