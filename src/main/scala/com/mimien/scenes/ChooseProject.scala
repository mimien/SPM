package com.mimien.scenes

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, StackPane, VBox}
import scalafx.scene.text.Font

/**
  * @author emiliocornejo
  * @version 06/11/15
  *          @(#)Session.scala
  */
object ChooseProject {
  def apply(user: String): Scene = {

    val welcomeLabel = new Label {
      text = s"Welcome $user!"
      font = new Font("Helvetica", 22)
    }

    val listPane = new StackPane {
      padding = Insets(20)
      children = new ListView[String] {
        onMouseClicked = new EventHandler[MouseEvent] {
          override def handle(event: MouseEvent) {

          }
        }
        maxWidth = 200
        maxHeight = 200
        items = ObservableBuffer("Project 1", "Project 2")
      }
    }

    val startPjBtn = new Button("Start") {
      onAction = { e: ActionEvent =>
        println(e.eventType + " occurred on Button")
      }
    }

    val centerPane = new VBox {
      alignment = Pos.Center
      children = List(welcomeLabel, listPane, startPjBtn)
    }

    new Scene(800, 600) {
      root = new BorderPane {
        top = new VBox(children = Create.menus)
        center = centerPane
      }
    }
  }
}
