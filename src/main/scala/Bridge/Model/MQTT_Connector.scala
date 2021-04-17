package Bridge.Model

import Business.{MQTTData, Triangulation}
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ClosedShape
import akka.stream.alpakka.mqtt.scaladsl.MqttSource
import akka.stream.alpakka.mqtt.{MqttConnectionSettings, MqttMessage, MqttQoS, MqttSubscriptions}
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

import java.util.Properties
import scala.util.parsing.json.JSON

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
  props.put("value.serializer", "Business.Serializer.MessageSerializer")

  val producer: KafkaProducer[String, MQTTData] = new KafkaProducer[String, MQTTData](props)
  val TOPIC_METADATA: String = "robot_positioning_from_factory"

  val graph = GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>

    import GraphDSL.Implicits._

    //  MQTT client implementation
    val input = builder.add(MqttSource.atMostOnce(connectionSettings.withClientId(clientId = "mqtt_position_consumer"),
        MqttSubscriptions(MQTT_TOPIC -> MqttQoS.AtLeastOnce), bufferSize = 8))

    val deserializer = builder.add(Flow[MqttMessage].map(deserializeJson))

    //  Kafka transformation
    val prepareKafkaMessage = builder.add(Flow[Map[String, Any]].map(transformToKafka))

    //  Kafka producer implementation
    val output = builder.add(Sink.foreach[ProducerRecord[String, MQTTData]](producer.send(_)))

    //  Combination of components
    input ~> deserializer ~> prepareKafkaMessage ~> output

    ClosedShape
  }

  //  Start the transformation graph
  RunnableGraph.fromGraph(graph).run()

  def deserializeJson(mqttMessage: MqttMessage): Map[String, Any] = {
    val MQTT_PAYLOAD = mqttMessage.payload.utf8String
    println(MQTT_PAYLOAD)
    val result = JSON.parseFull(MQTT_PAYLOAD)
    result match {
      case x: Some[Map[String, Any]] => x.value
      case None => Map()
    }
  }

  def transformToKafka(jsonData: Map[String, Any]): ProducerRecord[String, MQTTData] = {

    val robotMap = jsonData.get("mqttdata") match {
      case Some(x: Map[String, Any]) => x
    }
    val topic: String = robotMap.get("topic").getOrElse("null").toString
    println(topic)
    val position: (Int, Int) = robotMap.get("position").getOrElse(List()) match {
      case List(a: Double, b: Double) => (a.toInt, b.toInt)
    }
    val timestamp: String = robotMap.get("timestamp") match {
      case Some(x) => if(x != "null") x.toString else "null"
    }
    val triangulation: String = robotMap.get("triangulation") match {
      case Some(x) => if(x != "null") x.toString else "null"
    }

    val newRobotDate: MQTTData = MQTTData(topic, position, timestamp, triangulation)
    new ProducerRecord(TOPIC_METADATA, topic, newRobotDate)
  }
}
