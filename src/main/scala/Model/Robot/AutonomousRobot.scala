package Model.Robot

import Model.MQTT_Robot_Client

import java.awt.Image

class AutonomousRobot(override val name: String, override val image: Image, override val minPosition: RobotPosition,
                      override val maxPosition: RobotPosition, override var currentPosition: RobotPosition,
                      var downMovement: Boolean) extends Robot{

  val mqttRobotClient = new MQTT_Robot_Client(name)
}
