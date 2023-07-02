import json
from os import path

from PyQt6 import QtGui, QtWidgets, QtCore
from PyQt6.QtGui import QCloseEvent,QAction
from PyQt6.QtWidgets import QApplication, QSystemTrayIcon, QMenu, QWidget

from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from libs.color import Color
from ui_generated.settings import Ui_settings
from libs.QClasses.QToggle import AnimatedToggle
DEFAULT_SETTINGS = {
    "theme": "dark",
    "close_to_tray": True
}
SETTINGS=path.join(BASE_DIR,"user_data" ,"settings.json")

class SettingsManager:
    def __init__(self, filename):
        self.filename = filename
        self.settings = DEFAULT_SETTINGS

    def save_settings(self):
        with open(self.filename, "w") as file:
            json.dump(self.settings, file)
        logger.info(f'{Color.GREEN}Settings saved{Color.ENDC}')

    def load_settings(self):
        if path.exists(self.filename):
            with open(self.filename, "r") as file:
                self.settings = json.load(file)
            logger.info(f'{Color.GREEN}Settings loaded{Color.ENDC}')
        else:
            logger.info(f'{Color.RED}Settings file not found, creating new one{Color.ENDC}')
            self.save_settings()



class SettingsWindow(QtWidgets.QWidget):
    def __init__(self, parent,manipulate_window ) -> None:
        super().__init__(parent=parent)
        self.manipulate_window = manipulate_window
        self.settings_ui = Ui_settings()
        self.settings_ui.setupUi(self)
        self.setWindowIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'window_icon_wob_s.png')))
        self.setWindowTitle("Settings")  
        self.settings_ui.tab_widget.setCurrentIndex(0)
        self.local_settings = SettingsManager(SETTINGS)
        self.local_settings.load_settings()
        self.settings_ui.tab_widget.setTabPosition(QtWidgets.QTabWidget.TabPosition.West)
        logger.info(f'{Color.GREEN}Setting up local settings{Color.ENDC}')
        # Theme btn
        self.toggle_theme_btn=AnimatedToggle()
        self.toggle_theme_btn.setFixedSize(self.toggle_theme_btn.sizeHint())
        self.toggle_theme_btn.setObjectName("toggle_theme_btn")
        
        self.settings_ui.main_app_layout.replaceWidget(self.settings_ui.theme_toggle_placeholder,self.toggle_theme_btn)
        self.settings_ui.theme_toggle_placeholder.deleteLater()
        self.toggle_theme_btn.setChecked(self.local_settings.settings["theme"]=="dark")
        self.toggle_theme_btn.stateChanged.connect(self.update_theme)
        self.manipulate_window.current_theme = self.local_settings.settings["theme"]


        # Close to tray btn
        self.toggle_close_to_tray_btn=AnimatedToggle()
        self.toggle_close_to_tray_btn.setFixedSize(self.toggle_close_to_tray_btn.sizeHint())
        self.toggle_close_to_tray_btn.setObjectName("toggle_close_to_tray_btn")
        
        self.settings_ui.main_app_layout.replaceWidget(self.settings_ui.close_to_tray_placeholder,self.toggle_close_to_tray_btn)
        self.settings_ui.close_to_tray_placeholder.deleteLater()
        self.toggle_close_to_tray_btn.setChecked(self.local_settings.settings["close_to_tray"])
        self.manipulate_window.close_to_tray = self.local_settings.settings["close_to_tray"]
        # change bool value of close_to_tray on state change
        self.toggle_close_to_tray_btn.stateChanged.connect(self.toggle_close_to_tray_update)
        self.toggle_close_to_tray_btn.bar_color="#44FFB000"
       
        

    def toggle_close_to_tray_update(self):
       
        self.local_settings.settings["close_to_tray"] = self.toggle_close_to_tray_btn.isChecked()
        self.manipulate_window.close_to_tray =self.local_settings.settings["close_to_tray"]
        logger.info(f'{Color.GREEN}Close to tray set to {self.local_settings.settings["close_to_tray"]}{Color.ENDC}')
        self.local_settings.save_settings()

    def update_theme(self):
        
        self.local_settings.settings["theme"] = "dark" if self.toggle_theme_btn.isChecked() else "light"
        self.manipulate_window.current_theme =  self.local_settings.settings["theme"]
        self.manipulate_window.toggle_theme()
        logger.info(f'{Color.GREEN}Theme set to {self.local_settings.settings["theme"]}{Color.ENDC}')
        self.local_settings.save_settings()
        
        
    def closeEvent(self, a0: QCloseEvent) -> None:
        self.hide()


