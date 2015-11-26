package com.mimien.scenes

import java.io.{InputStream, File}

import com.mimien.{Project, SPM, DB, User}
import com.mimien.Design._

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.{Pos, Insets}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.VBox
import scalafx.scene.text.Font

/**
  * @author emiliocornejo
  * @version 26/11/15
  *          @(#)CreateFile.scala
  */
object CreateFile {

  def apply(project: Project, user: User): Scene = {

    val msgLabel = new Label {
      text = "Select the file info"
      font = new Font("Helvetica", 22)
    }

    val nameField = new TextField {
      promptText = "Name of the file"
      minWidth = 200
      prefHeight = 35
    }

    val phaseChoiceBox = new ChoiceBox(ObservableBuffer("Analysis", "Design", "Implementation", "Testing")) {
      minWidth = 200
      prefHeight = 35
      selectionModel().selectFirst()
    }

    val formatChoiceBox = new ChoiceBox(ObservableBuffer("html", "scala")) {
      minWidth = 200
      prefHeight = 35
      selectionModel().selectFirst()
    }

    val createBtn = new Button("Create") {
      defaultButton = true
      onAction = { e: ActionEvent =>
        if (nameField.isEmpty) new Alert(AlertType.Error) {
          headerText = "Do not leave fields on blank"
        }.showAndWait()
        else {
          val name = nameField.text.value
          val format = formatChoiceBox.getSelectionModel.getSelectedItem
          val phase = phaseChoiceBox.getSelectionModel.getSelectedItem
          val projectId = project.id

          val file = new File(s"src/main/resources/files/$projectId/$name.$format")

          println(file.getParentFile.mkdirs())
          file.createNewFile()
          DB.insertFile(name, format, phase, projectId)
          SPM.changeSceneTo(ManageProject(project, user))
        }
      }
    }

    val centerPane = new VBox {
      spacing = 10
      padding = Insets(20)
      alignment = Pos.Center
      children = List(
        msgLabel,
        labelInputBox("     Name:", nameField),
        labelInputBox("   Format:", formatChoiceBox),
        labelInputBox("SE Phase:", phaseChoiceBox),
        createBtn
      )
    }
    MenuLayout(centerPane, user)
  }
}
