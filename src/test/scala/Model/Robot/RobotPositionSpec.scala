package Model.Robot

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.time.LocalDateTime

class RobotPositionSpec extends AnyWordSpec with Matchers {
  "A Robot position" when {
    "when initialized" should {
      val timestamp = LocalDateTime.now().toString
      val target = RobotPosition(50, 50, timestamp)

      "have the position 50, 50" in {
        target.x should be(50)
        target.y should be(50)
      }
      "have the timestamp 'as in timestamp'" in {
        target.timeStampISO should be(timestamp)
      }
    }
  }
}
