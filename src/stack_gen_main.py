import sys

from PyQt6 import QtWidgets
from PyQt6.QtCore import QThread, QObject, QTimer

from ui_modules.Stackgen import Ui_Stackgen


class MainWindow(QtWidgets.QMainWindow):

    def __init__(self, parent=None) -> None:

        super(MainWindow, self).__init__(parent=parent)

        self.ui = Ui_Stackgen()
        self.ui.setupUi(self)

if __name__ == '__main__':
    app = QtWidgets.QApplication(sys.argv)
    w = MainWindow()
    w.show()
    sys.exit(app.exec())