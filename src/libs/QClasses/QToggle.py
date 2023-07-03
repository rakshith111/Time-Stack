from PyQt6.QtWidgets import QCheckBox
from PyQt6.QtGui import QColor, QBrush, QPaintEvent, QPen, QPainter
from PyQt6.QtCore import Qt, QSize, QPoint, QPointF, QRectF, QEasingCurve, QPropertyAnimation, QSequentialAnimationGroup, pyqtSlot, pyqtProperty
# credits to https://www.pythonguis.com/tutorials/pyqt6-animated-widgets/ 
class AnimatedToggle(QCheckBox):

    _transparent_pen = QPen(Qt.GlobalColor.transparent)
    _light_grey_pen = QPen(Qt.GlobalColor.lightGray)

    def __init__(self, parent=None):
        super().__init__(parent)
        self._bar_color = Qt.GlobalColor.lightGray
        self._handle_color = Qt.GlobalColor.white
        self._checked_color = "#007bff"     
        self._pulse_unchecked_color = "#AFAFAF"  
        self._pulse_checked_color = "#AFAFAF"

        self._bar_brush = QBrush(self._bar_color)
        self._bar_checked_brush = QBrush(QColor(self._checked_color).lighter())
        self._handle_brush = QBrush(self._handle_color)
        self._handle_checked_brush = QBrush(QColor(self._checked_color))
        self._pulse_unchecked_animation = QBrush(QColor(self._pulse_unchecked_color))
        self._pulse_checked_animation = QBrush(QColor(self._pulse_checked_color))

        self.setContentsMargins(8, 0, 8, 0)
        self._handle_position = 0
        self._pulse_radius = 0

        self.animation = QPropertyAnimation(self, b"handle_position", self)
        self.animation.setEasingCurve(QEasingCurve.Type.InOutCubic)
        self.animation.setDuration(200)

        self.pulse_anim = QPropertyAnimation(self, b"pulse_radius", self)
        self.pulse_anim.setDuration(350)
        self.pulse_anim.setStartValue(10)
        self.pulse_anim.setEndValue(20)

        self.animations_group = QSequentialAnimationGroup()
        self.animations_group.addAnimation(self.animation)
        self.animations_group.addAnimation(self.pulse_anim)

        self.stateChanged.connect(self.setup_animation)

    def sizeHint(self):
        return QSize(58, 45)

    def hitButton(self, pos: QPoint):
        return self.contentsRect().contains(pos)

    @pyqtSlot(int)
    def setup_animation(self, value):
        self.animations_group.stop()
        if value:
            self.animation.setEndValue(1)
        else:
            self.animation.setEndValue(0)
        self.animations_group.start()

    def paintEvent(self, e: QPaintEvent):
        contRect = self.contentsRect()
        handleRadius = round(0.24 * contRect.height())

        p = QPainter(self)
        p.setRenderHint(QPainter.RenderHint.Antialiasing)

        p.setPen(self._transparent_pen)
        barRect = QRectF(
            0, 0,
            contRect.width() - handleRadius, 0.40 * contRect.height()
        )
        barRect.moveCenter(QPointF(contRect.center()))
        rounding = barRect.height() / 2

        trailLength = contRect.width() - 2 * handleRadius

        xPos = contRect.x() + handleRadius + trailLength * self._handle_position

        if self.pulse_anim.state() == QPropertyAnimation.State.Running:
            p.setBrush(
                self._pulse_checked_animation if
                self.isChecked() else self._pulse_unchecked_animation)
            p.drawEllipse(QPointF(xPos, barRect.center().y()),
                          self._pulse_radius, self._pulse_radius)

        if self.isChecked():
            p.setBrush(self._bar_checked_brush)
            p.drawRoundedRect(barRect, rounding, rounding)
            p.setBrush(self._handle_checked_brush)
        else:
            p.setBrush(self._bar_brush)
            p.drawRoundedRect(barRect, rounding, rounding)
            p.setPen(self._light_grey_pen)
            p.setBrush(self._handle_brush)

        p.drawEllipse(
            QPointF(xPos, barRect.center().y()),
            handleRadius, handleRadius)

        p.end()

    # Setter methods for color properties
    def setBarColor(self, color):
        self._bar_color = color
        self._bar_brush = QBrush(color)
        self.update()

    def setHandleColor(self, color):
        self._handle_color = color
        self._handle_brush = QBrush(color)
        self.update()

    def setCheckedColor(self, color):
       
        self._checked_color = color
        self._bar_checked_brush = QBrush(QColor(color).lighter())
        self._handle_checked_brush = QBrush(QColor(color))
        self.update()

    def setPulseUncheckedColor(self, color):
        self._pulse_unchecked_color = color
        self._pulse_unchecked_animation = QBrush(QColor(color))
        self.update()

    def setPulseCheckedColor(self, color):
        self._pulse_checked_color = color
        self._pulse_checked_animation = QBrush(QColor(color))
        self.update()

    @pyqtProperty(float)
    def handle_position(self):
        return self._handle_position

    @handle_position.setter
    def handle_position(self, pos):
        self._handle_position = pos
        self.update()

    @pyqtProperty(float)
    def pulse_radius(self):
        return self._pulse_radius

    @pulse_radius.setter
    def pulse_radius(self, pos):
        self._pulse_radius = pos
        self.update()
