package com.mimien.scenes

import com.mimien.{User, SPM, DB}

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.geometry.{Pos, Insets}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.VBox
import scalafx.scene.text.Font

/**
  * @author emiliocornejo
  * @version 20/11/15
  *          @(#)CreateUser.scala
  */
object CreateUser {

  def apply(user: User): Scene = {

    val msgLabel = new Label {
      text = "Fill the user info"
      font = new Font("Helvetica", 22)
    }

    val usernameField = new TextField {
      promptText = "User"
      maxWidth = 250
      prefHeight = 35
    }

    val passwordField = new PasswordField() {
      promptText = "Password"
      maxWidth = 250
      prefHeight = 35
    }

    val pswd2Field = new PasswordField() {
      promptText = "Password"
      maxWidth = 250
      prefHeight = 35
    }


    val createBtn = new Button("Create") {
      defaultButton = true
      onAction = { e: ActionEvent =>
        if (usernameField.isEmpty || passwordField.isEmpty || pswd2Field.isEmpty) new Alert(AlertType.Error) {
          headerText = "Do not leave fields on blank"
        }.showAndWait()
        else if (passwordField.text.value != pswd2Field.text.value) new Alert(AlertType.Error) {
          headerText = "Password do not match"
        }.showAndWait()
        else {
          DB.insertUser(usernameField.text.value, pswd2Field.text.value)
          SPM.changeSceneTo(AdminOptions(user))
        }
      }
    }

    val centerPane = new VBox {
      spacing = 10
      padding = Insets(20)
      alignment = Pos.Center
      children = List(msgLabel, usernameField, passwordField, pswd2Field, createBtn)
    }
    MenuLayout(centerPane, user)
  }

  implicit class BetterField(tf: TextField) {
    def isEmpty: Boolean = tf.text.value == ""
  }

}
