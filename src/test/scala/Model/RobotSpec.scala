package Model

import Model.Robot.Robot
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.time.LocalDateTime
import javax.swing.ImageIcon

class RobotSpec extends AnyWordSpec with Matchers{
  "A Robot" when {
    "new initialized" should {

      /*val target = Robot("Robot_Name", new ImageIcon("src/images/robotimage.png").getImage(),
        RobotPosition(0, 0, null), RobotPosition(100, 100, null), false, RobotPosition(50, 50, null))

      "have the name Robot_Name" in {
        target.name should be("Robot_Name")
      }

      "have initial position 50, 50" in {
        target.position should be(RobotPosition(50, 50, null))
      }

      "have min position 0, 0" in {
        target.minPosition should be(RobotPosition(0, 0, null))
      }

      "have max position 100, 100" in {
        target.maxPosition should be(RobotPosition(100, 100, null))
      }

      "change X position if new X position is valid" in {
        target.changeXPosition(25, LocalDateTime.now())
        target.position.x should be(75)
      }

      "do not change X position if new X position is invalid" in {
        target.changeXPosition(100, LocalDateTime.now())
        target.position.x should be(75)
      }

      "change Y position if new Y position is valid" in {
        target.changeYPosition(25, LocalDateTime.now())
        target.position.y should be(75)
      }

      "do not change Y position if new Y position is invalid" in {
        target.changeYPosition(100, LocalDateTime.now())
        target.position.y should be(75)
      }*/
    }
  }
}