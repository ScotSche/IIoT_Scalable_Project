package Model.Serializer

import Model.Robot.RobotPosition
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.time.LocalDateTime

class SerializerSpec extends AnyWordSpec with Matchers {
  "A Serializer" when {
    "working" should {
      val targetSerializer = new MessageSerializer
      val targetDeserializer = new MessageDeserializer

      val timestampISO = LocalDateTime.now().toString
      val serializedTarget = RobotPosition(50, 50, timestampISO)

      "serialize specific data and deserialize it again" in {
        val result = targetDeserializer.deserialize("Topic", targetSerializer.serialize("Topic", serializedTarget))
        result should be(serializedTarget)
      }
    }
  }
}
