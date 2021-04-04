package Controller

import Controller.EventEnumeration.EventEnumeration
import Model.{MQTT_Robot_Client, Robot, RobotPosition, Timer}

import javax.swing.ImageIcon

class RobotController{

  var manual_Robot = Robot(new ImageIcon("src/images/robotimage.png").getImage(),
    RobotPosition(0, 0), RobotPosition(950, 550), RobotPosition(50, 50))
  val mqttRobotClient = new MQTT_Robot_Client()

  Timer(2000) {
    val mqttPayload = manual_Robot.position.x + "," + manual_Robot.position.y
    println("Payload: " + mqttPayload)
    mqttRobotClient.publish(mqttPayload)
  }

  def updateManualSteeringRobotPosition(event: EventEnumeration, value: Int): Unit = {
    if(event == EventEnumeration.UP || event == EventEnumeration.DOWN) { manual_Robot.changeYPosition(value) }
    else { manual_Robot.changeXPosition(value) }
  }
}
