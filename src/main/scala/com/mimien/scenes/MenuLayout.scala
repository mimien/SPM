package com.mimien.scenes

import com.mimien.{SPM, User}

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{Menu, MenuBar, MenuItem}
import scalafx.scene.input.KeyCombination
import scalafx.scene.layout.{Region, BorderPane, Pane}

/**
  * @author emiliocornejo
  * @version 19/11/15
  *          @(#)CreateMenus.scala
  */
object MenuLayout {
  def apply(centerPane: Region, user: User): Scene = {

    // Redirect
    val logoutMenu = new MenuItem("Log out") {
      accelerator = KeyCombination.keyCombination("Ctrl+Alt+L")
      onAction = (e: ActionEvent) => {
        SPM.changeSceneTo(SPM.View.login, "Software Project Management")
      }
    }

    // Redirect
    val adminMenu = new MenuItem("Admin Options") {
      accelerator = KeyCombination.keyCombination("Ctrl+Alt+O")
      onAction = (e: ActionEvent) => {
        SPM.changeSceneTo(AdminOptions(user))
      }

      // disable if the user is not admin or if the currente scene is admin options
      disable = user.isNotAdmin || centerPane.getId == "admin"
    }

    val userMenu = new Menu("User") {
      items = List(logoutMenu, adminMenu)
    }

    val editMenu = new Menu("Edit") {
      items = List(
        new MenuItem("Cut"),
        new MenuItem("Copy"),
        new MenuItem("Paste")
      )
    }

    val bar = new MenuBar {
      menus = List(userMenu, editMenu)
    }

    new Scene(800, 600) {
      root = new BorderPane {
        top = bar
        center = centerPane
      }
    }
  }
}
