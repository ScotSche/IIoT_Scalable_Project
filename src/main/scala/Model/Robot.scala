package Model

import java.awt.Image

case class Robot(image: Image, minPosition: RobotPosition, maxPosition: RobotPosition, var position: RobotPosition){
  def changePosition(newPosition: RobotPosition): Unit ={
    if((newPosition.x > minPosition.x && newPosition.x < maxPosition.x) &&
      (newPosition.y > minPosition.y && newPosition.y < maxPosition.y)){
      position = newPosition
    }
  }
}
