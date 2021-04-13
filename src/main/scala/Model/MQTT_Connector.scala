package Model

import Model.Robot.RobotPosition
import Model.Serializer.Triangulation
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ClosedShape
import akka.stream.alpakka.mqtt.{MqttConnectionSettings, MqttMessage, MqttQoS, MqttSubscriptions}
import akka.stream.alpakka.mqtt.scaladsl.MqttSource
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Zip}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

import java.util.Properties
import scala.util.parsing.json.JSON

case class RobotDataTransformation(robotName: String, robotDistance: Double, timestamp: String, stationName: String, stationPosition: (Int, Int))

class MQTT_Connector {

  type DoubleTuple = (RobotDataTransformation, RobotDataTransformation)

  type Database = Map[String, Map[String, RobotDataTransformation]]

  type RobotPositionDatabase = Map[String, (Int, Int)]

  implicit val system: ActorSystem = ActorSystem()

  val triangulation: Triangulation = new Triangulation()

  var robotMap: Database = Map()

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
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](props)
  val TOPIC_METADATA: String = "robot_positioning_from_factory"

  val graph = GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>

    import GraphDSL.Implicits._

    //  MQTT client implementation
    val input = builder.add(MqttSource.atMostOnce(connectionSettings.withClientId(clientId = "mqtt_position_consumer"),
        MqttSubscriptions(MQTT_TOPIC -> MqttQoS.AtLeastOnce), bufferSize = 8))

    //  Kafka transformation
    val prepareKafkaMessage = builder.add(Flow[MqttMessage].map(transformToKafka))

    //  Kafka producer implementation
    val output = builder.add(Sink.foreach[ProducerRecord[String, String]](producer.send(_)))

    //  Combination of components
    input ~> prepareKafkaMessage ~> output

    ClosedShape
  }

  //  Start the transformation graph
  RunnableGraph.fromGraph(graph).run()

  def transformToKafka(mqttMessage: MqttMessage): ProducerRecord[String, String] = {
    print(mqttMessage.topic + " -> " + mqttMessage.payload.utf8String)
    val MQTT_PAYLOAD = mqttMessage.payload.utf8String
    val payloadChange = MQTT_PAYLOAD.split("/")
    val payloadPosition = payloadChange(0).toString.substring(1).substring(0, payloadChange(0).length - 2).split(",").toList.map(x => x.toInt) match {
      case List(a, b) => (a, b)
    }
    val payloadTimestamp = payloadChange(1)
    val newRobotPosition: RobotPosition = RobotPosition(payloadPosition._1, payloadPosition._2, payloadTimestamp)
    new ProducerRecord(TOPIC_METADATA, mqttMessage.topic, newRobotPosition.toString)
  }
}
