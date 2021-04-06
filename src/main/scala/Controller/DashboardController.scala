package Controller

import Model.Kafka_Consumer
import Model.Robot.RobotPosition
import View.FactoryDashboard
import org.apache.kafka.clients.consumer.ConsumerRecords

import scala.collection.mutable
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
      val records: ConsumerRecords[String, RobotPosition] = kafka_Consumer.consumer.poll(1000)
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
