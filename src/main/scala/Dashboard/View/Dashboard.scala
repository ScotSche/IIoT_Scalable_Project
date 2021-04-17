package Dashboard.View

import Business.{MQTTData, Timer}

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

  var _robotPositions = Map[String, MQTTData]()

  def updateRobots(robotMap: Map[String, MQTTData]): Unit = {
    _robotPositions ++= robotMap
    this.repaint()
  }

  /*Timer(1000){
    this.repaint()
  }*/

  override def paintComponent(g : Graphics2D) {
    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
      java.awt.RenderingHints.VALUE_ANTIALIAS_ON)

    val d = size
    g.drawImage(factoryImage, (d.width - 800) / 2, (d.height - 800) / 2, null)

    g.setStroke(new BasicStroke(3f))

    evaluateTimeStamps()
    _robotPositions.foreach{ case (key, value) =>
      g.draw(new Ellipse2D.Double(value.position._1 - 25, value.position._2 - 25, 50, 50))
      drawTriangulation(g, value);
    }
  }
  def evaluateTimeStamps(): Unit = {
    val timestamp = LocalDateTime.now()
    _robotPositions.toList.foreach(robotData => {
      if(robotData._2.timestamp != "null") {
        val robotTimestamp = LocalDateTime.parse(robotData._2.timestamp)
        val difference = robotTimestamp.until(timestamp, ChronoUnit.SECONDS)
        if (difference > 2) _robotPositions = _robotPositions.-(robotData._1)
      }
    })
  }

  def drawTriangulation(g : Graphics2D, data:MQTTData): Unit = {
    val triangulationArray = data.triangulation.split(";").toList
    if(triangulationArray.size > 1) {
      val triangulation = triangulationArray.map(x => x.split(":").toList match {
        case List(a, b) => (stringToTuple(a), b.toDouble)
      })
      triangulation.foreach(stationData => {
        val radius = stationData._2
        val diameter = radius * 2
        g.setColor(Color.red)
        g.draw(new Ellipse2D.Double(stationData._1._1 - radius, stationData._1._2 - radius, diameter, diameter))
      })
    }
  }
  def stringToTuple(tuple: String): (Int, Int) = {
    tuple.substring(1).substring(0, tuple.length - 2).split(",").toList match {
      case List(a, b) => (a.toInt, b.toInt)
    }
  }
}


