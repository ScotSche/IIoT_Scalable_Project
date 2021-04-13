package General.Model.Kafka

import General.Model.Robot.RobotPosition
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties

object Producer extends App{

  //  Kafka Properties
  val props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](props)
  val TOPIC_METADATA: String = "test_topic"

  while (true){
    producer.send(new ProducerRecord(TOPIC_METADATA, "Test_Partition", "Test_Payload"))
    Thread.sleep(2000)
  }
}
