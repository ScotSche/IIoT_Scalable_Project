package View

import Model.Robot.RobotPosition

import java.awt.{BasicStroke, Color}
import java.awt.geom.Ellipse2D
import java.time.LocalDateTime
import java.time._
import java.time.temporal.ChronoUnit
import scala.concurrent.duration.Duration
import scala.swing.{BoxPanel, Component, Dimension, Graphics2D, MainFrame, Orientation}

class FactoryDashboard extends MainFrame {

  title = "Robotic Factory #2"
  preferredSize = new Dimension(1000, 600)

  val canvas = new DashboardCanvas()

  contents = new BoxPanel(Orientation.Vertical) {
    contents += canvas
  }
}

class DashboardCanvas() extends Component {

  focusable = true

  var _robotPositions = Map[String, RobotPosition]()

  def updateRobots(robotMap: Map[String, RobotPosition]): Unit = {
    _robotPositions = robotMap
    this.repaint()
  }

  override def paintComponent(g : Graphics2D) {
    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
      java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
    g.setColor(Color.lightGray);
    val d = size
    g.fillRect(0,0, d.width, d.height)

    g.setStroke(new BasicStroke(3f))

    _robotPositions.foreach{ case (key, value) =>

      val timeDifference = LocalDateTime.parse(value.timeStampISO).until(LocalDateTime.now(), ChronoUnit.SECONDS)
      if(timeDifference >= 5){
        g.setColor(Color.red)
      }
      else{
        g.setColor(Color.green)
      }
      g.draw(new Ellipse2D.Double(value.x, value.y, 50, 50))
    }
  }
}


