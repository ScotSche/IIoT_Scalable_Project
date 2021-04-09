import Controller.RobotController
import View.FactoryRobot

object RobotMain {

  //  Controller
  val controller: RobotController = new RobotController()

  //  Views
  val robotView: FactoryRobot = new FactoryRobot(controller)

  //  Main loop
  def main(args: Array[String]) = {
    robotView.visible = true
  }
}