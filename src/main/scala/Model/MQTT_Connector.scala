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

case class RobotDataTransformation(robotName: String, robotDistance: Double, stationName: String, stationPosition: String)//(Int, Int))

class MQTT_Connector {

  implicit val system: ActorSystem = ActorSystem()

  val triangulation: Triangulation = new Triangulation()

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
  props.put("value.serializer", "Model.Serializer.MessageSerializer")

  val producer: KafkaProducer[String, RobotPosition] = new KafkaProducer[String, RobotPosition](props)
  val TOPIC_METADATA: String = "robot_positioning"

  val graph = GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>

    import GraphDSL.Implicits._

    //  MQTT client implementation
    val input = builder.add(MqttSource.atMostOnce(connectionSettings.withClientId(clientId = "mqtt_position_consumer"),
        MqttSubscriptions(MQTT_TOPIC -> MqttQoS.AtLeastOnce), bufferSize = 8))

    //  Kafka transformation
    val jsonDeserializer = builder.add(Flow[MqttMessage].map(deserializeJSON))

    val switchStationWithRobot = builder.add(Flow[Map[String, Any]].map(getRobotData))

    val broadcast = builder.add(Broadcast[List[RobotDataTransformation]](4))

    val robotSpecificationOne = builder.add(Flow[List[RobotDataTransformation]].map(getSpecificRobotData(_, "robot_one")))
    val robotSpecificationTwo = builder.add(Flow[List[RobotDataTransformation]].map(getSpecificRobotData(_, "robot_two")))
    val robotSpecificationThree = builder.add(Flow[List[RobotDataTransformation]].map(getSpecificRobotData(_, "robot_three")))
    val robotSpecificationFour = builder.add(Flow[List[RobotDataTransformation]].map(getSpecificRobotData(_, "robot_four")))



    val zipOne = builder.add(Zip[RobotDataTransformation, RobotDataTransformation])
    val zipTwo = builder.add(Zip[RobotDataTransformation, RobotDataTransformation])

    val zipFinal = builder.add(Zip[(RobotDataTransformation, RobotDataTransformation), (RobotDataTransformation, RobotDataTransformation)])

    //val flow = builder.add(Flow[MqttMessage].map(transformToKafka))

    //  Kafka producer implementation
    //val output = builder.add(Sink.foreach[ProducerRecord[String, RobotPosition]](producer.send(_)))
    val output = builder.add(Sink.foreach[((RobotDataTransformation, RobotDataTransformation),
      (RobotDataTransformation, RobotDataTransformation))](println))

    //  Combination of components
    input ~> jsonDeserializer ~> switchStationWithRobot ~> broadcast
                                 broadcast.out(0) ~> robotSpecificationOne ~> zipOne.in0
                                 broadcast.out(1) ~> robotSpecificationTwo ~> zipOne.in1
                                                                              zipOne.out ~> zipFinal.in0
                                 broadcast.out(2) ~> robotSpecificationThree ~> zipTwo.in0
                                 broadcast.out(3) ~> robotSpecificationFour ~> zipTwo.in1
                                                                              zipTwo.out ~> zipFinal.in1
                                                                                            zipFinal.out ~> output

    ClosedShape
  }

  //  Start the transformation graph
  RunnableGraph.fromGraph(graph).run()

  def getRobotData(data: Map[String, Any]): List[RobotDataTransformation] ={
    var stationToRobotList: List[RobotDataTransformation] = List()
    val robotMap = data.get("mqttdata") match {
      case Some(x: Map[String, Any]) => x
    }
    val stationName = robotMap.get("name") match {
      case Some(x: String) => x
    }
    val stationPosition = robotMap.get("position") match {
      case Some(x: String) => x
    }

    val robotList = robotMap.get("robotdata") match {
      case Some(x: List[Map[String, Any]]) => x
    }
    robotList.foreach(robotData => {
      var roboName: String = ""
      var distance: Double = 0.0
      robotData.get("name") match {
        case Some(x: String) =>  roboName = x
      }
      robotData.get("distance") match {
        case Some(x: Any) => distance =  x.toString.toDouble
      }
      stationToRobotList ++= List(RobotDataTransformation(roboName, distance, stationName, stationPosition))
    })
    stationToRobotList
  }

  def getSpecificRobotData(robotData: List[RobotDataTransformation], name: String): RobotDataTransformation = {
    robotData.foreach(data => {
      if(data.robotName.equals(name)) return data
    })
    RobotDataTransformation("", 0.0, "", "")
  }

  def transformToKafka(mqttMessage: MqttMessage): ProducerRecord[String, RobotPosition] = {
    print(mqttMessage.topic + " -> " + mqttMessage.payload.utf8String + " | ")
    val MQTT_PAYLOAD = mqttMessage.payload.utf8String
    val payload_split = MQTT_PAYLOAD.split(",")
    val newRobotPosition: RobotPosition = RobotPosition(payload_split(0).toInt, payload_split(1).toInt, payload_split(2))
    new ProducerRecord(TOPIC_METADATA, mqttMessage.topic, newRobotPosition)
  }

  def deserializeJSON(mqttMessage: MqttMessage): Map[String, Any] ={
    val MQTT_PAYLOAD = mqttMessage.payload.utf8String
    val result = JSON.parseFull(MQTT_PAYLOAD)
    result match {
      case x: Some[Map[String, Any]] => {
        //println(x.value)
        x.value
      }
      case None => {
        println("Empty")
        Map()
      }
    }
  }
}
