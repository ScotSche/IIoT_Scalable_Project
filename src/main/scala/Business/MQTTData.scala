package Business

case class MQTTData(topic: String, position: (Int, Int), timestamp: String, var triangulation: String)
