# Form implementation generated from reading ui file 'd:\gitprojs\new-git\Time-Stack\src\ui_files\Stack_gen.ui'
#
# Created by: PyQt6 UI code generator 6.4.1
#
# WARNING: Any manual changes made to this file will be lost when pyuic6 is
# run again.  Do not edit this file unless you know what you are doing.


from PyQt6 import QtCore, QtGui, QtWidgets


class Ui_stack_gen(object):
    def setupUi(self, stack_gen):
        stack_gen.setObjectName("stack_gen")
        stack_gen.resize(373, 525)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Policy.Fixed, QtWidgets.QSizePolicy.Policy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(stack_gen.sizePolicy().hasHeightForWidth())
        stack_gen.setSizePolicy(sizePolicy)
        stack_gen.setMinimumSize(QtCore.QSize(373, 525))
        stack_gen.setMaximumSize(QtCore.QSize(373, 525))
        self.verticalLayoutWidget = QtWidgets.QWidget(parent=stack_gen)
        self.verticalLayoutWidget.setGeometry(QtCore.QRect(10, 10, 351, 501))
        self.verticalLayoutWidget.setObjectName("verticalLayoutWidget")
        self.content_v = QtWidgets.QVBoxLayout(self.verticalLayoutWidget)
        self.content_v.setSizeConstraint(QtWidgets.QLayout.SizeConstraint.SetMinimumSize)
        self.content_v.setContentsMargins(20, 20, 20, 20)
        self.content_v.setSpacing(10)
        self.content_v.setObjectName("content_v")
        self.name_layout_h = QtWidgets.QHBoxLayout()
        self.name_layout_h.setSizeConstraint(QtWidgets.QLayout.SizeConstraint.SetMinimumSize)
        self.name_layout_h.setContentsMargins(20, 20, 20, 20)
        self.name_layout_h.setSpacing(20)
        self.name_layout_h.setObjectName("name_layout_h")
        self.name_label = QtWidgets.QLabel(parent=self.verticalLayoutWidget)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Policy.Maximum, QtWidgets.QSizePolicy.Policy.Preferred)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.name_label.sizePolicy().hasHeightForWidth())
        self.name_label.setSizePolicy(sizePolicy)
        self.name_label.setMinimumSize(QtCore.QSize(80, 50))
        self.name_label.setMaximumSize(QtCore.QSize(100, 50))
        self.name_label.setObjectName("name_label")
        self.name_layout_h.addWidget(self.name_label)
        self.stack_name_input = QtWidgets.QTextEdit(parent=self.verticalLayoutWidget)
        self.stack_name_input.setEnabled(True)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Policy.Maximum, QtWidgets.QSizePolicy.Policy.Maximum)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.stack_name_input.sizePolicy().hasHeightForWidth())
        self.stack_name_input.setSizePolicy(sizePolicy)
        self.stack_name_input.setMaximumSize(QtCore.QSize(250, 40))
        self.stack_name_input.setBaseSize(QtCore.QSize(20, 20))
        self.stack_name_input.setVerticalScrollBarPolicy(QtCore.Qt.ScrollBarPolicy.ScrollBarAlwaysOff)
        self.stack_name_input.setObjectName("stack_name_input")
        self.name_layout_h.addWidget(self.stack_name_input)
        self.content_v.addLayout(self.name_layout_h)
        self.time_layout_h = QtWidgets.QHBoxLayout()
        self.time_layout_h.setContentsMargins(20, 20, 20, 20)
        self.time_layout_h.setSpacing(10)
        self.time_layout_h.setObjectName("time_layout_h")
        self.start_label = QtWidgets.QLabel(parent=self.verticalLayoutWidget)
        self.start_label.setObjectName("start_label")
        self.time_layout_h.addWidget(self.start_label)
        self.start_time_input = QtWidgets.QTimeEdit(parent=self.verticalLayoutWidget)
        self.start_time_input.setObjectName("start_time_input")
        self.time_layout_h.addWidget(self.start_time_input)
        self.end_label = QtWidgets.QLabel(parent=self.verticalLayoutWidget)
        self.end_label.setObjectName("end_label")
        self.time_layout_h.addWidget(self.end_label)
        self.end_time_input = QtWidgets.QTimeEdit(parent=self.verticalLayoutWidget)
        self.end_time_input.setObjectName("end_time_input")
        self.time_layout_h.addWidget(self.end_time_input)
        self.content_v.addLayout(self.time_layout_h)
        self.total_time_layout_h = QtWidgets.QHBoxLayout()
        self.total_time_layout_h.setSizeConstraint(QtWidgets.QLayout.SizeConstraint.SetMinimumSize)
        self.total_time_layout_h.setContentsMargins(20, 20, 20, 20)
        self.total_time_layout_h.setSpacing(20)
        self.total_time_layout_h.setObjectName("total_time_layout_h")
        self.name_label_2 = QtWidgets.QLabel(parent=self.verticalLayoutWidget)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Policy.Maximum, QtWidgets.QSizePolicy.Policy.Preferred)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.name_label_2.sizePolicy().hasHeightForWidth())
        self.name_label_2.setSizePolicy(sizePolicy)
        self.name_label_2.setMinimumSize(QtCore.QSize(80, 50))
        self.name_label_2.setMaximumSize(QtCore.QSize(100, 50))
        self.name_label_2.setObjectName("name_label_2")
        self.total_time_layout_h.addWidget(self.name_label_2)
        self.total_time_output = QtWidgets.QTextEdit(parent=self.verticalLayoutWidget)
        self.total_time_output.setEnabled(True)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Policy.Maximum, QtWidgets.QSizePolicy.Policy.Maximum)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.total_time_output.sizePolicy().hasHeightForWidth())
        self.total_time_output.setSizePolicy(sizePolicy)
        self.total_time_output.setMaximumSize(QtCore.QSize(250, 40))
        self.total_time_output.setBaseSize(QtCore.QSize(20, 20))
        self.total_time_output.setVerticalScrollBarPolicy(QtCore.Qt.ScrollBarPolicy.ScrollBarAlwaysOff)
        self.total_time_output.setTextInteractionFlags(QtCore.Qt.TextInteractionFlag.NoTextInteraction)
        self.total_time_output.setObjectName("total_time_output")
        self.total_time_layout_h.addWidget(self.total_time_output)
        self.content_v.addLayout(self.total_time_layout_h)
        self.create_stack_button = QtWidgets.QPushButton(parent=self.verticalLayoutWidget)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Policy.Maximum, QtWidgets.QSizePolicy.Policy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.create_stack_button.sizePolicy().hasHeightForWidth())
        self.create_stack_button.setSizePolicy(sizePolicy)
        self.create_stack_button.setMinimumSize(QtCore.QSize(200, 30))
        self.create_stack_button.setMaximumSize(QtCore.QSize(200, 30))
        self.create_stack_button.setObjectName("create_stack_button")
        self.content_v.addWidget(self.create_stack_button, 0, QtCore.Qt.AlignmentFlag.AlignHCenter)

        self.retranslateUi(stack_gen)
        QtCore.QMetaObject.connectSlotsByName(stack_gen)

    def retranslateUi(self, stack_gen):
        _translate = QtCore.QCoreApplication.translate
        stack_gen.setWindowTitle(_translate("stack_gen", "Create your stack"))
        self.name_label.setText(_translate("stack_gen", "STACK NAME"))
        self.start_label.setText(_translate("stack_gen", "START"))
        self.end_label.setText(_translate("stack_gen", "END"))
        self.name_label_2.setText(_translate("stack_gen", "Total time:-"))
        self.total_time_output.setHtml(_translate("stack_gen", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n"
"<html><head><meta name=\"qrichtext\" content=\"1\" /><style type=\"text/css\">\n"
"p, li { white-space: pre-wrap; }\n"
"</style></head><body style=\" font-family:\'MS Shell Dlg 2\'; font-size:8.25pt; font-weight:400; font-style:normal;\">\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p></body></html>"))
        self.create_stack_button.setText(_translate("stack_gen", "Create"))


if __name__ == "__main__":
    import sys
    app = QtWidgets.QApplication(sys.argv)
    stack_gen = QtWidgets.QWidget()
    ui = Ui_stack_gen()
    ui.setupUi(stack_gen)
    stack_gen.show()
    sys.exit(app.exec())
