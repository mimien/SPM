package com.mimien.scenes

import com.mimien.SPM

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{Menu, MenuBar, MenuItem}
import scalafx.scene.input.KeyCombination
import scalafx.scene.layout.{BorderPane, Pane}

/**
  * @author emiliocornejo
  * @version 19/11/15
  *          @(#)CreateMenus.scala
  */
object Menu {

  // menu lists: File, Edit
  private val fileMenu = new Menu("User") {
    items = List(
      new MenuItem("Log out") {
        accelerator = KeyCombination.keyCombination("Ctrl+Alt+L")
        onAction = (e: ActionEvent) => {
          SPM.stage.hide
          SPM.stage.scene = SPM.View.sc
          SPM.stage.show
        }
      }
    )
  }

  private val editMenu = new Menu("Edit") {
    items = List(
      new MenuItem("Cut"),
      new MenuItem("Copy"),
      new MenuItem("Paste")
    )
  }

  val menus = new MenuBar {
    menus = List(fileMenu, editMenu)
  }

  def layout(centerPane: Pane) = new Scene(800, 600) {
    root = new BorderPane {
      top = Menu.menus
      center = centerPane
    }
  }

}
