package Model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import javax.swing.ImageIcon

class RobotSpec extends AnyWordSpec with Matchers{
  "A Robot" when {
    "new initialized" should {

      val target = Robot(new ImageIcon("src/images/robotimage.png").getImage(),
        RobotPosition(0, 0), RobotPosition(100, 100), RobotPosition(50, 50))

      "have initial position 50, 50" in {
        target.position should be(RobotPosition(50, 50))
      }

      "have min position 0, 0" in {
        target.minPosition should be(RobotPosition(0, 0))
      }

      "have max position 100, 100" in {
        target.maxPosition should be(RobotPosition(100, 100))
      }

      "change position if new position is valid" in {
        target.changePosition(RobotPosition(75, 75))
        target.position should be(RobotPosition(75, 75))
      }

      "do not change position if new position is invalid" in {
        target.changePosition(RobotPosition(150, 150))
        target.position should be(RobotPosition(75, 75))
      }
    }
  }
}
