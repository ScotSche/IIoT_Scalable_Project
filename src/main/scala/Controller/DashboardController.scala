package Controller

import Model.{Kafka_Consumer, RobotPosition}
import View.FactoryDashboard
import org.apache.kafka.clients.consumer.ConsumerRecords
import scala.jdk.CollectionConverters.iterableAsScalaIterableConverter

class DashboardController(dashboardView: FactoryDashboard) {

  var manualSteeringRobotPosition: RobotPosition = RobotPosition(50, 50)

  //  View Object
  val _dashBoardView = dashboardView

  //  Message consuption in Kafka
  val kafka_Consumer = new Kafka_Consumer()

  def start(): Unit = {
    while(true){
      println("polling...")
      val records: ConsumerRecords[String, String] = kafka_Consumer.consumer.poll(1000)
      for (record<-records.asScala){
        print("MESSAGE: " + record.topic() + " - " + record.key() + " -> " + record.value())
        val msg = record.value()
        val tmpArray = msg.split(",")
        if (tmpArray.length == 2){
          updateManualSteeringRobotPosition(RobotPosition(tmpArray(0).toInt, tmpArray(1).toInt))
        }
      }
    }
  }

  def updateManualSteeringRobotPosition(robotPosition: RobotPosition): Unit = {
    dashboardView.canvas.updateRobotPositions(robotPosition)
  }
}
