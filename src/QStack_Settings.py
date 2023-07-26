import json
import pathlib

from libs.QClasses.QToggle import AnimatedToggle
from libs.color import Color
from libs._base_logger import logger
from libs._base_logger import BASE_DIR

from PyQt6 import QtCore
from PyQt6 import QtGui
from PyQt6.QtMultimedia import QSoundEffect
from PyQt6.QtCore import Qt,QUrl ,QStandardPaths
from PyQt6.QtWidgets import QApplication,QMainWindow


DEFAULT_SETTINGS = {
    "theme": "dark",
    "close_to_tray": True,
    "notification": True,
    "notification_sound_general": "upset_(Default_gen).wav",
    "notification_sound_midway": "that_was_quick_(Default_midway).wav",
    "notification_sound_quarterly": "good_news_(Default_quat).wav",
    "notification_sound_end": "strong_minded_(Default_end).wav",
}

SETTINGS=pathlib.Path(QStandardPaths.writableLocation(QStandardPaths.StandardLocation.AppLocalDataLocation),'TIME_STACK','user_data','settings.json')

class SettingsManager:

    def __init__(self, filename: str):
        '''
        Settings manager class to manage the settings of the app
        Handles the saving and loading of the settings.

        Args:
            filename (str): Path to the settings file
        '''        

        self.filename = filename
        self.settings = DEFAULT_SETTINGS

    def save_settings(self):
        '''
        Saves the settings to the settings file
        '''        
        with open(self.filename, "w") as file:
            json.dump(self.settings, file,indent=4)
        logger.info(f'{Color.CVIOLET}Settings saved{Color.ENDC}')

    def load_settings(self):
        '''
        Performs the loading of the settings from the settings file
        Also creates a new settings file if not found.
        '''        
        if pathlib.Path(self.filename).exists():
            with open(self.filename, "r") as file:
                self.settings = json.load(file)
            logger.info(f'{Color.CVIOLET}Settings loaded{Color.ENDC}')
        else:
            logger.info(f'{Color.RED}Settings file not found, creating new one{Color.ENDC}')
           
            if not pathlib.Path(pathlib.Path(self.filename).parent).exists():
                logger.info(f'{Color.RED}user_data folder not found, creating new one{Color.ENDC}')
                pathlib.Path(pathlib.Path(self.filename).parent).mkdir(parents=True, exist_ok=True)

            self.save_settings()

class ThemeManager:
    def __init__(self, time_stack_ui:QMainWindow, parent):
        '''
        Initializes the ThemeManager class.

        Args:
            time_stack_ui (QMainWindow): The main window of the application.
            parent (optional): Defaults to None.
        '''        
        self.parent = parent
        self.time_stack_ui = time_stack_ui
       
        self._init_settings()
        self.set_theme()

    def _init_settings(self):
        '''
        Initializes the settings  page of the application. 
        Replaces the placeholders with the actual widgets.
        Populates items with the settings data.
        Loads the settings from the settings file and updates the UI.
        '''        
   
        self.local_settings=SettingsManager(SETTINGS)
        self.local_settings.load_settings()

        self.time_stack_ui.toggle_theme_btn=AnimatedToggle()
        self.time_stack_ui.toggle_theme_btn.setFixedSize(self.time_stack_ui.toggle_theme_btn.sizeHint())
        self.time_stack_ui.toggle_theme_btn.setObjectName("toggle_theme_btn")

        self.time_stack_ui.main_app_layout.replaceWidget(self.time_stack_ui.theme_toggle_placeholder,self.time_stack_ui.toggle_theme_btn)
        self.time_stack_ui.theme_toggle_placeholder.deleteLater()
        self.time_stack_ui.toggle_theme_btn.setChecked(self.local_settings.settings["theme"]=="dark")
        self.time_stack_ui.toggle_theme_btn.stateChanged.connect(self.update_theme)
        self.parent.current_theme = self.local_settings.settings["theme"]

        # Close to tray btn
        self.time_stack_ui.toggle_close_to_tray_btn=AnimatedToggle()
        self.time_stack_ui.toggle_close_to_tray_btn.setFixedSize(self.time_stack_ui.toggle_close_to_tray_btn.sizeHint())
        self.time_stack_ui.toggle_close_to_tray_btn.setObjectName("toggle_close_to_tray_btn")

        self.time_stack_ui.main_app_layout.replaceWidget(self.time_stack_ui.close_to_tray_placeholder,self.time_stack_ui.toggle_close_to_tray_btn)
        self.time_stack_ui.close_to_tray_placeholder.deleteLater()
      
        self.time_stack_ui.toggle_close_to_tray_btn.setChecked(self.local_settings.settings["close_to_tray"])
        self.parent.close_to_tray=self.local_settings.settings["close_to_tray"]
        # change bool value of close_to_tray on state change
        self.time_stack_ui.toggle_close_to_tray_btn.stateChanged.connect(self.toggle_close_to_tray_update)
        

        # Notification btn
        self.time_stack_ui.toggle_notification_btn=AnimatedToggle()
        self.time_stack_ui.toggle_notification_btn.setFixedSize(self.time_stack_ui.toggle_notification_btn.sizeHint())
        self.time_stack_ui.toggle_notification_btn.setObjectName("toggle_notification_btn")

        self.time_stack_ui.main_app_layout.replaceWidget(self.time_stack_ui.notification_placeholder,self.time_stack_ui.toggle_notification_btn)
        self.time_stack_ui.notification_placeholder.deleteLater()
        self.time_stack_ui.toggle_notification_btn.setChecked(self.local_settings.settings["notification"])
    
        self.parent.manager.notifications_enabled = self.local_settings.settings["notification"]
        # change bool value of notification on state change
        self.time_stack_ui.toggle_notification_btn.stateChanged.connect(self.toggle_notification_update)
        self.populate_notification_sounds()
        self.toggle_notification_update()

    def populate_notification_sounds(self):
        '''
        Populates the notification sounds in the settings window
        '''        
        self.notification_sounds = []
        for sounds in pathlib.Path(BASE_DIR,"src","ui_files", "sounds").iterdir():
            self.notification_sounds.append(sounds.name)
        self.time_stack_ui.general_notification_sound.addItems(self.notification_sounds)
        self.time_stack_ui.midway_notification_sound.addItems(self.notification_sounds)
        self.time_stack_ui.quarterly_notification_sound.addItems(self.notification_sounds)
        self.time_stack_ui.end_notification_sound.addItems(self.notification_sounds)
        self.time_stack_ui.general_notification_sound.setCurrentText(self.local_settings.settings["notification_sound_general"])
        self.time_stack_ui.midway_notification_sound.setCurrentText(self.local_settings.settings["notification_sound_midway"])
        self.time_stack_ui.quarterly_notification_sound.setCurrentText(self.local_settings.settings["notification_sound_quarterly"])
        self.time_stack_ui.end_notification_sound.setCurrentText(self.local_settings.settings["notification_sound_end"])
        self.time_stack_ui.general_notification_sound.currentTextChanged.connect(self.update_general_notification_sound)
        self.time_stack_ui.midway_notification_sound.currentTextChanged.connect(self.update_midway_notification_sound)
        self.time_stack_ui.quarterly_notification_sound.currentTextChanged.connect(self.update_quarterly_notification_sound)
        self.time_stack_ui.end_notification_sound.currentTextChanged.connect(self.update_end_notification_sound)
        self.parent.manager.notification_sound_general = self.local_settings.settings["notification_sound_general"]
        self.parent.manager.notification_sound_midway = self.local_settings.settings["notification_sound_midway"]
        self.parent.manager.notification_sound_quarterly = self.local_settings.settings["notification_sound_quarterly"]
        self.parent.manager.notification_sound_end = self.local_settings.settings["notification_sound_end"]
        logger.info(f'{Color.CVIOLET}Notification sounds set{Color.ENDC}')

    def toggle_notification_update(self):
        '''
        This function is called when the user toggles the notification button
        Updates the notification settings in manager and updates the notification labels
        '''        
        
        self.local_settings.settings["notification"] = self.time_stack_ui.toggle_notification_btn.isChecked()
        logger.info(f'{Color.CVIOLET}Notification set to {self.local_settings.settings["notification"]}{Color.ENDC}')
        self.parent.manager.notifications_enabled = self.local_settings.settings["notification"]
        self.local_settings.save_settings()
        self.toggle_theme()
        if not self.local_settings.settings["notification"]:
            # Hide the notification if it is visible
            self.time_stack_ui.general_notification_label.hide()
            self.time_stack_ui.midway_notification_label.hide()
            self.time_stack_ui.quarterly_notification_label.hide()
            self.time_stack_ui.end_notification_label.hide()

            self.time_stack_ui.general_notification_sound.hide()
            self.time_stack_ui.midway_notification_sound.hide()
            self.time_stack_ui.quarterly_notification_sound.hide()
            self.time_stack_ui.end_notification_sound.hide()

        else:

            self.time_stack_ui.general_notification_label.show()
            self.time_stack_ui.midway_notification_label.show()
            self.time_stack_ui.quarterly_notification_label.show()
            self.time_stack_ui.end_notification_label.show()

            self.time_stack_ui.general_notification_sound.show()
            self.time_stack_ui.midway_notification_sound.show()
            self.time_stack_ui.quarterly_notification_sound.show()
            self.time_stack_ui.end_notification_sound.show()

    def play_sound(self,sound:str)->None:
        '''
        This function plays the sound passed to it

        Args:
            sound (str): Sound to be played
        '''        

        self.sound = QSoundEffect()

        self.sound.setSource(QUrl.fromLocalFile(str(pathlib.Path(BASE_DIR,"src","ui_files", "sounds", sound))))
        self.sound.setVolume(1)
        self.sound.play()

    def update_general_notification_sound(self):

        '''
        This function is called when the user changes the general notification sound, it updates the settings and plays the sound
        '''
        self.local_settings.settings["notification_sound_general"] = self.time_stack_ui.general_notification_sound.currentText()
        self.parent.manager.notification_sound_general = self.local_settings.settings["notification_sound_general"]
        logger.info(f'{Color.CVIOLET}General notification sound set to {self.local_settings.settings["notification_sound_general"]}{Color.ENDC}')
        self.local_settings.save_settings()
        self.play_sound(self.local_settings.settings["notification_sound_general"])        

    def update_midway_notification_sound(self):
        '''
        This function is called when the user changes the midway notification sound, it updates the settings and plays the sound
        '''
        self.local_settings.settings["notification_sound_midway"] = self.time_stack_ui.midway_notification_sound.currentText()
        self.parent.manager.notification_sound_midway = self.local_settings.settings["notification_sound_midway"]
        logger.info(f'{Color.CVIOLET}Midway notification sound set to {self.local_settings.settings["notification_sound_midway"]}{Color.ENDC}')
        self.local_settings.save_settings()
        self.play_sound(self.local_settings.settings["notification_sound_midway"])

    def update_quarterly_notification_sound(self):
        '''
        This function is called when the user changes the quarterly notification sound, it updates the settings and plays the sound
        '''
        self.local_settings.settings["notification_sound_quarterly"] = self.time_stack_ui.quarterly_notification_sound.currentText()
        self.parent.manager.notification_sound_quarterly = self.local_settings.settings["notification_sound_quarterly"]
        logger.info(f'{Color.CVIOLET}Quarterly notification sound set to {self.local_settings.settings["notification_sound_quarterly"]}{Color.ENDC}')
        self.local_settings.save_settings()
        self.play_sound(self.local_settings.settings["notification_sound_quarterly"])

    def update_end_notification_sound(self):
        '''
        This function is called when the user changes the end notification sound, it updates the settings and plays the sound
        '''
        self.local_settings.settings["notification_sound_end"] = self.time_stack_ui.end_notification_sound.currentText()
        self.parent.manager.notification_sound_end = self.local_settings.settings["notification_sound_end"]
        logger.info(f'{Color.CVIOLET}End notification sound set to {self.local_settings.settings["notification_sound_end"]}{Color.ENDC}')
        self.local_settings.save_settings()
        self.play_sound(self.local_settings.settings["notification_sound_end"])
        
    def toggle_close_to_tray_update(self):
        '''
        This function is called when the user toggles the close to tray button
        '''
        self.local_settings.settings["close_to_tray"] =self.time_stack_ui.toggle_close_to_tray_btn.isChecked()
        self.parent.close_to_tray =self.local_settings.settings["close_to_tray"]
        logger.info(f'{Color.CVIOLET}Close to tray set to {self.local_settings.settings["close_to_tray"]}{Color.ENDC}')
        self.local_settings.save_settings()

    def update_theme(self):
        '''
        This function is called when the user toggles the theme button
        '''
        self.local_settings.settings["theme"] = "dark" if self.time_stack_ui.toggle_theme_btn.isChecked() else "light"
        self.parent.current_theme =  self.local_settings.settings["theme"]
        logger.info(f'{Color.CVIOLET}Theme set to {self.local_settings.settings["theme"]}{Color.ENDC}')
        self.local_settings.save_settings()
        self.toggle_theme()

    def set_theme(self):
        '''
        Sets the theme of the application to Fusion.
        '''        
        app = QApplication.instance()
        app.setStyle("Fusion")
        self.toggle_theme()

    
    def toggle_theme(self):
        '''
        Loads the items in the application.
        '''

        if self.parent.current_theme == "dark":

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

            self.parent.setPalette(dark_palette)
          
            self.time_stack_ui.theme_icon_placeholder.setPixmap(QtGui.QPixmap(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'sun.png')))
            self.time_stack_ui.close_icon_placeholder.setPixmap(QtGui.QPixmap(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'to_tray_white.png')))

            if self.time_stack_ui.toggle_notification_btn.isChecked():
                self.time_stack_ui.notification_icon_placeholder.setPixmap(QtGui.QPixmap(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'active_notif_white.png')))
            else:
                self.time_stack_ui.notification_icon_placeholder.setPixmap(QtGui.QPixmap(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'inactive_notif_white.png')))
            self.time_stack_ui.logo_placeholder.setPixmap(QtGui.QPixmap(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'horizontal_white.png')))
            self.time_stack_ui.theme_icon_placeholder.alignment=QtCore.Qt.AlignmentFlag.AlignLeft

            self.parent.tray_icon.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'window_icon_bow_s.png')))

            for toggle in [self.time_stack_ui.toggle_close_to_tray_btn, self.time_stack_ui.toggle_theme_btn,self.time_stack_ui.toggle_notification_btn]: 

                toggle.setBarColor(Qt.GlobalColor.darkGray)
                toggle.setHandleColor(Qt.GlobalColor.lightGray)
                toggle.setCheckedColor(QtGui.QColor("#00b894"))
                toggle.setPulseUncheckedColor(QtGui.QColor("#ff7675"))
                toggle.setPulseCheckedColor(QtGui.QColor("#ff7675"))

            self.time_stack_ui.start_btn.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'play_white.png')))
            self.time_stack_ui.remove_btn.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'remove_white.png')))
            self.time_stack_ui.pause_btn.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'pause_white.png')))

            self.time_stack_ui.main_tab_widget.setTabIcon(1,QtGui.QIcon(str(pathlib.Path(BASE_DIR) /'src'/ 'ui_files' / 'icon' / 'add_white.png')))
            self.time_stack_ui.main_tab_widget.setTabIcon(2,QtGui.QIcon(str(pathlib.Path(BASE_DIR) /'src'/ 'ui_files' / 'icon' / 'settings_white.png')))
            self.time_stack_ui.main_tab_widget.setTabIcon(0,QtGui.QIcon(str(pathlib.Path(BASE_DIR) /'src'/ 'ui_files' / 'icon' / 'hourglass_white.png')))
            self.time_stack_ui.main_tab_widget.setTabIcon(3,QtGui.QIcon(str(pathlib.Path(BASE_DIR) /'src'/ 'ui_files' / 'icon' / 'device_white.png')))

            self.time_stack_ui.minimize_btn.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'minimize_white.png')))
            self.time_stack_ui.close_btn.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'close_white.png')))
           
            

        elif self.parent.current_theme == "light":
           
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
            self.parent.setPalette(light_palette)
            self.time_stack_ui.theme_icon_placeholder.setPixmap(QtGui.QPixmap(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'moon.png')))
            self.time_stack_ui.close_icon_placeholder.setPixmap(QtGui.QPixmap(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'to_tray_black.png')))

            if self.time_stack_ui.toggle_notification_btn.isChecked():
                self.time_stack_ui.notification_icon_placeholder.setPixmap(QtGui.QPixmap(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'active_notif_black.png')))
            else:
                self.time_stack_ui.notification_icon_placeholder.setPixmap(QtGui.QPixmap(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'inactive_notif_black.png')))

            self.time_stack_ui.theme_icon_placeholder.alignment=QtCore.Qt.AlignmentFlag.AlignLeft
            self.time_stack_ui.logo_placeholder.setPixmap(QtGui.QPixmap(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'horizontal_black.png')))
            self.parent.tray_icon.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'window_icon_wob_s.png')))

            for toggle in [self.time_stack_ui.toggle_close_to_tray_btn, self.time_stack_ui.toggle_theme_btn,self.time_stack_ui.toggle_notification_btn]: 
            
                toggle.setBarColor(Qt.GlobalColor.lightGray)
                toggle.setHandleColor(Qt.GlobalColor.white)
                toggle.setCheckedColor(QtGui.QColor(0, 123, 255))
                toggle.setPulseUncheckedColor(QtGui.QColor('#AFAFAF'))
                toggle.setPulseCheckedColor(QtGui.QColor('#AFAFAF'))
                
            self.time_stack_ui.start_btn.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'play_black.png')))
            self.time_stack_ui.remove_btn.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'remove_black.png')))
            self.time_stack_ui.pause_btn.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'pause_black.png')))

            self.time_stack_ui.main_tab_widget.setTabIcon(1,QtGui.QIcon(str(pathlib.Path(BASE_DIR) /'src'/ 'ui_files' / 'icon' / 'add_black.png')))
            self.time_stack_ui.main_tab_widget.setTabIcon(2,QtGui.QIcon(str(pathlib.Path(BASE_DIR) /'src'/ 'ui_files' / 'icon' / 'settings_black.png')))
            self.time_stack_ui.main_tab_widget.setTabIcon(0,QtGui.QIcon(str(pathlib.Path(BASE_DIR) /'src'/ 'ui_files' / 'icon' / 'hourglass_black.png')))
            self.time_stack_ui.main_tab_widget.setTabIcon(3,QtGui.QIcon(str(pathlib.Path(BASE_DIR) /'src'/ 'ui_files' / 'icon' / 'device_black.png')))

            self.time_stack_ui.minimize_btn.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'minimize_black.png')))
            self.time_stack_ui.close_btn.setIcon(QtGui.QIcon(str(pathlib.Path(BASE_DIR) / 'src' / 'ui_files' / 'icon' / 'close_black.png')))