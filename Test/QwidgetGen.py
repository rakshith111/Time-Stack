import sys
from PyQt6.QtCore import Qt
from PyQt6.QtWidgets import QApplication, QWidget, QScrollArea, QVBoxLayout, QLabel


class CustomWidget(QWidget):
    def __init__(self, text, parent=None):
        super().__init__(parent)

        # Create a QLabel to display the text
        self.label = QLabel(text)

        # Add the label to the layout
        layout = QVBoxLayout()
        layout.addWidget(self.label)
        self.setLayout(layout)


class MainWindow(QWidget):
    def __init__(self):
        super().__init__()

        # Create a scroll area
        scroll_area = QScrollArea(self)
        scroll_area.setWidgetResizable(True)

        # Create a widget to hold the custom widgets
        widget = QWidget(scroll_area)
        scroll_area.setWidget(widget)

        # Add custom widgets to the widget
        layout = QVBoxLayout(widget)
        for i in range(20):
            custom_widget = CustomWidget(
                "Custom Widget {}".format(i+1), parent=widget)
            layout.addWidget(custom_widget)

        # Set the layout of the main window
        main_layout = QVBoxLayout(self)
        main_layout.addWidget(scroll_area)


if __name__ == '__main__':
    app = QApplication(sys.argv)
    main_window = MainWindow()
    main_window.show()
    sys.exit(app.exec())
