package Factory.Model.Robot

import Factory.Model.MQTT_Client

class ManualRobot(override val name: String, override val minPosition: RobotPosition,
                  override val maxPosition: RobotPosition) extends Robot {

  val mqttRobotClient: MQTT_Client = new MQTT_Client(name)
}
