package Model.Robot

import Model.MQTT_Robot_Client

import java.awt.Image
import java.time.LocalDateTime

trait Robot {
  def name: String
  def minPosition: RobotPosition
  def maxPosition: RobotPosition
}
