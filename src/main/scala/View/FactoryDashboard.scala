package View

import Model.RobotPosition

import java.awt.{BasicStroke, Color}
import java.awt.geom.Ellipse2D
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

  var _robotPosition = RobotPosition(50, 50)

  def updateRobotPositions(robotPosition: RobotPosition): Unit ={
    _robotPosition = robotPosition
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
    g.draw(new Ellipse2D.Double(_robotPosition.x, _robotPosition.y, 50, 50))
  }
}


