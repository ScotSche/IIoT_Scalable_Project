package Controller

import Factory.Controller.FactoryController
import Factory.Model.Robot.RobotPosition
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FactoryControllerSpec extends AnyWordSpec with Matchers {
  "A robot controller" when {
    "initialized" should {
      val target = new FactoryController
    }
  }
}
