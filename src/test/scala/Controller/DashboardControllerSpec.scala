package Controller

import View.FactoryDashboard
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DashboardControllerSpec extends AnyWordSpec with Matchers {
  "A dashboard controller" when {
    "initialized" should {
      val dashboardView = new FactoryDashboard
      val target = new DashboardController(dashboardView)

      "should have an empty robot positions map" in {
        target.robotPositions.isEmpty should be(true)
      }
      "should habe a Apache kafka consumer" in {
        target.kafka_Consumer should not be(null)
      }

      "should be able to force update the robot positions in view" in {
        target.updateRobotPositions()
      }
    }
  }
}
