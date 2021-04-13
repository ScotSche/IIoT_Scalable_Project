package General.Model.Robot

import Factory.Model.MQTT_Robot_Client

import java.awt.Image

class AutonomousRobot(override val name: String, override val minPosition: RobotPosition,
                      override val maxPosition: RobotPosition, val vertical: Boolean,
                      var reverseMovement: Boolean) extends Robot{

  val mqttRobotClient = new MQTT_Robot_Client(name)
}
