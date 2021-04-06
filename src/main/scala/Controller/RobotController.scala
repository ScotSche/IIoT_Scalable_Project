package Controller

import Controller.EventEnumeration.EventEnumeration
import Model.{Robot, RobotPosition, Timer}
import java.awt.Image
import java.time.LocalDateTime
import javax.swing.ImageIcon

class RobotController{

  var robotImage = new ImageIcon("src/images/robotimage_DARK.png").getImage()
    .getScaledInstance(50, 50, Image.SCALE_DEFAULT)

  var manual_Robot = Robot("robot_manual", robotImage, RobotPosition(0, 0, null), RobotPosition(950, 550, null), false,
    RobotPosition(50, 50, null))

  var manual_Robot_Position_OLD = manual_Robot.position

  var autonomousRobots = List(
    Robot("robot_one", robotImage, RobotPosition(0, 0, null), RobotPosition(950, 550, null), false,
      RobotPosition(200, 50, null)),
    Robot("robot_two", robotImage, RobotPosition(0, 0, null), RobotPosition(950, 550, null), false,
      RobotPosition(400, 500, null)),
    Robot("robot_three", robotImage, RobotPosition(0, 0, null), RobotPosition(950, 550, null), false,
      RobotPosition(600, 50, null)),
    Robot("robot_four", robotImage, RobotPosition(0, 0, null), RobotPosition(950, 550, null), false,
      RobotPosition(800, 500, null))
  )

  Timer(2000) {
    if(manual_Robot_Position_OLD != manual_Robot.position){
      manual_Robot.mqtt_publish()
      manual_Robot_Position_OLD = manual_Robot.position
    }
  }
  var downMovement = true

  Timer(1000) {
    directRobot(0)
    if(downMovement){
      autonomousRobots(0).changeYPosition(25, LocalDateTime.now())
    }
    else{
      autonomousRobots(0).changeYPosition(-25, LocalDateTime.now())
    }
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
    if(event == EventEnumeration.UP || event == EventEnumeration.DOWN) { manual_Robot.changeYPosition(value, LocalDateTime.now()) }
    else { manual_Robot.changeXPosition(value, LocalDateTime.now()) }
  }
}
