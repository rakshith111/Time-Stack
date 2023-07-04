import json
from os import path,listdir

from PyQt6.QtCore import QUrl
from PyQt6 import QtGui, QtWidgets
from PyQt6.QtGui import QCloseEvent
from PyQt6.QtMultimedia import QSoundEffect


from libs.color import Color
from libs._base_logger import logger
from libs._base_logger import BASE_DIR

from ui_generated.settings import Ui_settings
from libs.QClasses.QToggle import AnimatedToggle

DEFAULT_SETTINGS = {
    "theme": "dark",
    "close_to_tray": True,
    "notification": True,
    "notification_sound_general": "upset(Default_gen).wav",
    "notification_sound_midway": "that-was-quick(Default_mid).wav",
    "notification_sound_quarterly": "good-news(Default_quat).wav",
    "notification_sound_end": "strong-minded(Default_end).wav",
}

SETTINGS=path.join(BASE_DIR,"user_data" ,"settings.json")

class SettingsManager:
    def __init__(self, filename):
        self.filename = filename
        self.settings = DEFAULT_SETTINGS

    def save_settings(self):
        with open(self.filename, "w") as file:
            json.dump(self.settings, file,indent=4)
        logger.info(f'{Color.CVIOLET}Settings saved{Color.ENDC}')

    def load_settings(self):
        if path.exists(self.filename):
            with open(self.filename, "r") as file:
                self.settings = json.load(file)
            logger.info(f'{Color.CVIOLET}Settings loaded{Color.ENDC}')
        else:
            logger.info(f'{Color.RED}Settings file not found, creating new one{Color.ENDC}')
            if not path.exists((path.join(BASE_DIR,"user_data"))):
                logger.info(f'{Color.RED}user_data folder not found, creating new one{Color.ENDC}')
                from os import mkdir
                mkdir(path.join(BASE_DIR,"user_data" ))
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
        logger.info(f'{Color.CVIOLET}Setting up local settings{Color.ENDC}')
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

        # Notification btn
        self.toggle_notification_btn=AnimatedToggle()
        self.toggle_notification_btn.setFixedSize(self.toggle_notification_btn.sizeHint())
        self.toggle_notification_btn.setObjectName("toggle_notification_btn")

        self.settings_ui.main_app_layout.replaceWidget(self.settings_ui.notification_placeholder,self.toggle_notification_btn)
        self.settings_ui.notification_placeholder.deleteLater()
        self.toggle_notification_btn.setChecked(self.local_settings.settings["notification"])
        self.manipulate_window.stack_space.manager.notifications_enabled = self.local_settings.settings["notification"]
        # change bool value of notification on state change
        self.toggle_notification_btn.stateChanged.connect(self.toggle_notification_update)
        self.populate_notification_sounds()
        
    def populate_notification_sounds(self):
        self.notification_sounds = []
        for sound in listdir(path.join(BASE_DIR, "ui_files", "sounds")):
            self.notification_sounds.append(sound)
        self.settings_ui.general_notification_sound.addItems(self.notification_sounds)
        self.settings_ui.midway_notification_sound.addItems(self.notification_sounds)
        self.settings_ui.quarterly_notification_sound.addItems(self.notification_sounds)
        self.settings_ui.end_notification_sound.addItems(self.notification_sounds)
        self.settings_ui.general_notification_sound.setCurrentText(self.local_settings.settings["notification_sound_general"])
        self.settings_ui.midway_notification_sound.setCurrentText(self.local_settings.settings["notification_sound_midway"])
        self.settings_ui.quarterly_notification_sound.setCurrentText(self.local_settings.settings["notification_sound_quarterly"])
        self.settings_ui.end_notification_sound.setCurrentText(self.local_settings.settings["notification_sound_end"])
        self.settings_ui.general_notification_sound.currentTextChanged.connect(self.update_general_notification_sound)
        self.settings_ui.midway_notification_sound.currentTextChanged.connect(self.update_midway_notification_sound)
        self.settings_ui.quarterly_notification_sound.currentTextChanged.connect(self.update_quarterly_notification_sound)
        self.settings_ui.end_notification_sound.currentTextChanged.connect(self.update_end_notification_sound)
        self.manipulate_window.stack_space.manager.notification_sound_general = self.local_settings.settings["notification_sound_general"]
        self.manipulate_window.stack_space.manager.notification_sound_midway = self.local_settings.settings["notification_sound_midway"]
        self.manipulate_window.stack_space.manager.notification_sound_quarterly = self.local_settings.settings["notification_sound_quarterly"]
        self.manipulate_window.stack_space.manager.notification_sound_end = self.local_settings.settings["notification_sound_end"]
        logger.info(f'{Color.CVIOLET}Notification sounds set{Color.ENDC}')

    def play_sound(self,sound:str)->None:

        self.sound = QSoundEffect()
        self.sound.setSource(QUrl.fromLocalFile(path.join(BASE_DIR, "ui_files", "sounds", sound)))
        self.sound.setVolume(1)
        self.sound.play()

    def update_general_notification_sound(self):

        self.local_settings.settings["notification_sound_general"] = self.settings_ui.general_notification_sound.currentText()
        self.manipulate_window.stack_space.manager.notification_sound_general = self.local_settings.settings["notification_sound_general"]
        logger.info(f'{Color.CVIOLET}General notification sound set to {self.local_settings.settings["notification_sound_general"]}{Color.ENDC}')
        self.local_settings.save_settings()
        self.play_sound(self.local_settings.settings["notification_sound_general"])         
        
    
    def update_midway_notification_sound(self):
            
        self.local_settings.settings["notification_sound_midway"] = self.settings_ui.midway_notification_sound.currentText()
        self.manipulate_window.stack_space.manager.notification_sound_midway = self.local_settings.settings["notification_sound_midway"]
        logger.info(f'{Color.CVIOLET}Midway notification sound set to {self.local_settings.settings["notification_sound_midway"]}{Color.ENDC}')
        self.local_settings.save_settings()
        self.play_sound(self.local_settings.settings["notification_sound_midway"])

    def update_quarterly_notification_sound(self):
                
        self.local_settings.settings["notification_sound_quarterly"] = self.settings_ui.quarterly_notification_sound.currentText()
        self.manipulate_window.stack_space.manager.notification_sound_quarterly = self.local_settings.settings["notification_sound_quarterly"]
        logger.info(f'{Color.CVIOLET}Quarterly notification sound set to {self.local_settings.settings["notification_sound_quarterly"]}{Color.ENDC}')
        self.local_settings.save_settings()
        self.play_sound(self.local_settings.settings["notification_sound_quarterly"])

    def update_end_notification_sound(self):
                    
        self.local_settings.settings["notification_sound_end"] = self.settings_ui.end_notification_sound.currentText()
        self.manipulate_window.stack_space.manager.notification_sound_end = self.local_settings.settings["notification_sound_end"]
        logger.info(f'{Color.CVIOLET}End notification sound set to {self.local_settings.settings["notification_sound_end"]}{Color.ENDC}')
        self.local_settings.save_settings()
        self.play_sound(self.local_settings.settings["notification_sound_end"])
        
    def toggle_close_to_tray_update(self):
       
        self.local_settings.settings["close_to_tray"] = self.toggle_close_to_tray_btn.isChecked()
        self.manipulate_window.close_to_tray =self.local_settings.settings["close_to_tray"]
        logger.info(f'{Color.CVIOLET}Close to tray set to {self.local_settings.settings["close_to_tray"]}{Color.ENDC}')
        self.local_settings.save_settings()

    def update_theme(self):
        
        self.local_settings.settings["theme"] = "dark" if self.toggle_theme_btn.isChecked() else "light"
        self.manipulate_window.current_theme =  self.local_settings.settings["theme"]
        self.manipulate_window.toggle_theme()
        logger.info(f'{Color.CVIOLET}Theme set to {self.local_settings.settings["theme"]}{Color.ENDC}')
        self.local_settings.save_settings()
    
    def toggle_notification_update(self):
        
        self.local_settings.settings["notification"] = self.toggle_notification_btn.isChecked()
        self.manipulate_window.notifications_enabled = self.local_settings.settings["notification"]
        logger.info(f'{Color.CVIOLET}Notification set to {self.local_settings.settings["notification"]}{Color.ENDC}')
        self.manipulate_window.stack_space.manager.notifications_enabled = self.local_settings.settings["notification"]
        self.local_settings.save_settings()
        self.manipulate_window.toggle_theme()
        if self.local_settings.settings["notification"]:
            # Hide the notification if it is visible
            pass
        
    def closeEvent(self, a0: QCloseEvent) -> None:
        self.hide()


