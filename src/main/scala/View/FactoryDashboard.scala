package View

import Model.RobotPosition

import java.awt.{BasicStroke, Color}
import java.awt.geom.Ellipse2D
import scala.collection.mutable
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

    g.setColor(Color.red)

    _robotPositions.foreach{ case (key, value) => g.draw(new Ellipse2D.Double(value.x, value.y, 50, 50)) }
  }
}


