package com.mimien.scenes

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

import com.mimien.{SPM, Project, DB, User}

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.{Orientation, Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle
import scalafx.scene.text.Font

/**
  * @author emiliocornejo
  * @version 06/11/15
  *          @(#)Session.scala
  */
object ChooseProject {

  def apply(user: User): Scene = {

    val msgLabel = new Label {
      text = "Choose a Project!"
      font = new Font("Helvetica", 22)
    }

    val projectsList = new ListView[Project] {
      maxWidth = 200
      maxHeight = 200
      val userProjects = DB.listUserProjects(user id)
      items = ObservableBuffer(userProjects)
    }

    val listPane = new StackPane {
      padding = Insets(20)
      children = projectsList
    }

    val startPjBtn = new Button("Start") {
      defaultButton = true
      onAction = { e: ActionEvent =>
        val project = projectsList.getSelectionModel.getSelectedItem
        if (project == null) new Alert(AlertType.Error) {
          headerText = "Select a project first"
        }.showAndWait()
        else SPM.changeSceneTo(ManageProject(project, user), "Project - " + project.name)
      }
    }

    val centerPane = new VBox {
      alignment = Pos.Center
      children = List(msgLabel, listPane, startPjBtn)
    }

    MenuLayout(centerPane, user)
  }
}
