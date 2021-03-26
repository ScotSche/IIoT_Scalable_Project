package Model

import akka.Done
import akka.actor.ActorSystem
import akka.stream.alpakka.mqtt.scaladsl.MqttSink
import akka.stream.alpakka.mqtt.{MqttConnectionSettings, MqttMessage, MqttQoS}
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

import scala.concurrent.Future

class MQTT_Robot_Client {

  implicit val system: ActorSystem = ActorSystem()

  val MQTT_TOPIC = "roboter_one_position"

  val connectionSettings = MqttConnectionSettings(
    "tcp://localhost:1883",
    "roboter-one-client",
    new MemoryPersistence
  )

  val sink: Sink[MqttMessage, Future[Done]] = MqttSink(connectionSettings, MqttQoS.AtLeastOnce)

  def publish(payload:String) = {
    val messages = List(MqttMessage(MQTT_TOPIC, ByteString(payload)))
    Source(messages).runWith(sink)
    println("MQTT position message sent!")
  }
}
