package Robots

case class RobotDataTransformation(robotName: String, robotDistance: Double, timestamp: String,
                                   stationName: String, stationPosition: (Int, Int), triangulationData: String)
