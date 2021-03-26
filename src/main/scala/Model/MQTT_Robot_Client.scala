package Model

import akka.Done
import akka.actor.{ActorRef, ActorRefFactory, ActorRefProvider, ActorSystem, ActorSystemImpl, InternalActorRef, Props}
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.mqtt.scaladsl.MqttSink
import akka.stream.alpakka.mqtt.{MqttConnectionSettings, MqttMessage, MqttQoS}
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

import scala.concurrent.{ExecutionContextExecutor, Future}

object MQTT_Robot_Client extends App{

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: Materializer = ActorMaterializer()

  val MQTT_TOPIC = "roboter_one_position"

  val messages = List(MqttMessage(MQTT_TOPIC, ByteString("Test message")))

  val connectionSettings = MqttConnectionSettings(
    "tcp://localhost:1883",
    "roboter-one-client",
    new MemoryPersistence
  )

  val sink: Sink[MqttMessage, Future[Done]] = MqttSink(connectionSettings, MqttQoS.AtLeastOnce)

  while(true){
    Thread.sleep(1000)
    println("Messages send")
    Source(messages).runWith(sink)
  }
}
