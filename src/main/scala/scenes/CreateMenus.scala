package scenes

import scalafx.event.ActionEvent
import scalafx.scene.control.{MenuBar, MenuItem, Menu}
import scalafx.scene.input.KeyCombination
import scalafx.Includes._

/**
  * @author emiliocornejo
  * @version 19/11/15
  *          @(#)CreateMenus.scala
  */
abstract class CreateMenus {

  // menu lists: File, Edit
  val fileMenu = new Menu("File") {
    items = List(
      new MenuItem("New Project") {
        accelerator = KeyCombination.keyCombination("Ctrl +N")
        onAction = (e: ActionEvent) => {
        }
      }
    )
  }

  val editMenu = new Menu("Edit") {
    items = List(
      new MenuItem("Cut"),
      new MenuItem("Copy"),
      new MenuItem("Paste")
    )
  }

  val createMenus = new MenuBar {
    useSystemMenuBar = true
    menus = List(fileMenu, editMenu)
  }
}
