package com.mimien

import com.mimien.scenes._
/**
  * @author emiliocornejo
  * @version 06/11/15
  *          @(#)Session.scala
  */
object Session {

  def apply(user: User) {
    if (user isAdmin) SPM.changeSceneTo(AdminOptions(user))
    else SPM.changeSceneTo(ChooseProject(user), s"Welcome ${user name}!")
  }
}
