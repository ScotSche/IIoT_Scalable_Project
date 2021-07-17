package Factory.View

import Business.{EventEnumeration, Timer}
import Factory.Controller.FactoryController
import EventEnumeration.EventEnumeration
import Factory.Model.Robot.{AutonomousRobot, RobotPosition}

import java.awt.{BasicStroke, Image}
import javax.swing.ImageIcon
import scala.swing._
import scala.swing.event.{ButtonClicked, Event, Key, KeyPressed}

case class MoveEvent(event: EventEnumeration) extends Event
case class ClickEvent(mode: Boolean) extends Event

class FactoryRobot(controller: FactoryController) extends MainFrame {
  title = "Robotic Factory #1"

  preferredSize = new Dimension(900, 900)

  val canvas = new RobotCanvas(controller)
  val autonomousRadioButton = new RadioButton("Autonomous Mode")
  autonomousRadioButton.selected = true
  autonomousRadioButton.focusable = false
  val manualRadioButton = new RadioButton("Manual Mode")
  manualRadioButton.focusable = false
  val modeGroup = new ButtonGroup(autonomousRadioButton, manualRadioButton)

  contents = new BoxPanel(Orientation.Vertical) {
    contents += canvas
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += manualRadioButton
      contents += autonomousRadioButton
    }
  }

  listenTo(canvas, autonomousRadioButton, manualRadioButton)
  reactions += {
    case MoveEvent(event: EventEnumeration) => {
      if(event == EventEnumeration.UP) controller.updateManualSteeringRobotPosition(event, -25)
      else if(event == EventEnumeration.DOWN) controller.updateManualSteeringRobotPosition(event, 25)
      else if(event == EventEnumeration.LEFT) controller.updateManualSteeringRobotPosition(event, -25)
      else if(event == EventEnumeration.RIGHT) controller.updateManualSteeringRobotPosition(event, 25)
      canvas.repaint()
    }
    case ButtonClicked(button) => {
      if(button.equals(manualRadioButton)){
        controller.mode = true
      }
      else {
        controller.mode = false
      }
      canvas.repaint()
    }
  }
  Timer(1000){
    canvas.repaint()
  }
}

class RobotCanvas(controller: FactoryController) extends Component {

  focusable = true
  val factoryImage = new ImageIcon("src/images/Lagerhalle_real.png").getImage()
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
    if(controller.mode) {
      g.drawImage(controller.robotImageFull, controller.manual_Robot_Position.x - 25, controller.manual_Robot_Position.y - 25, null)
    }
    else {
      // Autonomous robots drawing
      controller.autonomousRobots.foreach{
        autonomousRobot: (AutonomousRobot, RobotPosition) => {
          var image = new ImageIcon().getImage
          if (autonomousRobot._1.vertical) {
            if (autonomousRobot._1.reverseMovement) {
              image = controller.robotImageEmpty
            }
            else {
              image = controller.robotImageFull
            }
          }
          else {
            if (autonomousRobot._1.reverseMovement) {
              image = controller.robotImageEmpty
            }
            else {
              image = controller.robotImageFull
            }
          }
          g.drawImage(image, autonomousRobot._2.x - 25, autonomousRobot._2.y - 25, null)
        }
      }
    }
  }
}