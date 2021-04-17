package Factory.Controller

import Business.Locator.LocatorMaster
import Business.{EventEnumeration, Timer}

import java.awt.Image
import java.time.LocalDateTime
import javax.swing.ImageIcon
import EventEnumeration.EventEnumeration
import Factory.Model.Robot.{AutonomousRobot, ManualRobot, RobotPosition}

//  http://walter.bislins.ch/blog/index.asp?page=Schnittpunkte+zweier+Kreise+berechnen+%28JavaScript%29#H_Rechenformular

class FactoryController{

  // Triangulation Stations
  val locatorMaster: LocatorMaster = new LocatorMaster("locator_master")

  //  Robot images
  val robotImageFull = new ImageIcon("src/images/LogBOT4.0_Voll.png").getImage()
    .getScaledInstance(50, 50, Image.SCALE_DEFAULT)
  val robotImageEmpty = new ImageIcon("src/images/LogBOT4.0_Leer.png").getImage()
    .getScaledInstance(50, 50, Image.SCALE_DEFAULT)

  //  Mode in View
  var mode: Boolean = false

  // Manual Robot Position
  var manual_Robot_Position = RobotPosition(650 + 25, 750 + 25, null)

  //  Definition of manual robot
  var manual_Robot = new ManualRobot("robot_manual", null, null)

  //  Definition of autonomous robots
  var autonomousRobots = List(
    (new AutonomousRobot("robot_one", RobotPosition(50 + 25, 100 + 25, null),
      RobotPosition(375 + 25, 100 + 25, null), false, false),
      RobotPosition(50 + 25, 100 + 25, null)),
    (new AutonomousRobot("robot_two", RobotPosition(50 + 25, 325 + 25, null),
      RobotPosition(375 + 25, 325 + 25, null), false, true),
      RobotPosition(375 + 25, 325 + 25, null)),
    (new AutonomousRobot("robot_three", RobotPosition(500 + 25, 50 + 25, null),
      RobotPosition(500 + 25, 475 + 25, null), true, false),
      RobotPosition(500 + 25, 50 + 25, null)),
    (new AutonomousRobot("robot_four", RobotPosition(700 + 25, 50 + 25, null),
      RobotPosition(700 + 25, 475 + 25, null), true, true),
      RobotPosition(700 + 25, 475 + 25, null))
  )

  //  Handler for autonomous vs. manual mode
  Timer(1000){
    if(mode){ locatorMasterManual_handler() } else { locatorMasterAutonomous_handler() }
  }

  //  Timer for autonomous robots
  Timer(1000) {
    if( !mode) {
      autonomousRobots.foreach(robotData => {
        if (robotData._1.vertical) {
          if (robotData._1.reverseMovement) {
            robotData._2.y -= 25
            robotData._2.timeStampISO = LocalDateTime.now().toString
          }
          else {
            robotData._2.y += 25
            robotData._2.timeStampISO = LocalDateTime.now().toString
          }
          if (robotData._2.y == robotData._1.minPosition.y) {
            robotData._1.reverseMovement = false
          }
          else if (robotData._2.y == robotData._1.maxPosition.y) {
            robotData._1.reverseMovement = true
          }
        }
        else {
          if (robotData._1.reverseMovement) {
            robotData._2.x -= 25
            robotData._2.timeStampISO = LocalDateTime.now().toString
          }
          else {
            robotData._2.x += 25
            robotData._2.timeStampISO = LocalDateTime.now().toString
          }
          if (robotData._2.x == robotData._1.minPosition.x) {
            robotData._1.reverseMovement = false
          }
          else if (robotData._2.x == robotData._1.maxPosition.x) {
            robotData._1.reverseMovement = true
          }
        }
      })
    }
  }

  def locatorMasterAutonomous_handler(): Unit = {
    var tmpList: List[(String, RobotPosition)] = List()
    autonomousRobots.foreach(robots => tmpList ++= List((robots._1.name, robots._2)))
    locatorMaster.stations.foreach(station => {
      station.updateRobotPositions(tmpList)
    })
    locatorMaster.gatherAutonomousRobotDataFromLocator()
  }

  def locatorMasterManual_handler(): Unit = {
    val tmpList: List[(String, RobotPosition)] = List((manual_Robot.name, manual_Robot_Position))
    locatorMaster.stations.foreach(station => {
      station.updateRobotPositions(tmpList)
    })
    locatorMaster.gatherManualRobotDataFromLocator()
  }

  def updateManualSteeringRobotPosition(event: EventEnumeration, value: Int): Unit = {
    if(event == EventEnumeration.UP || event == EventEnumeration.DOWN) {
      manual_Robot_Position.y += value
      manual_Robot_Position.timeStampISO = LocalDateTime.now().toString
    }
    else {
      manual_Robot_Position.x += value
      manual_Robot_Position.timeStampISO = LocalDateTime.now().toString
    }
  }
}
