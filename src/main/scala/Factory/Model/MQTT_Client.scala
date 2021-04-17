package Factory.Model

import akka.Done
import akka.actor.ActorSystem
import akka.stream.alpakka.mqtt.scaladsl.MqttSink
import akka.stream.alpakka.mqtt.{MqttConnectionSettings, MqttMessage, MqttQoS}
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

import scala.concurrent.Future

class MQTT_Client(name: String) {

  implicit val system: ActorSystem = ActorSystem()

  val MQTT_BASIC_LEVEL = "robotfactory/positions/"

  val connectionSettings = MqttConnectionSettings(
    "tcp://localhost:1883",
    name,
    new MemoryPersistence
  )
    .withAutomaticReconnect(true)


  val sink: Sink[MqttMessage, Future[Done]] = MqttSink(connectionSettings, MqttQoS.AtLeastOnce)

  def publish(topic: String, payload:String) = {
    val MQTT_TOPIC = MQTT_BASIC_LEVEL + topic
    val MQTT_PAYLOAD = payload
    val messages = List(MqttMessage(MQTT_TOPIC, ByteString(MQTT_PAYLOAD)))
    Source(messages).runWith(sink)
    //println(topic + ": MQTT message sent - " + MQTT_PAYLOAD)
  }
}
