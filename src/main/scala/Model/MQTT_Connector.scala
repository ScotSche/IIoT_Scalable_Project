package Model

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ClosedShape
import akka.stream.alpakka.mqtt.{MqttConnectionSettings, MqttMessage, MqttQoS, MqttSubscriptions}
import akka.stream.alpakka.mqtt.scaladsl.MqttSource
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.Properties

object MQTT_Connector {

  implicit val system: ActorSystem = ActorSystem()

  //  MQTT Properties
  val connectionSettings = MqttConnectionSettings(
    "tcp://localhost:1883", // (1)
    "position_client", // (2)
    new MemoryPersistence // (3)
  )

  val MQTT_TOPIC = "roboter_one_position"

  //  Kafka Properties
  val props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](props)
  val TOPIC_METADATA: String = "robot_positioning"
  val KEY_METADATA: String = "robot_one_data"

  val graph = GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>

    import GraphDSL.Implicits._

    //  MQTT client implementation
    val input = builder.add(MqttSource.atMostOnce(connectionSettings.withClientId(clientId = "mqtt_position_consumer"),
        MqttSubscriptions(MQTT_TOPIC -> MqttQoS.AtLeastOnce), bufferSize = 8))

    //  Kafka transformation
    val flow = builder.add(Flow[MqttMessage].map(transformToKafka))

    //  Kafka producer implementation
    val output = builder.add(Sink.foreach[ProducerRecord[String, String]](producer.send(_)))

    //  Combination of components
    input ~> flow ~> output

    ClosedShape
  }

  def transformToKafka(mqttMessage: MqttMessage): ProducerRecord[String, String] = {
    new ProducerRecord(TOPIC_METADATA, mqttMessage.topic, mqttMessage.payload.utf8String)
  }

  def main(args: Array[String]): Unit = {
    RunnableGraph.fromGraph(graph).run()
  }
}
