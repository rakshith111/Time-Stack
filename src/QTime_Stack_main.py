from PyQt6 import QtGui, QtWidgets, QtCore
from PyQt6.QtCore import QSize
from PyQt6.QtWidgets import QHBoxLayout, QLabel, QWidget,QPushButton

from os import path

from os import path


from libs._base_logger import logger
from libs._base_logger import BASE_DIR
from ui_generated.main_menu import Ui_MainMenu

class IconLabel(QtWidgets.QWidget):

    IconSize = QSize(50, 50)
    HorizontalSpacing = 2

    def __init__(self, icon_path, text, final_stretch=False):
        super(QWidget, self).__init__()

        layout = QHBoxLayout()
        layout.setContentsMargins(0, 0, 0, 0)
        self.setLayout(layout)

        icon = QLabel()
        icon.setPixmap(QtGui.QPixmap(icon_path))
        icon.setFixedSize(self.IconSize)

        layout.addWidget(icon)
        layout.addSpacing(self.HorizontalSpacing)
        layout.addWidget(QPushButton(text))

        if final_stretch:
            layout.addStretch()

class QTimeStackMain(QtWidgets.QMainWindow):
    def __init__(self, parent=None) -> None:
        super().__init__(parent=parent)
        
        self.main_menu_ui = Ui_MainMenu()
        self.main_menu_ui.setupUi(self)
        self.setWindowIcon(QtGui.QIcon(path.join(BASE_DIR, 'ui_files', 'icon', 'hourglass_blackw.png')))
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
