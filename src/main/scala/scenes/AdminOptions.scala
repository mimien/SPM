package scenes

import scalafx.scene.Scene
import scalafx.scene.layout.{VBox, BorderPane}

/**
  * @author emiliocornejo
  * @version 19/11/15
  *          @(#)AdminOptions.scala
  */
object AdminOptions {
  def apply(user: String): Scene = {
    new Scene(800, 600) {
      root = new BorderPane {
        top = new VBox {
          children = CreateMenus
        }
        center = centerPane
      }
    }
  }

}
