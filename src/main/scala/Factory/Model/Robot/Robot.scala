package Factory.Model.Robot

trait Robot {
  def name: String

  def minPosition: RobotPosition

  def maxPosition: RobotPosition
}
