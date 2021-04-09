package Model.Serializer

class Triangulation {

  /*def calculateTriangulation(): Unit ={

    var radii: List[Double] = List()
    stations.foreach(station => {

      //  Calculate Delta of Coordinates
      var deltaX = manual_Robot_Position.x - station._1
      var deltaY = manual_Robot_Position.y - station._2
      if(deltaX < 0) deltaX *= -1
      if(deltaY < 0) deltaY *= -1
      val deltaCoordinates = (deltaX, deltaY)

      //  Pythagoras to calculate distance between points
      radii ++= List(sqrt(pow(deltaCoordinates._1, 2) + pow(deltaCoordinates._2, 2)))
    })

    //  Make triangulation for every position
    val station_one_two_List = triangulation(0, 1, radii)
    val station_two_three_List = triangulation(1, 2, radii)
    val station_three_one_List = triangulation(0, 2, radii)

    //  Compare triangulations and find correct position of robot
    var triangulationSuccessful = false
    station_one_two_List.foreach(position => {
      if(station_two_three_List.contains(position) && station_three_one_List.contains(position)){
        triangulationSuccessful = true
        manual_Robot_Position = RobotPosition(position._1, position._2, LocalDateTime.now().toString)
      }
    })

    //  Check if triangulation was successful
    if(triangulationSuccessful) manual_Robot.mqtt_publish(manual_Robot_Position) else println("No unique position available")
  }

  def triangulation(stationOne: Int, stationTwo: Int, radii: List[Double]): List[(Int, Int)] ={

    var intersection_list: List[(Int, Int)] = List()

    //  Calculate center point deltas Station 0 & Station 1
    val AB0 = stations.apply(stationTwo)._1 - stations.apply(stationOne)._1
    val AB1 = stations.apply(stationTwo)._2 - stations.apply(stationOne)._2

    //  Calculate distance between centers
    val c = sqrt(pow(AB0, 2) + pow(AB1, 2))

    //  Check if centers are valid
    if(c == 0){
      println("There is no distance between both center points")
      return intersection_list
    }

    val x = (pow(radii.apply(stationOne), 2) + pow(c, 2) - pow(radii.apply(stationTwo), 2)) / (2 * c)
    var y = pow(radii.apply(stationOne), 2) - pow(x, 2)

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
      val Q1x = stations.apply(stationOne)._1 + x * ex0
      val Q1y = stations.apply(stationOne)._2 + x * ex1

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
  }*/
}
