package Business.Serializer

import Robots.MQTTData
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import org.apache.kafka.common.serialization.Deserializer

import java.util
import scala.util.{Success, Try}

class MessageDeserializer extends Deserializer[MQTTData]{
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
  }

  override def deserialize(topic: String, data: Array[Byte]): MQTTData = {
    lazy val mapper: ObjectMapper with ScalaObjectMapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val tryMessage: Try[MQTTData] = Try(mapper.readValue[MQTTData](data))
    var message: MQTTData = MQTTData("", (-1, -1), "", "")
    tryMessage match {
      case Success(value: MQTTData) => message = value
    }
    message
  }
}
