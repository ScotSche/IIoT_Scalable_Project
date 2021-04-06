package Model

import java.awt.Image

case class Robot(name: String, image: Image, minPosition: RobotPosition, maxPosition: RobotPosition, var position: RobotPosition){

  val mqttRobotClient = new MQTT_Robot_Client(name)

  def changeXPosition(xPositionDelta: Int): Unit ={
    val newXPosition = position.x + xPositionDelta
    if(newXPosition >= minPosition.x && newXPosition <= maxPosition.x) {
      position = RobotPosition(newXPosition, position.y)
    }
  }

  def changeYPosition(yPositionDelta: Int): Unit = {
    val newYPosition = position.y + yPositionDelta
    if(newYPosition >= minPosition.y && newYPosition <= maxPosition.y) {
      position = RobotPosition(position.x, newYPosition)
    }
  }

  def mqtt_publish(): Unit ={
    val mqttPayload = position.x + "," + position.y
    mqttRobotClient.publish(mqttPayload)
  }
}