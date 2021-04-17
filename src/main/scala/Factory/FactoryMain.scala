package Factory

import Factory.Controller.FactoryController
import View.FactoryRobot

object FactoryMain {

  //  Controller
  val controller: FactoryController = new FactoryController()

  //  Views
  val robotView: FactoryRobot = new FactoryRobot(controller)

  //  Main loop
  def main(args: Array[String]) = {
    robotView.visible = true
  }
}
