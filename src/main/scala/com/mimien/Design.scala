package com.mimien

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{TextField, Control, Label}
import scalafx.scene.layout.{VBox, Pane, HBox}

/**
  * @author emiliocornejo
  * @version 26/11/15
  *          @(#)Design.scala
  */
object Design {

  def labelInputBox(msg: String, input: Control) = {
    val label = new Label {
      text = msg
    }

    new HBox {
      alignment = Pos.Center
      spacing = 10
      children = List(label, input)
    }
  }

  implicit class BetterField(tf: TextField) {
    def isEmpty: Boolean = tf.text.value == ""
  }
}
