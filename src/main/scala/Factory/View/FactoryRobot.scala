package Factory.View

import Factory.Controller.RobotController
import General.EventEnumeration
import General.Model.Robot.{AutonomousRobot, RobotPosition}
import General.Model.Timer
import General.EventEnumeration.EventEnumeration

import java.awt.{BasicStroke, Image}
import javax.swing.ImageIcon
import scala.swing._
import scala.swing.event.{Event, Key, KeyPressed}

case class MoveEvent(event: EventEnumeration) extends Event

class FactoryRobot(controller: RobotController) extends MainFrame {
  title = "Robotic Factory #1"

  preferredSize = new Dimension(900, 900)

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
  Timer(1000){
    canvas.repaint()
  }
}

class RobotCanvas(controller: RobotController) extends Component {

  focusable = true
  val factoryImage = new ImageIcon("src/images/factoryimage.jpeg").getImage()
    .getScaledInstance(800, 800, Image.SCALE_DEFAULT)

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

    val d = size
    g.drawImage(factoryImage, (d.width - 800) / 2, (d.height - 800) / 2, null)

    g.setStroke(new BasicStroke(3f))

    //  Manual robot drawing
    //g.drawImage(controller.manual_Robot.image_Full, controller.manual_Robot.currentPosition.x, controller.manual_Robot.currentPosition.y, null)
    g.drawImage(controller.robotImageFull, controller.manual_Robot_Position.x, controller.manual_Robot_Position.y, null)

    // Autonomous robots drawing
    controller.autonomousRobots.foreach{
      autonomousRobot: (AutonomousRobot, RobotPosition) => {
        var image = new ImageIcon().getImage
        if(autonomousRobot._1.vertical){
          if(autonomousRobot._1.reverseMovement){
            image = controller.robotImageEmpty
          }
          else{
            image = controller.robotImageFull
          }
        }
        else{
          if(autonomousRobot._1.reverseMovement){
            image = controller.robotImageEmpty
          }
          else{
            image = controller.robotImageFull
          }
        }
        g.drawImage(image, autonomousRobot._2.x, autonomousRobot._2.y, null)
      }
    }
  }
}