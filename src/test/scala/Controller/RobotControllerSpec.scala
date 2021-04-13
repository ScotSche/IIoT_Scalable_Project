package Controller

import Factory.Controller.RobotController
import General.Model.Robot.RobotPosition
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RobotControllerSpec extends AnyWordSpec with Matchers {
  "A robot controller" when {
    "initialized" should {
      val target = new RobotController
    }
  }
}
