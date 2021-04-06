package Model

import java.util
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import org.apache.kafka.common.serialization.Deserializer
import scala.util.{Failure, Success, Try}

class MessageDeserializer extends Deserializer[RobotPosition]{
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
  }

  override def deserialize(topic: String, data: Array[Byte]): RobotPosition = {
    lazy val mapper: ObjectMapper with ScalaObjectMapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val tryMessage: Try[RobotPosition] = Try(mapper.readValue[RobotPosition](data))
    println("tryMessage: " + tryMessage)
    var message: RobotPosition = RobotPosition(0, 0, null)
    tryMessage match {
      case Success(value: RobotPosition) => message = value
      case Failure(_) =>
    }
    message
  }
}
