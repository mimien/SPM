package com.mimien.scenes

import com.mimien.{User, SPM, DB}
import com.mimien.Design._

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, StackPane, VBox}
import scalafx.scene.text.Font

/**
  * @author emiliocornejo
  * @version 19/11/15
  *          @(#)CreateProject.scala
  */
object CreateProject {
  def apply(user: User): Scene = {

    val msgLabel = new Label {
      text = "Choose the project info"
      font = new Font("Helvetica", 22)
    }

    val pjNameField = new TextField {
      promptText = "Name of the project"
      maxWidth = 250
    }

    val usersList1 = new ListView[User] {
      maxWidth = 200
      maxHeight = 200
      items = ObservableBuffer(DB.listUsers)
    }

    val usersPane1 = new StackPane {
      padding = Insets(20)
      children = usersList1
    }

    val usersList2 = new ListView[User] {
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
      defaultButton = true
      onAction = { e: ActionEvent =>
        if (pjNameField.isEmpty) {
          new Alert(AlertType.Error) {
            headerText = "Do not leave fields on blank"
          }.showAndWait()
        } else if (usersList2.getItems.isEmpty) {
          new Alert(AlertType.Error) {
            headerText = "Add a user to the project first"
          }.showAndWait()
        } else {
          val projectName = pjNameField.text.value
          val projectId = DB.addProject(projectName)

          println(projectId.get)

          val users = usersList2.getItems.toSeq
          for (u <- users) DB.relateProjectUser(Some(projectId.get), u.id)

          SPM.changeSceneTo(AdminOptions(user))
        }
      }
    }

    val addRemoveHBox = new VBox {
      alignment = Pos.Center
      //      padding = Insets(20)
      spacing = 10
      children = List(addBtn, removeBtn)
    }

    val usersHBox = new HBox {
      alignment = Pos.Center
      padding = Insets(20)
      spacing = 10
      children = List(usersPane1, addRemoveHBox, usersPane2)
    }

    val centerPane = new VBox {
      spacing = 20
      padding = Insets(30)
      alignment = Pos.Center
      children = List(msgLabel, labelInputBox("Project name:", pjNameField), usersHBox, createBtn)
    }
    MenuLayout(centerPane, user)
  }
}