package com.mimien.scenes

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

import com.mimien.{Project, DB, User}

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{StackPane, VBox}
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

    val listPane = new StackPane {
      padding = Insets(20)
      children = new ListView[Project] {
        maxWidth = 200
        maxHeight = 200
        val userProjects = DB.listUserProjects(user id)
        items = ObservableBuffer(userProjects)
      }
    }

    val startPjBtn = new Button("Start") {
      defaultButton = true
      onAction = { e: ActionEvent =>
      }
    }

    val centerPane = new VBox {
      alignment = Pos.Center
      children = List(msgLabel, listPane, startPjBtn)
    }

    MenuLayout(centerPane, user)
  }
}
