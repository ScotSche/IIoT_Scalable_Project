package Controller

import Controller.EventEnumeration.EventEnumeration
import Model.Robot.{AutonomousRobot, ManualRobot}
import Model.{RobotPosition, Timer}

import java.awt.Image
import java.time.LocalDateTime
import javax.swing.ImageIcon

class RobotController{

  var robotImage = new ImageIcon("src/images/robotimage_DARK.png").getImage()
    .getScaledInstance(50, 50, Image.SCALE_DEFAULT)

  var manual_Robot = new ManualRobot("robot_manual", robotImage, RobotPosition(0, 0, null),
    RobotPosition(950, 550, null), RobotPosition(50, 50, null))

  var manual_Robot_Position_OLD = manual_Robot.currentPosition

  var autonomousRobots = List(
    new AutonomousRobot("robot_one", robotImage, RobotPosition(0, 0, null),
      RobotPosition(950, 550, null), RobotPosition(200, 50, null), true),
    new AutonomousRobot("robot_two", robotImage, RobotPosition(0, 0, null),
      RobotPosition(950, 550, null), RobotPosition(400, 500, null), false),
    new AutonomousRobot("robot_three", robotImage, RobotPosition(0, 0, null),
      RobotPosition(950, 550, null), RobotPosition(600, 50, null), true),
    new AutonomousRobot("robot_four", robotImage, RobotPosition(0, 0, null),
      RobotPosition(950, 550, null), RobotPosition(800, 500, null), false)
  )

  //  Manual Robot Update
  Timer(2000) {
    if(manual_Robot_Position_OLD != manual_Robot.currentPosition){
      manual_Robot.mqtt_publish()
      manual_Robot_Position_OLD = manual_Robot.currentPosition
    }
  }

  Timer(1000) {
    autonomousRobots.foreach(rbt => {
      if(rbt.downMovement){
        rbt.changeYPosition(25, LocalDateTime.now())
      }
      else{
        rbt.changeYPosition(-25, LocalDateTime.now())
      }
      if(rbt.currentPosition.y == rbt.minPosition.y){
        rbt.downMovement = true
      }
      else if(rbt.currentPosition.y == rbt.maxPosition.y){
        rbt.downMovement = false
      }
      rbt.mqtt_publish()
    })
  }

  def updateManualSteeringRobotPosition(event: EventEnumeration, value: Int): Unit = {
    if(event == EventEnumeration.UP || event == EventEnumeration.DOWN) { manual_Robot.changeYPosition(value, LocalDateTime.now()) }
    else { manual_Robot.changeXPosition(value, LocalDateTime.now()) }
  }
}
