package Model.Serializer

import Model.Robot.RobotPosition
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serdes, Serializer}

class CustomSerde extends Serde[RobotPosition]{
  override def deserializer(): Deserializer[RobotPosition] = new MessageDeserializer
  def configure(configs: Map[String, _], isKey: Boolean): Unit = ()
  override def close(): Unit = ()
  override def serializer(): Serializer[RobotPosition] = new MessageSerializer
}