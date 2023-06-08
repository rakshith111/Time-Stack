from PyQt6 import QtGui, QtWidgets, QtCore
from os import path
from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from ui_generated.main_menu import Ui_MainMenu

class QTimeStackMain(QtWidgets.QMainWindow):
    def __init__(self, parent=None) -> None:
        super().__init__(parent=parent)
        self.setWindowIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'hourglass_blackw.jpg')))
        self.main_menu_ui = Ui_MainMenu()
        self.main_menu_ui.setupUi(self)
        
        self.main_menu_ui.logo_label.setPixmap(QtGui.QPixmap(path.join(BASE_DIR, 'ui_files', 'icon', 'time_stack_white_small.png')))
        self.main_menu_ui.logo_label.setScaledContents(True)
        self.main_menu_ui.logo_label.setFixedSize(400,300)
        self.main_menu_ui.logo_label.setAlignment(QtCore.Qt.AlignmentFlag.AlignCenter)
        self.main_menu_ui.add_activity_m.setIcon(
            QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'add.png')))

        self.main_menu_ui.view_stack_m.setIcon(
            QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'hourglass_white.png')))
        self.main_menu_ui.view_stack_m.setIconSize(QtCore.QSize(40, 40))
        
   
        
        

if __name__ == '__main__':
    import sys
    app = QtWidgets.QApplication(sys.argv)
    main_menu = QTimeStackMain()
    app.setStyle('Fusion')

    main_menu.show()
    sys.exit(app.exec())
