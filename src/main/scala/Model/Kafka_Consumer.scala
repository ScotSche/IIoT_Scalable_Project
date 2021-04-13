package Model

import Model.Robot.RobotPosition
import org.apache.kafka.clients.consumer.KafkaConsumer

import java.util.Properties
import java.util

class Kafka_Consumer {

  //  https://kafka.apache.org/27/documentation/streams/tutorial

  val  props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "Model.Serializer.MessageDeserializer")
  props.put("group.id", "something")

  val consumer: KafkaConsumer[String, RobotPosition] = new KafkaConsumer[String, RobotPosition](props)
  val TOPIC_METADATA: String = "robot_positioning"

  consumer.subscribe(util.Collections.singletonList(TOPIC_METADATA))
}