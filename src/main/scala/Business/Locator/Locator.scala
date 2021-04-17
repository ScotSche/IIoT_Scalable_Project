package Business.Locator

import Robots.RobotPosition

import java.time.LocalDateTime
import scala.math.{pow, sqrt}

class Locator(name:String, position: (Int, Int), master: LocatorMaster) {
  var robot_Data: List[(String, Double, String)] = List()

  def pull_data(): String ={
    createJSONData()
  }

  def updateRobotPositions(preData: List[(String, RobotPosition)]): Unit ={
    var newList: List[(String, Double, String)] = List()
    preData.foreach(data => {

      //  Calculate Delta of Coordinates
      var deltaX = data._2.x - position._1
      var deltaY = data._2.y - position._2
      if(deltaX < 0) deltaX *= -1
      if(deltaY < 0) deltaY *= -1
      val deltaCoordinates = (deltaX, deltaY)

      //  Pythagoras to calculate distance between points
      val distance = sqrt(pow(deltaCoordinates._1, 2) + pow(deltaCoordinates._2, 2))
      newList ++= List((data._1, distance, data._2.timeStampISO))
    })
    robot_Data = newList
  }

  def createJSONData(): String = s"""
  {
    "mqttdata": {
      "name": "$name",
      "position": "$position",
      "timestamp": "${LocalDateTime.now().toString}",
      "robotdata": [
        ${createJSONMetaData()}
      ]
    }
  }
  """.strip()

  def getJSONMetaData(data: (String, Double, String)): String = s"""
  {
    "name": "${data._1}",
    "distance": "${data._2}",
    "timestamp": "${data._3}"
  }
  """.strip()

  def createJSONMetaData(): String = {
    var string = ""

    robot_Data.foreach(data => {
      string += getJSONMetaData(data)
      if( data != robot_Data.last){
        string += ","
      }
    })
    string
  }
}
