package Dashboard

import Dashboard.Controller.DashboardController
import Dashboard.View.FactoryDashboard

object DashboardMain {

  //  Views
  val dashBoardView: FactoryDashboard = new FactoryDashboard()

  //  Controller
  val controller: DashboardController = new DashboardController(dashBoardView)

  def main(args: Array[String]) = {
    dashBoardView.visible = true
    controller.start()
  }
}
