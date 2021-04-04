package Model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RobotPositionSpec extends AnyWordSpec with Matchers {
  "A Robot position" when {
    "when initialized" should {
      val target = RobotPosition(50, 50)

      "have the position 50, 50" in {
        target.x should be(50)
        target.y should be(50)
      }
    }
  }
}
