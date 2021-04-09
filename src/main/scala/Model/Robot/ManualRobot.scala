package Model.Robot

import Model.MQTT_Robot_Client

import java.awt.Image

class ManualRobot(override val name: String, override val minPosition: RobotPosition,
                  override val maxPosition: RobotPosition) extends Robot {

  val mqttRobotClient: MQTT_Robot_Client = new MQTT_Robot_Client(name)
}
