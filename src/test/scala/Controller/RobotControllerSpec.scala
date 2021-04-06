package Controller

import Model.Robot.RobotPosition
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RobotControllerSpec extends AnyWordSpec with Matchers {
  "A robot controller" when {
    "initialized" should {
      val target = new RobotController

      "should save a robot image" in {
        target.robotImage should not be(null)
      }
      "should have a manual robot" in {
        target.manual_Robot should not be(null)
      }
      "should have an old manual robot position" in {
        target.manual_Robot_Position_OLD should be(RobotPosition(50, 50, null))
      }
      "should have a list of 4 autonomous robots" in {
        target.autonomousRobots.size should be(4)
      }

      "should be able to update the manual robot position vertically" in {
        target.updateManualSteeringRobotPosition(EventEnumeration.UP, 25)
        target.manual_Robot.currentPosition.y should be(75)
      }
      "should be able to update the manual robot position horizontally" in {
        target.updateManualSteeringRobotPosition(EventEnumeration.LEFT, 25)
        target.manual_Robot.currentPosition.x should be(75)
      }
    }
  }
}
