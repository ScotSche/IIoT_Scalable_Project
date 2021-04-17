package Business.Serializer

import Robots.MQTTData
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import org.apache.kafka.common.serialization.Serializer

import java.util

class MessageSerializer extends Serializer[MQTTData]{
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
  }

  override def serialize(topic: String, data: MQTTData): Array[Byte] = {
    if(data == null){
      null
    }
    else{
      val objectMapper: ObjectMapper with ScalaObjectMapper = new ObjectMapper() with ScalaObjectMapper
      objectMapper.registerModule(DefaultScalaModule)
      println("Serialized: " + objectMapper.writeValueAsString(data))
      objectMapper.writeValueAsString(data).getBytes
    }
  }
}
