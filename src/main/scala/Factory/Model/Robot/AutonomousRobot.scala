package Factory.Model.Robot

import Factory.Model.MQTT_Client

class AutonomousRobot(override val name: String, override val minPosition: RobotPosition,
                      override val maxPosition: RobotPosition, val vertical: Boolean,
                      var reverseMovement: Boolean) extends Robot{

  val mqttRobotClient = new MQTT_Client(name)
}
