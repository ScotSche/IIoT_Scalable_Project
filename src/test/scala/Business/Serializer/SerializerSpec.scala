package Business.Serializer

import Robots.RobotPosition
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

      "Serializer should be initialized" in {
        targetSerializer.configure(null, true)
      }
      "Serializer should return null if data is null" in {
        val result = targetSerializer.serialize("Topic", null)
        result should be(null)
      }

      "Deserializer should be initialized" in {
        targetDeserializer.configure(null, true)
      }

      "serialize specific data and deserialize it again" in {
        //val result = targetDeserializer.deserialize("Topic", targetSerializer.serialize("Topic", serializedTarget))
        //result should be(serializedTarget)
      }
    }
  }
}
