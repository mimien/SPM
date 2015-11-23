package com.mimien.scenes

import com.mimien.DB

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, ListView, TextField}
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
        if (item == null)
        usersList1.getItems.remove(item)
        usersList2.getItems.add(item)
      }
    }

    val removeBtn = new Button("<") {
      onAction = { e: ActionEvent =>
        val item = usersList2.getSelectionModel.getSelectedItem
        usersList2.getItems.remove(item)
        usersList1.getItems.add(item)
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
      children = List(pjNameHBox, usersHBox)
    }
    Menu.layout(centerPane)
  }
}
