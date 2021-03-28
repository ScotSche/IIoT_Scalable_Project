import Model.MQTT_Connector

object ControllerMain {

  def main(args: Array[String]) = {

    //  Message ahndling from MQTT to Kafka
    val mqtt_to_kafka_connector = new MQTT_Connector()
  }
}
