package com.mimien.scenes

import com.mimien.SPM

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Label, Button}
import scalafx.scene.layout.VBox
import scalafx.scene.text.Font

/**
  * @author emiliocornejo
  * @version 19/11/15
  *          @(#)AdminOptions.scala
  */
object AdminOptions {
  def apply(user: String): Scene = {

    val welcomeLabel = new Label {
      text = s"Welcome $user!"
      font = new Font("Helvetica", 22)
    }

    val choosePjBtn = new Button("Choose Project") {
      prefHeight = 35
      maxWidth = 200
      onAction = { e: ActionEvent =>
        SPM.stage.hide()
        SPM.stage.scene = ChooseProject(user)
        SPM.stage.show()
      }
    }

    val createUsrBtn = new Button("Create a User") {
      prefHeight = 35
      maxWidth = 200
      onAction = { e: ActionEvent =>
        SPM.stage.scene = CreateUser()
      }
    }

    val createPjBtn = new Button("Create a Project") {
      prefHeight = 35
      maxWidth = 200
      onAction = { e: ActionEvent =>
        SPM.stage.scene = CreateProject()
      }
    }

    val editPjBtn = new Button("Edit Project") {
      prefHeight = 35
      maxWidth = 200
      onAction = { e: ActionEvent =>
        SPM.stage.scene = CreateUser()
      }
    }
    val centerPane = new VBox {
      spacing = 10
      padding = Insets(20)
      alignment = Pos.Center
      children = List(welcomeLabel, choosePjBtn, createUsrBtn, createPjBtn, editPjBtn)
    }

    Menu.layout(centerPane)
  }

}
