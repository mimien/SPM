package com.mimien.scenes

import com.mimien.{SPM, DB}

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, StackPane, VBox}

/**
  * @author emiliocornejo
  * @version 19/11/15
  *          @(#)CreateProject.scala
  */
object CreateProject {
  def apply(): Scene = {

    val pjNameField = new TextField {
      promptText = "Name of the project"
      maxWidth = 250
    }

    val pjNameLabel = new Label {
      text = "Project name:"
    }

    val pjNameHBox = new HBox {
      alignment = Pos.Center
      padding = Insets(20)
      spacing = 10
      children = List(pjNameLabel, pjNameField)
    }

    val usersList1 = new ListView[String] {
      maxWidth = 200
      maxHeight = 200
      items = ObservableBuffer(DB.listUsers)
    }

    val usersPane1 = new StackPane {
      padding = Insets(20)
      children = usersList1
    }

    val usersList2 = new ListView[String] {
      maxWidth = 200
      maxHeight = 200
    }

    val usersPane2 = new StackPane {
      padding = Insets(20)
      children = usersList2
    }

    val addBtn = new Button(">") {
      onAction = { e: ActionEvent =>

        val item = usersList1.getSelectionModel.getSelectedItem

        if (item == null) new Alert(AlertType.Error) {
          headerText = "Select a user from the left first to add him to the project"
        }.showAndWait()
        else {
          usersList1.getItems.remove(item)
          usersList2.getItems.add(item)
        }
      }
    }

    val removeBtn = new Button("<") {
      onAction = { e: ActionEvent =>
        val item = usersList2.getSelectionModel.getSelectedItem

        if (item == null) new Alert(AlertType.Error) {
          headerText = "Select a user from the right first to remove him from the project"
        }.showAndWait()
        else {
          usersList2.getItems.remove(item)
          usersList1.getItems.add(item)
        }
      }
    }

    val createBtn = new Button("Create") {
      onAction = { e: ActionEvent =>
        if (pjNameField.text.value == "") {//FIXME add implict methods to this class too
          new Alert(AlertType.Error) {
            headerText = "Do not leave fields on blank"
          }.showAndWait()
        } else if (usersList2.getItems.isEmpty) {
          new Alert(AlertType.Error) {
            headerText = "Add a user to the project first"
          }.showAndWait()
        } else {
          val projectName = pjNameField.text.value
          val result = DB.addProject(projectName)
          println(result)
          val users = usersList2.getItems.toSeq
          for (u <- users) DB.relateProjectUser(projectName, u)
          SPM.stage.hide()
          SPM.stage.scene = AdminOptions("emilio") // FIXME
          SPM.stage.show()
        }
      }
    }

    val usersHBox = new HBox {
      alignment = Pos.Center
      padding = Insets(20)
      spacing = 10
      children = List(usersPane1, addBtn, removeBtn, usersPane2)
    }

    val centerPane = new VBox {
      spacing = 20
      padding = Insets(30)
      alignment = Pos.Center
      children = List(pjNameHBox, usersHBox, createBtn)
    }
    Menu.layout(centerPane)
  }
}
