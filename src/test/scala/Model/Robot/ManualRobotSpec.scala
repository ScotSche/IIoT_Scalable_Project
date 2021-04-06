package Model.Robot

import Model.RobotPosition
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.time.LocalDateTime
import javax.swing.ImageIcon

class ManualRobotSpec extends AnyWordSpec with Matchers {
  "A manual robot" when {
    "new initialized" should {
      val target: ManualRobot = new ManualRobot("Robot Manual", new ImageIcon("src/images/robotimage.png").getImage(),
        RobotPosition(0, 0, null), RobotPosition(100, 100, null), RobotPosition(50, 50, null))

      var timestampValid: LocalDateTime = null

      "have a specific name" in {
        target.name should be("Robot Manual")
      }
      "have a specific min position" in {
        target.minPosition should be(RobotPosition(0, 0, null))
      }
      "have a specific max position" in {
        target.maxPosition should be(RobotPosition(100, 100, null))
      }
      "have a specific current position" in {
        target.currentPosition should be(RobotPosition(50, 50, null))
      }

      "should change the current position if X value is valid" in {
        timestampValid = LocalDateTime.now()
        target.changeXPosition(25, timestampValid)
        target.currentPosition should be(RobotPosition(75, 50, timestampValid.toString))
      }
      "should not change the current position if X value is invalid" in {
        val timeStampISOXInvalid = LocalDateTime.now()
        target.changeXPosition(100, timeStampISOXInvalid)
        target.currentPosition should be(RobotPosition(75, 50, timestampValid.toString))
      }

      "should change the current position if Y value is valid" in {
        timestampValid = LocalDateTime.now()
        target.changeYPosition(25, timestampValid)
        target.currentPosition should be(RobotPosition(75, 75, timestampValid.toString))
      }
      "should not change the current position if Y value is invalid" in {
        val timeStampISOYInvalid = LocalDateTime.now()
        target.changeYPosition(100, timeStampISOYInvalid)
        target.currentPosition should be(RobotPosition(75, 75, timestampValid.toString))
      }

      "should publish a mqtt message" in {
        target.mqtt_publish()
      }
    }
  }
}
