package Model

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ClosedShape
import akka.stream.alpakka.mqtt.{MqttConnectionSettings, MqttMessage, MqttQoS, MqttSubscriptions}
import akka.stream.alpakka.mqtt.scaladsl.MqttSource
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

import java.time.LocalDateTime
import java.util.Properties

class MQTT_Connector {

  implicit val system: ActorSystem = ActorSystem()

  //  MQTT Properties
  val connectionSettings = MqttConnectionSettings(
    "tcp://localhost:1883", // (1)
    "position_client", // (2)
    new MemoryPersistence // (3)
  )

  val MQTT_TOPIC = "robotfactory/positions/#"

  //  Kafka Properties
  val props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "Model.MessageSerializer")

  val producer: KafkaProducer[String, RobotPosition] = new KafkaProducer[String, RobotPosition](props)
  val TOPIC_METADATA: String = "robot_positioning"

  val graph = GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>

    import GraphDSL.Implicits._

    //  MQTT client implementation
    val input = builder.add(MqttSource.atMostOnce(connectionSettings.withClientId(clientId = "mqtt_position_consumer"),
        MqttSubscriptions(MQTT_TOPIC -> MqttQoS.AtLeastOnce), bufferSize = 8))

    //  Kafka transformation
    val flow = builder.add(Flow[MqttMessage].map(transformToKafka))

    //  Kafka producer implementation
    val output = builder.add(Sink.foreach[ProducerRecord[String, RobotPosition]](producer.send(_)))

    //  Combination of components
    input ~> flow ~> output

    ClosedShape
  }

  //  Start the transformation graph
  RunnableGraph.fromGraph(graph).run()

  def transformToKafka(mqttMessage: MqttMessage): ProducerRecord[String, RobotPosition] = {
    print(mqttMessage.topic + " -> " + mqttMessage.payload.utf8String + " | ")
    val MQTT_PAYLOAD = mqttMessage.payload.utf8String
    val payload_split = MQTT_PAYLOAD.split(",")
    val newRobotPosition: RobotPosition = RobotPosition(payload_split(0).toInt, payload_split(1).toInt, payload_split(2))
    new ProducerRecord(TOPIC_METADATA, mqttMessage.topic, newRobotPosition)
  }
}
