package Model.Robot

import Model.{MQTT_Robot_Client, RobotPosition}

import java.awt.Image

class ManualRobot(val name: String, val image: Image, val minPosition: RobotPosition,
                  val maxPosition: RobotPosition, var currentPosition: RobotPosition) extends Robot {

  val mqttRobotClient: MQTT_Robot_Client = new MQTT_Robot_Client(name)

}
