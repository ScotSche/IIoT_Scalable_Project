package Model.Kafka

import Model.Robot.RobotPosition
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}

import java.util
import java.util.Properties
import scala.jdk.CollectionConverters.iterableAsScalaIterableConverter

object Consumer extends App{
  val  props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("group.id", "something")

  val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
  val TOPIC_METADATA: String = "robot_positions_output_table"

  consumer.subscribe(util.Collections.singletonList(TOPIC_METADATA))

  while(true){
    println("polling...")
    val records: ConsumerRecords[String, String] = consumer.poll(1000)
    for (record<-records.asScala){
      //print("MESSAGE: " + record.topic() + " - " + record.key() + " -> " + record.value())
      val newRobotPosition = record.value()
      val data =  (record.key() -> record.value())
      println("Positions: " + data)
    }
  }
}
