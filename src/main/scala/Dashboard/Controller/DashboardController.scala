package Dashboard.Controller

import Dashboard.Model.Kafka_Consumer
import Dashboard.View.FactoryDashboard
import General.Model.Robot.RobotPosition
import org.apache.kafka.clients.consumer.ConsumerRecords

import scala.jdk.CollectionConverters.iterableAsScalaIterableConverter

class DashboardController(dashboardView: FactoryDashboard) {

  var robotPositions = Map[String, RobotPosition]()

  //  View Object
  val _dashBoardView = dashboardView

  //  Message consumption in Kafka
  val kafka_Consumer = new Kafka_Consumer()

  def start(): Unit = {
    while(true){
      println("polling...")
      val records: ConsumerRecords[String, RobotPosition] = kafka_Consumer.consumer.poll(500)
      for (record<-records.asScala){
        //print("MESSAGE: " + record.topic() + " - " + record.key() + " -> " + record.value())
        val newRobotPosition = record.value()
        robotPositions += (record.key() -> newRobotPosition)
        println("Positions: " + robotPositions)
        updateRobotPositions()
      }
    }
  }

  def updateRobotPositions(): Unit = {
    dashboardView.canvas.updateRobots(robotPositions)
  }
}