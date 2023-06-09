# Form implementation generated from reading ui file 'd:\gitprojs\new-git\Time-Stack\src\ui_files\main_menu.ui'
#
# Created by: PyQt6 UI code generator 6.5.1
#
# WARNING: Any manual changes made to this file will be lost when pyuic6 is
# run again.  Do not edit this file unless you know what you are doing.


from PyQt6 import QtCore, QtGui, QtWidgets


class Ui_MainMenu(object):
    def setupUi(self, MainMenu):
        MainMenu.setObjectName("MainMenu")
        MainMenu.resize(900, 500)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Policy.Maximum, QtWidgets.QSizePolicy.Policy.Maximum)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(MainMenu.sizePolicy().hasHeightForWidth())
        MainMenu.setSizePolicy(sizePolicy)
        MainMenu.setMinimumSize(QtCore.QSize(900, 400))
        MainMenu.setMaximumSize(QtCore.QSize(900, 500))
        self.centralwidget = QtWidgets.QWidget(parent=MainMenu)
        self.centralwidget.setObjectName("centralwidget")
        self.verticalLayoutWidget = QtWidgets.QWidget(parent=self.centralwidget)
        self.verticalLayoutWidget.setGeometry(QtCore.QRect(10, 10, 923, 471))
        self.verticalLayoutWidget.setObjectName("verticalLayoutWidget")
        self.base_container = QtWidgets.QVBoxLayout(self.verticalLayoutWidget)
        self.base_container.setSizeConstraint(QtWidgets.QLayout.SizeConstraint.SetMinimumSize)
        self.base_container.setContentsMargins(20, 20, 20, 20)
        self.base_container.setSpacing(20)
        self.base_container.setObjectName("base_container")
        self.primary_containter = QtWidgets.QVBoxLayout()
        self.primary_containter.setSizeConstraint(QtWidgets.QLayout.SizeConstraint.SetMaximumSize)
        self.primary_containter.setObjectName("primary_containter")
        self.logo_container = QtWidgets.QHBoxLayout()
        self.logo_container.setSizeConstraint(QtWidgets.QLayout.SizeConstraint.SetMinimumSize)
        self.logo_container.setContentsMargins(20, 20, 20, 20)
        self.logo_container.setSpacing(20)
        self.logo_container.setObjectName("logo_container")
        self.logo_label = QtWidgets.QLabel(parent=self.verticalLayoutWidget)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Policy.Minimum, QtWidgets.QSizePolicy.Policy.Minimum)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.logo_label.sizePolicy().hasHeightForWidth())
        self.logo_label.setSizePolicy(sizePolicy)
        self.logo_label.setMinimumSize(QtCore.QSize(100, 100))
        self.logo_label.setMaximumSize(QtCore.QSize(300, 300))
        self.logo_label.setObjectName("logo_label")
        self.logo_container.addWidget(self.logo_label)
        self.primary_containter.addLayout(self.logo_container)
        self.h_btn_layout = QtWidgets.QHBoxLayout()
        self.h_btn_layout.setSizeConstraint(QtWidgets.QLayout.SizeConstraint.SetMinimumSize)
        self.h_btn_layout.setContentsMargins(20, 20, 20, 20)
        self.h_btn_layout.setSpacing(20)
        self.h_btn_layout.setObjectName("h_btn_layout")
        self.add_activity_m = QtWidgets.QPushButton(parent=self.verticalLayoutWidget)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Policy.Maximum, QtWidgets.QSizePolicy.Policy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.add_activity_m.sizePolicy().hasHeightForWidth())
        self.add_activity_m.setSizePolicy(sizePolicy)
        self.add_activity_m.setMinimumSize(QtCore.QSize(200, 30))
        self.add_activity_m.setMaximumSize(QtCore.QSize(200, 30))
        self.add_activity_m.setObjectName("add_activity_m")
        self.h_btn_layout.addWidget(self.add_activity_m)
        self.view_stack_m = QtWidgets.QPushButton(parent=self.verticalLayoutWidget)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Policy.Maximum, QtWidgets.QSizePolicy.Policy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.view_stack_m.sizePolicy().hasHeightForWidth())
        self.view_stack_m.setSizePolicy(sizePolicy)
        self.view_stack_m.setMinimumSize(QtCore.QSize(200, 30))
        self.view_stack_m.setMaximumSize(QtCore.QSize(200, 30))
        self.view_stack_m.setObjectName("view_stack_m")
        self.h_btn_layout.addWidget(self.view_stack_m)
        self.more = QtWidgets.QPushButton(parent=self.verticalLayoutWidget)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Policy.Maximum, QtWidgets.QSizePolicy.Policy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.more.sizePolicy().hasHeightForWidth())
        self.more.setSizePolicy(sizePolicy)
        self.more.setMinimumSize(QtCore.QSize(200, 30))
        self.more.setMaximumSize(QtCore.QSize(200, 30))
        self.more.setObjectName("more")
        self.h_btn_layout.addWidget(self.more)
        self.primary_containter.addLayout(self.h_btn_layout)
        self.base_container.addLayout(self.primary_containter)
        MainMenu.setCentralWidget(self.centralwidget)

        self.retranslateUi(MainMenu)
        QtCore.QMetaObject.connectSlotsByName(MainMenu)

    def retranslateUi(self, MainMenu):
        _translate = QtCore.QCoreApplication.translate
        MainMenu.setWindowTitle(_translate("MainMenu", "Time Stack"))
        self.logo_label.setText(_translate("MainMenu", "LoGo"))
        self.add_activity_m.setText(_translate("MainMenu", "Add Activity(Stack)"))
        self.view_stack_m.setText(_translate("MainMenu", "Stack Space"))
        self.more.setText(_translate("MainMenu", "More.."))


if __name__ == "__main__":
    import sys
    app = QtWidgets.QApplication(sys.argv)
    MainMenu = QtWidgets.QMainWindow()
    ui = Ui_MainMenu()
    ui.setupUi(MainMenu)
    MainMenu.show()
    sys.exit(app.exec())