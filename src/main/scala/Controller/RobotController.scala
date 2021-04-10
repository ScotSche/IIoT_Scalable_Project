package Controller

import Controller.EventEnumeration.EventEnumeration
import Model.Locator.{Locator, LocatorMaster}
import Model.Robot.{AutonomousRobot, ManualRobot, Robot, RobotPosition}
import Model.Timer

import scala.math._
import java.awt.{Graphics2D, Image}
import java.time.LocalDateTime
import javax.swing.ImageIcon

//  http://walter.bislins.ch/blog/index.asp?page=Schnittpunkte+zweier+Kreise+berechnen+%28JavaScript%29#H_Rechenformular

class RobotController{

  // Triangulation Stations
  val locatorMaster: LocatorMaster = new LocatorMaster("locator_master")

  //  Robot images
  val robotImageFull = new ImageIcon("src/images/LogBOT4.0_Voll.png").getImage()
    .getScaledInstance(50, 50, Image.SCALE_DEFAULT)
  val robotImageEmpty = new ImageIcon("src/images/LogBOT4.0_Leer.png").getImage()
    .getScaledInstance(50, 50, Image.SCALE_DEFAULT)

  // Manual Robot Position
  var manual_Robot_Position = RobotPosition(650, 750, null)
  var manual_Robot_Position_OLD = manual_Robot_Position

  //  Definition of manual robot
  var manual_Robot = new ManualRobot("robot_manual", null, null)

  //  Definition of autonomous robots
  var autonomousRobots = List(
    (new AutonomousRobot("robot_one", RobotPosition(50, 100, null),
      RobotPosition(375, 100, null), false, false), RobotPosition(50, 100, null)),
    (new AutonomousRobot("robot_two", RobotPosition(50, 325, null),
      RobotPosition(375, 325, null), false, true), RobotPosition(375, 325, null)),
    (new AutonomousRobot("robot_three", RobotPosition(500, 50, null),
      RobotPosition(500, 475, null), true, false), RobotPosition(500, 50, null)),
    (new AutonomousRobot("robot_four", RobotPosition(700, 50, null),
      RobotPosition(700, 475, null), true, true), RobotPosition(700, 475, null))
  )

  Timer(1000) {
    var tmpList: List[(String, RobotPosition)] = List()
    autonomousRobots.foreach(robots => tmpList ++= List((robots._1.name, robots._2)))
    locatorMaster.stations.foreach(station => {
      station.updateRobotPositions(tmpList)
    })
    locatorMaster.gatherRobotDataFromLocator()
  }

  //  Manual Robot Update
/*  Timer(1000){
    calculateTriangulation()
  }*/
  /*Timer(2000) {
    if(manual_Robot_Position_OLD != manual_Robot.currentPosition){
      manual_Robot.mqtt_publish()
      manual_Robot_Position_OLD = manual_Robot.currentPosition
    }
  }*/

  Timer(1000) {
    autonomousRobots.foreach(robotData => {
      if(robotData._1.vertical){
        if(robotData._1.reverseMovement){
          robotData._2.y -= 25
          robotData._2.timeStampISO = LocalDateTime.now().toString
        }
        else{
          robotData._2.y += 25
          robotData._2.timeStampISO = LocalDateTime.now().toString
        }
        if(robotData._2.y == robotData._1.minPosition.y){
          robotData._1.reverseMovement = false
        }
        else if(robotData._2.y == robotData._1.maxPosition.y){
          robotData._1.reverseMovement = true
        }
      }
      else{
        if(robotData._1.reverseMovement){
          robotData._2.x -= 25
          robotData._2.timeStampISO = LocalDateTime.now().toString
        }
        else{
          robotData._2.x += 25
          robotData._2.timeStampISO = LocalDateTime.now().toString
        }
        if(robotData._2.x == robotData._1.minPosition.x){
          robotData._1.reverseMovement = false
        }
        else if(robotData._2.x == robotData._1.maxPosition.x){
          robotData._1.reverseMovement = true
        }
      }
    })
  }

  def updateManualSteeringRobotPosition(event: EventEnumeration, value: Int): Unit = {
    if(event == EventEnumeration.UP || event == EventEnumeration.DOWN) { manual_Robot_Position.y += value }//manual_Robot.changeYPosition(value, LocalDateTime.now()) }
    else { manual_Robot_Position.x += value }//manual_Robot.changeXPosition(value, LocalDateTime.now()) }
  }
}
