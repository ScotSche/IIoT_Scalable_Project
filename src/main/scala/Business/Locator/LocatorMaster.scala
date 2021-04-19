package Business.Locator

import Bridge.Model.RobotDataTransformation
import Business.{MQTTData, Triangulation}
import Factory.Model.MQTT_Client

import scala.util.parsing.json.JSON


class LocatorMaster(name: String) {

  val mqttRobotClient: MQTT_Client = new MQTT_Client(name)
  val triangulation: Triangulation = new Triangulation

  type TriangulationData = (String, (Int, Int), Double)
  val locationData: List[TriangulationData] = List()

  val stations: List[Locator] = List(new Locator("Station 1", (0, 0), this), new Locator("Station 2", (900, 0), this),
    new Locator("Station 3", (450, 900), this))

  def gatherManualRobotDataFromLocator(): Unit = {
    var robotMasterList: List[RobotDataTransformation] = List()
    stations.foreach(locator => {
      val data = locator.pull_data()
      val jsonObject = deserializeJSON(data)
      val robotData = getRobotData(jsonObject)
      robotMasterList ++= robotData
    })
    val extractionForManualMode = robotMasterList.map(x => (x.stationPosition.toString(), x.robotDistance))
    val sortedDataMap = sortDataToRobots(robotMasterList)
    val manualRobotPosition = triangulationOfRobotData(sortedDataMap, "robot_manual")

    var tmpString = ""
    extractionForManualMode.foreach(x => tmpString += x._1 + ":" + x._2.toString + ";")
    manualRobotPosition.triangulation = tmpString

    mqtt_publish(manualRobotPosition.topic, createMQTTPayload(manualRobotPosition))
  }

  def gatherAutonomousRobotDataFromLocator(): Unit = {
    var robotMasterList: List[RobotDataTransformation] = List()
    stations.foreach(locator => {
      val data = locator.pull_data()
      val jsonObject = deserializeJSON(data)
      val robotData = getRobotData(jsonObject)
      robotMasterList ++= robotData
    })
    val sortedDataMap = sortDataToRobots(robotMasterList)
    val robotOnePositionList = triangulationOfRobotData(sortedDataMap, "robot_one")
    val robotTwoPositionList = triangulationOfRobotData(sortedDataMap, "robot_two")
    val robotThreePositionList = triangulationOfRobotData(sortedDataMap, "robot_three")
    val robotFourPositionList = triangulationOfRobotData(sortedDataMap, "robot_four")

    val positionList = List(robotOnePositionList, robotTwoPositionList, robotThreePositionList, robotFourPositionList)
    positionList.foreach(robot => {
      if(robot.position._1 != -1 && robot.position._2 != -1) mqtt_publish(robot.topic, createMQTTPayload(robot))
      Thread.sleep(50)
    })
  }

  def deserializeJSON(jsonString: String): Map[String, Any] ={
    val result = JSON.parseFull(jsonString)
    result match {
      case x: Some[Map[String, Any]] => x.value
      case None => Map()
    }
  }

  def getRobotData(data: Map[String, Any]): List[RobotDataTransformation] ={
    var stationToRobotList: List[RobotDataTransformation] = List()
    val robotMap = data.get("mqttdata") match {
      case Some(x: Map[String, Any]) => x
    }
    val stationName = robotMap.get("name") match {
      case Some(x: String) => x
    }
    val stationPosition = robotMap.get("position") match {
      case Some(x: String) => x.substring(1).substring(0, x.length - 2).split(",").toList.map(x => x.toInt) match {
        case List(a, b) => (a, b)
      }
    }

    val robotList = robotMap.get("robotdata") match {
      case Some(x: List[Map[String, Any]]) => x
    }
    robotList.foreach(robotData => {
      var roboName: String = ""
      var distance: Double = 0.0
      var timestamp: String = ""
      robotData.get("name") match {
        case Some(x: String) =>  roboName = x
      }
      robotData.get("distance") match {
        case Some(x: Any) => distance =  x.toString.toDouble
      }
      robotData.get("timestamp") match {
        case Some(x: Any) => timestamp =  x.toString
      }
      stationToRobotList ++= List(RobotDataTransformation(roboName, distance, timestamp, stationName, stationPosition, null))
    })
    stationToRobotList
  }

  def sortDataToRobots(data: List[RobotDataTransformation]): Map[String, List[RobotDataTransformation]] ={
    var dataMap: Map[String, List[RobotDataTransformation]] = Map()
    data.foreach(robotData => {
      if(dataMap.contains(robotData.robotName)){
        val tmpList: List[RobotDataTransformation] = dataMap(robotData.robotName) ++ List(robotData)
        dataMap ++= Map(robotData.robotName -> tmpList)
      }
      else{
        dataMap ++= Map(robotData.robotName -> List(robotData))
      }
    })
    dataMap
  }

  def triangulationOfRobotData(data: Map[String, List[RobotDataTransformation]], name: String): MQTTData ={
    val specificRobotData = data(name)

    val stationOne = specificRobotData.filter(x => x.stationName.equals("Station 1")) match {
      case List(x: RobotDataTransformation) => x
    }
    val stationTwo = specificRobotData.filter(x => x.stationName.equals("Station 2")) match {
      case List(x: RobotDataTransformation) => x
    }
    val stationThree = specificRobotData.filter(x => x.stationName.equals("Station 3")) match {
      case List(x: RobotDataTransformation) => x
    }

    val triangulationOne = triangulation.triangulation(stationOne.stationPosition, stationOne.robotDistance,
      stationTwo.stationPosition, stationTwo.robotDistance)

    val triangulationTwo = triangulation.triangulation(stationTwo.stationPosition, stationTwo.robotDistance,
      stationThree.stationPosition, stationThree.robotDistance)

    val triangulationThree = triangulation.triangulation(stationOne.stationPosition, stationOne.robotDistance,
      stationThree.stationPosition, stationThree.robotDistance)

    MQTTData(name, triangulation.evaluateTriangulation(triangulationOne, triangulationTwo, triangulationThree), stationOne.timestamp, null)
  }

  def createMQTTPayload(robot: MQTTData): String = s"""
  |{
  |  "mqttdata": {
  |    "topic": "${robot.topic}",
  |    "position": [${robot.position._1} , ${robot.position._2}],
  |    "timestamp": "${robot.timestamp}",
  |    "triangulation": "${robot.triangulation}"
  |  }
  |}""".stripMargin.trim.replaceAll("\r\n", " ")

  def mqtt_publish(name: String, payload: String): Unit = {
    mqttRobotClient.publish(name, payload)
  }
}
