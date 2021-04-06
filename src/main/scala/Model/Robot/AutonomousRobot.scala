package Model.Robot

import Model.{MQTT_Robot_Client, RobotPosition}

import java.awt.Image

class AutonomousRobot(val name: String, val image: Image, val minPosition: RobotPosition,
                      val maxPosition: RobotPosition, var currentPosition: RobotPosition,
                      var downMovement: Boolean) extends Robot{

  val mqttRobotClient = new MQTT_Robot_Client(name)
}
