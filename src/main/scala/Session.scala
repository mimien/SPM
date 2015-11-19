import scenes._

/**
  * @author emiliocornejo
  * @version 06/11/15
  *          @(#)Session.scala
  */
object Session {

  def apply(name: String, admin: Boolean) = {
    chooseProject(name)
  }
}