package General.Model.Kafka

import General.Model.Robot.RobotPosition
import General.Model.Serializer.CustomSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.{KStream, KTable}
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}

import java.util.Properties

object Kafka extends App {

  val customSerde: CustomSerde = new CustomSerde

  val config: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "map-function-scala-example")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    p.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, customSerde.getClass)
    p
  }

  val builder = new StreamsBuilder

  val positionUpdates: KStream[String, RobotPosition] = builder.stream("robot_positioning_from_factory")
  val locationPerRobot: KTable[String, RobotPosition] = positionUpdates.toTable()
    .mapValues((_, v) => v)

  locationPerRobot.toStream().to("robot_positions_output_table")//, Produced.`with`(Serdes.String(), customSerde))

  println(builder.build().describe())

  val streams: KafkaStreams = new KafkaStreams(builder.build(), config)
  streams.cleanUp()
  streams.start()
}
