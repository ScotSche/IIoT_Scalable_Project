package General.Model.Robot

case class MQTTData(topic: String, position: (Int, Int), timestamp: String){
  override def toString: String = position.toString() + "/" + timestamp
}
