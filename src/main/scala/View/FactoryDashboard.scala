package View

import java.awt.{BasicStroke, Color}
import java.awt.geom.Ellipse2D
import scala.swing.event.{Key, KeyPressed}
import scala.swing.{BoxPanel, Component, Dimension, Graphics2D, MainFrame, Orientation}

object FactoryDashboard {
    def main(args: Array[String]): Unit = {
      val ui = new Dashboard()
      ui.visible = true
    }
}

class Dashboard extends MainFrame {
  title = "Robotic Factory #2"

  val canvas = new DashboardCanvas()

  contents = new BoxPanel(Orientation.Vertical) {
    contents += canvas
  }
}

class DashboardCanvas extends Component {

  focusable = true
  preferredSize = new Dimension(1000, 600)

  override def paintComponent(g : Graphics2D) {
    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
      java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
    g.setColor(Color.lightGray);
    val d = size
    g.fillRect(0,0, d.width, d.height)

    g.setStroke(new BasicStroke(3f))

    g.setColor(Color.darkGray)
    g.draw(new Ellipse2D.Double(50, 50, 50, 50))
  }
}


