package Controller

import Controller.EventEnumeration.EventEnumeration
import Model.{MQTT_Robot_Client, RobotPosition, Timer}

class RobotController{

  var manualSteeringRobotPosition: RobotPosition = RobotPosition(50, 50)
  val mqttRobotClient = new MQTT_Robot_Client()

  Timer(2000) {
    val mqttPayload = manualSteeringRobotPosition.x + "," + manualSteeringRobotPosition.y
    mqttRobotClient.publish(mqttPayload)
  }

  def updateManualSteeringRobotPosition(event: EventEnumeration, value: Int): Unit = {
    if(event == EventEnumeration.UP || event == EventEnumeration.DOWN) { }//manualSteeringRobotPosition.y += value }
    else { }//manualSteeringRobotPosition.x += value }
  }
}
