package Model.Robot

import Model.MQTT_Robot_Client

import java.awt.Image

class ManualRobot(override val name: String, override val image: Image, override val minPosition: RobotPosition,
                  override val maxPosition: RobotPosition, override var currentPosition: RobotPosition) extends Robot {

  val mqttRobotClient: MQTT_Robot_Client = new MQTT_Robot_Client(name)

}
