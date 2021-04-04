package Model

import java.awt.Image

case class Robot(image: Image, minPosition: RobotPosition, maxPosition: RobotPosition, var position: RobotPosition){
  def changeXPosition(xPositionDelta: Int): Unit ={
    val newXPosition = position.x + xPositionDelta
    if(newXPosition > minPosition.x && newXPosition < maxPosition.x) {
      position = RobotPosition(newXPosition, position.y)
    }
  }
  def changeYPosition(yPositionDelta: Int): Unit = {
    val newYPosition = position.y + yPositionDelta
    if(newYPosition > minPosition.y && newYPosition < maxPosition.y) {
      position = RobotPosition(position.x, newYPosition)
    }
  }
}
