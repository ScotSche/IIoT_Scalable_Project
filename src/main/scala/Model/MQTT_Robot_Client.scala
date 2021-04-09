package Model

import akka.Done
import akka.actor.ActorSystem
import akka.stream.alpakka.mqtt.scaladsl.MqttSink
import akka.stream.alpakka.mqtt.{MqttConnectionSettings, MqttMessage, MqttQoS}
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

import java.time.LocalDateTime
import scala.concurrent.Future

class MQTT_Robot_Client(name: String) {

  implicit val system: ActorSystem = ActorSystem()

  val MQTT_BASIC_LEVEL = "robotfactory/positions/"

  val connectionSettings = MqttConnectionSettings(
    "tcp://localhost:1883",
    name,
    new MemoryPersistence
  )

  val sink: Sink[MqttMessage, Future[Done]] = MqttSink(connectionSettings, MqttQoS.AtLeastOnce)

  def publish(payload:String) = {
    val MQTT_TOPIC = MQTT_BASIC_LEVEL + name
    val MQTT_PAYLOAD = payload
    val messages = List(MqttMessage(MQTT_TOPIC, ByteString(MQTT_PAYLOAD)))
    Source(messages).runWith(sink)
    println(name + ": MQTT message sent - " + MQTT_PAYLOAD)
  }
}
