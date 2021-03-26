package View

import Model.MQTT_Robot_Client
import View.EventEnumeration.EventEnumeration

import java.awt.{BasicStroke, Color}
import java.awt.geom.Ellipse2D
import scala.swing.{BoxPanel, Component, Dimension, Graphics2D, MainFrame, Orientation}
import scala.swing.event.{Event, Key, KeyPressed}

object EventEnumeration extends Enumeration {
  type EventEnumeration = Value
  val UP, DOWN, LEFT, RIGHT = Value
}

case class MoveEvent(event: EventEnumeration) extends Event

case class RobotPosition(var x:Int = 50, var y:Int = 50)

class RobotCanvas(robotPosition: RobotPosition) extends Component {

  focusable = true
  preferredSize = new Dimension(1000, 600)

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

    g.setColor(Color.darkGray)
    g.draw(new Ellipse2D.Double(robotPosition.x, robotPosition.y, 50, 50))

  }
}

class Robot(robotPosition: RobotPosition) extends MainFrame {
  title = "Robotic Factory #1"

  val robot = new MQTT_Robot_Client()
  val canvas = new RobotCanvas(robotPosition)

  contents = new BoxPanel(Orientation.Vertical) {
    contents += canvas
  }

  listenTo(canvas)
  reactions += {
    case MoveEvent(event: EventEnumeration) => {

      if(event == EventEnumeration.UP) robotPosition.y -= 25
      else if(event == EventEnumeration.DOWN) robotPosition.y += 25
      else if(event == EventEnumeration.LEFT) robotPosition.x -= 25
      else if(event == EventEnumeration.RIGHT) robotPosition.x += 25
      robot.publish("X: " + robotPosition.x + " / " + robotPosition.y)
      canvas.repaint()
    }

  }
}

object Robot {
  def main(args: Array[String]): Unit = {
    val robotPosition = RobotPosition()
    val ui = new Robot(robotPosition)
    ui.visible = true
  }
}

