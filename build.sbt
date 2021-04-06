name := "IIoT_Scalable_Project"
version := "0.1"
scalaVersion := "2.12.11"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"

val AkkaVersion = "2.6.5"
libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-mqtt" % "2.0.2",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion
)

libraryDependencies += "org.apache.kafka" %% "kafka" % "2.6.0"

coverageExcludedPackages := ".*ControllerMain.*;.*DashboardMain.*;.*RobotMain.*"
