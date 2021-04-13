package General.Model

import java.awt.event.ActionEvent
import javax.swing.{AbstractAction, Timer}

object Timer {
  def apply(interval: Int, repeats: Boolean = true)(op: => Unit): Unit ={
    val timeOut = new AbstractAction() {
      override def actionPerformed(e: ActionEvent): Unit = op
    }
    val t = new Timer(interval, timeOut)
    t.setRepeats(repeats)
    t.start()
  }
}
