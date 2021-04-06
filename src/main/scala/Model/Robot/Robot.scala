package Model.Robot

import Model.MQTT_Robot_Client

import java.awt.Image
import java.time.LocalDateTime

trait Robot {
  def name: String
  def image: Image
  def minPosition: RobotPosition
  def maxPosition: RobotPosition
  var currentPosition: RobotPosition
  def mqttRobotClient: MQTT_Robot_Client

    def changeXPosition(xPositionDelta: Int, timeStamp: LocalDateTime): Unit = {
      val newXPosition = currentPosition.x + xPositionDelta
      if (newXPosition >= minPosition.x && newXPosition <= maxPosition.x) {
        currentPosition = RobotPosition(newXPosition, currentPosition.y, timeStamp.toString)
      }
    }

    def changeYPosition(yPositionDelta: Int, timeStamp: LocalDateTime): Unit = {
      val newYPosition = currentPosition.y + yPositionDelta
      if (newYPosition >= minPosition.y && newYPosition <= maxPosition.y) {
        currentPosition = RobotPosition(currentPosition.x, newYPosition, timeStamp.toString)
      }
    }

    def mqtt_publish(): Unit = {
      val mqttPayload = currentPosition.x + "," + currentPosition.y
      mqttRobotClient.publish(mqttPayload)
    }
}
