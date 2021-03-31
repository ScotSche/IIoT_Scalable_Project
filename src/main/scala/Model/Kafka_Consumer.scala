package Model

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties
import java.util

class Kafka_Consumer {

  val  props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("group.id", "something")

  val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
  val TOPIC_METADATA: String = "robot_positioning"

  consumer.subscribe(util.Collections.singletonList(TOPIC_METADATA))
}
