package com.mimien.scenes

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{PasswordField, TextField}
import scalafx.scene.layout.VBox

/**
  * @author emiliocornejo
  * @version 19/11/15
  *          @(#)CreateProject.scala
  */
object CreateProject {
  def apply(): Scene = {

    val usernameField = new TextField {
      promptText = "Name of the project"
      maxWidth = 250
      prefHeight = 35
    }

    val passwordField = new PasswordField() {
      promptText = "Password"
      maxWidth = 250
      prefHeight = 35
    }

    val centerPane = new VBox {
      spacing = 20
      padding = Insets(30)
      alignment = Pos.Center
      children = List(usernameField, passwordField)
    }
    Menu.layout(centerPane)
  }
}
