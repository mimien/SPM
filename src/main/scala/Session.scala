import scenes._

import scalafx.scene.Scene

/**
  * @author emiliocornejo
  * @version 06/11/15
  *          @(#)Session.scala
  */
object Session {

  def apply(name: String, isAdmin: Boolean): Scene = {
    if (isAdmin) {

    }
    ChooseProject(name)
  }
}