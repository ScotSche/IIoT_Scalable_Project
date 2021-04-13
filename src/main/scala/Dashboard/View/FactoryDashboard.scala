package Dashboard.View

import General.Model.Robot.RobotPosition
import General.Model.Timer

import java.awt.geom.Ellipse2D
import java.awt.{BasicStroke, Color, Image}
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.swing.ImageIcon
import scala.swing._

class FactoryDashboard extends MainFrame {

  title = "Robotic Factory #2"
  preferredSize = new Dimension(900, 900)

  val canvas = new DashboardCanvas()

  contents = new BoxPanel(Orientation.Vertical) {
    contents += canvas
  }
}

class DashboardCanvas() extends Component {

  focusable = true

  val factoryImage = new ImageIcon("src/images/factoryimage.jpeg").getImage()
    .getScaledInstance(800, 800, Image.SCALE_DEFAULT)

  var _robotPositions = Map[String, RobotPosition]()

  def updateRobots(robotMap: Map[String, RobotPosition]): Unit = {
    _robotPositions = robotMap
    this.repaint()
  }

  Timer(1000){
    this.repaint()
  }

  override def paintComponent(g : Graphics2D) {
    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
      java.awt.RenderingHints.VALUE_ANTIALIAS_ON)

    val d = size
    g.drawImage(factoryImage, (d.width - 800) / 2, (d.height - 800) / 2, null)

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


