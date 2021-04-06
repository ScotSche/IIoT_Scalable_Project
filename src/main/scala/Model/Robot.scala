package Model

import java.awt.Image
import java.time.LocalDateTime

case class Robot(name: String, image: Image, minPosition: RobotPosition, maxPosition: RobotPosition, var positionChanged: Boolean,
                 var position: RobotPosition){

  val mqttRobotClient = new MQTT_Robot_Client(name)

  def changeXPosition(xPositionDelta: Int, timeStamp: LocalDateTime): Unit ={
    val newXPosition = position.x + xPositionDelta
    if(newXPosition >= minPosition.x && newXPosition <= maxPosition.x) {
      position = RobotPosition(newXPosition, position.y, timeStamp.toString)
    }
  }

  def changeYPosition(yPositionDelta: Int, timeStamp: LocalDateTime): Unit = {
    val newYPosition = position.y + yPositionDelta
    if(newYPosition >= minPosition.y && newYPosition <= maxPosition.y) {
      position = RobotPosition(position.x, newYPosition, timeStamp.toString)
    }
  }

  def mqtt_publish(): Unit ={
    val mqttPayload = position.x + "," + position.y
    mqttRobotClient.publish(mqttPayload)
  }
}