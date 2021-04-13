package Model.Serializer

import Model.Robot.RobotPosition
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import org.apache.kafka.common.serialization.Deserializer

import java.util
import scala.util.{Failure, Success, Try}

class MessageDeserializer extends Deserializer[RobotPosition]{
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
  }

  override def deserialize(topic: String, data: Array[Byte]): RobotPosition = {
    lazy val mapper: ObjectMapper with ScalaObjectMapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val tryMessage: Try[RobotPosition] = Try(mapper.readValue[RobotPosition](data))
    var message: RobotPosition = RobotPosition(-1, -1, "")
    tryMessage match {
      case Success(value: RobotPosition) => message = value
    }
    message
  }
}
