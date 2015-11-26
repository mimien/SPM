package com.mimien.scenes

import com.mimien.{SPM, User}

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox
import scalafx.scene.text.Font

/**
  * @author emiliocornejo
  * @version 19/11/15
  *          @(#)AdminOptions.scala
  */
object AdminOptions {
  def apply(user: User): Scene = {

    val welcomeLabel = new Label {
      text = s"Welcome ${user name}!"
      font = new Font("Helvetica", 22)
    }

    val choosePjBtn = new Button("Choose Project") {
      prefHeight = 35
      maxWidth = 200
      onAction = { e: ActionEvent =>
        SPM.changeSceneTo(ChooseProject(user))
      }
    }

    val createUsrBtn = new Button("Create a User") {
      prefHeight = 35
      maxWidth = 200
      onAction = { e: ActionEvent =>
        SPM.changeSceneTo(CreateUser(user))
      }
    }

    val createPjBtn = new Button("Create a Project") {
      prefHeight = 35
      maxWidth = 200
      onAction = { e: ActionEvent =>
        SPM.changeSceneTo(CreateProject(user))
      }
    }

    val editPjBtn = new Button("Edit Project") {
      prefHeight = 35
      maxWidth = 200
      onAction = { e: ActionEvent =>
      }
    }
    val centerPane = new VBox {
      id = "admin"
      spacing = 10
      padding = Insets(20)
      alignment = Pos.Center
      children = List(welcomeLabel, choosePjBtn, createUsrBtn, createPjBtn, editPjBtn)
    }

    MenuLayout(centerPane, user)
  }

}
