package Dashboard.Controller

import Business.MQTTData
import Dashboard.Model.Kafka_Consumer
import Dashboard.View.FactoryDashboard
import org.apache.kafka.clients.consumer.ConsumerRecords

import scala.jdk.CollectionConverters.iterableAsScalaIterableConverter

class DashboardController(dashboardView: FactoryDashboard) {

  var robotPositions = Map[String, MQTTData]()

  //  View Object
  val _dashBoardView = dashboardView

  //  Message consumption in Kafka
  val kafka_Consumer = new Kafka_Consumer()

  //  Mode in View
  var mode: Boolean = false

  def start(): Unit = {
    while(true){
      println("polling...")
      val records: ConsumerRecords[String, MQTTData] = kafka_Consumer.consumer.poll(500)
      for (record<-records.asScala){
        println("MESSAGE: " + record.topic() + " - " + record.key() + " -> " + record.value())
        val newData = record.value()
        updateRobotPositions(Map(record.key() -> newData))
      }
    }
  }

  def updateRobotPositions(data: Map[String, MQTTData]): Unit = {
    dashboardView.canvas.updateRobots(data)
  }
}
