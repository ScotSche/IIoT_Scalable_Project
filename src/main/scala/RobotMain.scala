import Controller.RobotController
import View.Robot

object RobotMain {

  //  Controller
  val controller: RobotController = new RobotController()

  //  Views
  val robotView: Robot = new Robot(controller)

  def main(args: Array[String]) = {
    robotView.visible = true
  }
}
