package Controller

import Controller.EventEnumeration.EventEnumeration
import Model.{MQTT_Robot_Client, Robot, RobotPosition, Timer}

import java.awt.Image
import javax.swing.ImageIcon

class RobotController{

  var robotImage = new ImageIcon("src/images/robotimage_DARK.png").getImage()
    .getScaledInstance(50, 50, Image.SCALE_DEFAULT)

  var manual_Robot = Robot("robot_manual", robotImage, RobotPosition(0, 0), RobotPosition(950, 550), RobotPosition(50, 50))

  var autonomousRobots = List(
    Robot("robot_one", robotImage, RobotPosition(0, 0), RobotPosition(950, 550), RobotPosition(200, 50)),
    Robot("robot_two", robotImage, RobotPosition(0, 0), RobotPosition(950, 550), RobotPosition(400, 500)),
    Robot("robot_three", robotImage, RobotPosition(0, 0), RobotPosition(950, 550), RobotPosition(600, 50)),
    Robot("robot_four", robotImage, RobotPosition(0, 0), RobotPosition(950, 550), RobotPosition(800, 500)),
  )

  Timer(2000) {
    manual_Robot.mqtt_publish()
  }
  var downMovement = true

  Timer(1000) {
    directRobot(0)
    if(downMovement){
      autonomousRobots(0).changeYPosition(25)
    }
    else{
      autonomousRobots(0).changeYPosition(-25)
    }
    val mqttPayload = manual_Robot.position.x + "," + manual_Robot.position.y
    autonomousRobots(0).mqtt_publish()
  }

  def directRobot(robot_number: Int): Unit ={
    if(autonomousRobots(robot_number).position.y == autonomousRobots(robot_number).minPosition.y){
      downMovement = true
    }
    else if (autonomousRobots(robot_number).position.y == autonomousRobots(robot_number).maxPosition.y){
      downMovement = false
    }
  }

  def updateManualSteeringRobotPosition(event: EventEnumeration, value: Int): Unit = {
    if(event == EventEnumeration.UP || event == EventEnumeration.DOWN) { manual_Robot.changeYPosition(value) }
    else { manual_Robot.changeXPosition(value) }
  }
}
