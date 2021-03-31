package View

import Controller.{EventEnumeration, RobotController}
import _root_.Controller.EventEnumeration.EventEnumeration
import Model.MQTT_Robot_Client
import java.awt.{BasicStroke, Color, Image}
import javax.swing.ImageIcon
import scala.swing.{BoxPanel, Component, Dimension, Graphics2D, MainFrame, Orientation}
import scala.swing.event.{Event, Key, KeyPressed}

case class MoveEvent(event: EventEnumeration) extends Event

class Robot(controller: RobotController) extends MainFrame {
  title = "Robotic Factory #1"
  preferredSize = new Dimension(1000, 600)

  val robot = new MQTT_Robot_Client()
  val canvas = new RobotCanvas(controller)

  contents = new BoxPanel(Orientation.Vertical) {
    contents += canvas
  }

  listenTo(canvas)
  reactions += {
    case MoveEvent(event: EventEnumeration) => {
      if(event == EventEnumeration.UP) controller.updateManualSteeringRobotPosition(event, -25)
      else if(event == EventEnumeration.DOWN) controller.updateManualSteeringRobotPosition(event, 25)
      else if(event == EventEnumeration.LEFT) controller.updateManualSteeringRobotPosition(event, -25)
      else if(event == EventEnumeration.RIGHT) controller.updateManualSteeringRobotPosition(event, 25)
      canvas.repaint()
    }

  }
}

class RobotCanvas(controller: RobotController) extends Component {

  focusable = true

  var robotImage = new ImageIcon("src/images/robotimage.png").getImage()
    .getScaledInstance(50, 50, Image.SCALE_DEFAULT)

  listenTo(keys)
  reactions += {
    case KeyPressed(_, pressedKey, _, _) =>
      if(pressedKey == Key.Up) publish(MoveEvent(EventEnumeration.UP))
      else if(pressedKey == Key.Down) publish(MoveEvent(EventEnumeration.DOWN))
      else if(pressedKey == Key.Left) publish(MoveEvent(EventEnumeration.LEFT))
      else if(pressedKey == Key.Right) publish(MoveEvent(EventEnumeration.RIGHT))
  }
  override def paintComponent(g : Graphics2D) {
    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
      java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
    g.setColor(Color.lightGray);
    val d = size
    g.fillRect(0,0, d.width, d.height)

    g.setStroke(new BasicStroke(3f))

    //  Manual robot drawing
    g.drawImage(robotImage, controller.manualSteeringRobotPosition.x, controller.manualSteeringRobotPosition.y, null)
  }
}