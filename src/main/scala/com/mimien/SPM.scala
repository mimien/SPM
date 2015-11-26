package com.mimien

import scalafx.Includes._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.{Future, ExecutionContext}
import scalafx.application.{Platform, JFXApp}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}

/**
  * @author emiliocornejo
  * @version 06/10/15
  *          @(#)SPM.scala
  */
object SPM extends JFXApp {

  stage = new PrimaryStage {
    minWidth = 400
    minHeight = 400
    title = "Software Project Management"
    scene = View.login
  }

  def changeSceneTo(scene: Scene, title: String = "Admin Options"): Unit = {
    stage.hide()
    stage.scene = scene
    stage.title = title
    stage.show()
  }

  object View {

    val logoImg = new ImageView {
      image = new Image(
        this.getClass.getResourceAsStream("/images/logo.png"))
      margin = Insets(0, 0, 20, 0)
    }

    val titleLabel = new Label {
      text = "Software Project Management"
      font = Font.font("Helvetica", FontWeight.ExtraLight, 32)
    }

    val sbtitleLabel = new Label {
      text = "Sign in to get started"
      font = Font.font("Helvetica", FontWeight.Thin, 18)
    }

    val msgLabel = new Label {
      textFill = Color.Red
      font = Font.font("Helvetica", FontWeight.ExtraLight, 14)
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

    val progressBar = new ProgressBar {
      maxWidth = 300
      visible = false
    }

    val loginBtn = new Button {
      text = "Enter"
      defaultButton = true
      prefHeight = 35
      font = Font.font("Helvetica", FontWeight.Thin, 18)
      maxWidth = 250
    }

    val login: Scene = new Scene(600, 600) {
      root = new VBox { _root =>
        spacing = 10
        padding = Insets(20)
        alignment = Pos.Center
        children = List(logoImg, titleLabel, sbtitleLabel, msgLabel, progressBar, usernameField, passwordField, loginBtn)

        // hookup login button event
        loginBtn.onAction = (ae: ActionEvent) => {
          progressBar.visible = true
          _root.disable = true

          val username = usernameField.text.value.toLowerCase
          val f: Future[User] = Future(DB.login(username)) (ExecutionContext.global)
          f.onSuccess { case user =>
            // reset control inputs to default
            Platform runLater {
              passwordField.text = ""
              progressBar.visible = false
              _root.disable = false
            }

            if (user != null) {
              if (user.password == passwordField.text.value) Platform runLater Session(user)
              else Platform runLater (msgLabel.text = "Your password is incorrect. Please re-enter your password")
            }
            else Platform runLater (msgLabel.text = "That username doesnt exist")
          } // query on success
        } // button action event
      } // root
    } // scene
  } // Object View
}