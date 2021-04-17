package Business

import Robots.RobotDataTransformation

import scala.math.{pow, sqrt}

class Triangulation {

  type Database = Map[String, Map[String, RobotDataTransformation]]
  type StationData = (String, (Int, Int), Double)
  type RobotPositionDatabase = Map[String, (Int, Int)]

  def triangulation(stationAPosition: (Int, Int), stationADistance: Double,
                    stationBPosition: (Int, Int), stationBDistance: Double): List[(Int, Int)] ={

    var intersection_list: List[(Int, Int)] = List()

    //  Calculate center point deltas Station 0 & Station 1
    val AB0 = stationBPosition._1 - stationAPosition._1
    val AB1 = stationBPosition._2 - stationAPosition._2

    //  Calculate distance between centers
    val c = sqrt(pow(AB0, 2) + pow(AB1, 2))

    //  Check if centers are valid
    if(c == 0){
      println("There is no distance between both center points")
      return intersection_list
    }

    val x = (pow(stationADistance, 2) + pow(c, 2) - pow(stationBDistance, 2)) / (2 * c)
    var y = pow(stationADistance, 2) - pow(x, 2)

    //  Check if intersection is available
    if(y < 0){
      println("There is no intersection available")
      return intersection_list
    }
    if(y > 0){
      y = sqrt(y)

      // Compute unit vectors ex and ey
      val ex0 = AB0 / c
      val ex1 = AB1 / c
      val ey0 = -ex1
      val ey1 =  ex0
      val Q1x = stationAPosition._1 + x * ex0
      val Q1y = stationAPosition._2 + x * ex1

      //  Check if there is a touch point available
      if(y == 0){
        println("There is only one touch point")
        return intersection_list
      }

      // Calculate intersections
      var Q2x = Q1x - y * ey0
      if(Q2x % 25 != 0) {
        val tmpValue = Q2x % 25
        if (tmpValue < 25 / 2) Q2x -= tmpValue else Q2x += (25 - tmpValue)
      }

      var Q2y = Q1y - y * ey1
      if(Q2y % 25 != 0) {
        val tmpValue = Q2y % 25
        if (tmpValue < 25 / 2) Q2y -= tmpValue else Q2y += (25 - tmpValue)
      }

      var Q0x = Q1x + y * ey0
      if(Q0x % 25 != 0) {
        val tmpValue = Q0x % 25
        if (tmpValue < 25 / 2) Q0x -= tmpValue else Q0x += (25 - tmpValue)
      }

      var Q0y = Q1y + y * ey1
      if(Q0y % 25 != 0) {
        val tmpValue = Q0y % 25
        if (tmpValue < 25 / 2) Q0y -= tmpValue else Q0y += (25 - tmpValue)
      }

      //  Fill list with intersections
      intersection_list ++= List((Q0x.toInt, Q0y.toInt))
      intersection_list ++= List((Q2x.toInt, Q2y.toInt))
    }
    intersection_list
  }

  def evaluateTriangulation(stationOne: List[(Int, Int)], stationTwo: List[(Int, Int)],
                            stationThree: List[(Int, Int)]): (Int, Int) = {

    stationOne.filter(x => stationTwo.contains(x) && stationThree.contains(x)) match {
      case List((a, b)) => return (a, b)
      case _ => return (-1, -1)
    }
    null
  }
}
