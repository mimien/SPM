package com.mimien.scenes

import com.mimien._
import slick.model
import scalafx.Includes._
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer

import scalafx.event.{ActionEvent, Event}
import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle
import scalafx.scene.{Node, Scene}
import scalafx.scene.control._
import scalafx.scene.layout.{VBox, BorderPane}
import scalafx.scene.web.WebView

/**
  * @author emiliocornejo
  * @version 26/11/15
  *          @(#)Project.scala
  */
object ManageProject {

  def apply(project: Project, user: User): Scene = {

    val toolBar = new ToolBar {
      content = List(
        new Button {
          padding = Insets(4, 4, 4, 4)
          graphic = new ImageView(new Image(this, "/images/newfile.png"))
          tooltip = Tooltip("Create File... Ctrl+N")
          onAction = handle {
            SPM.changeSceneTo(CreateFile(project, user), "Create File")
          }
        },
        new Button {
          padding = Insets(4, 4, 4, 4)
          graphic = new ImageView(new Image(this, "/images/importfile.png"))
          tooltip = Tooltip("Import File Ctrl+I")
          onAction = handle {
            println("New toolbar button clicked")
          }
        },
        new Button {
          padding = Insets(4, 4, 4, 4)
          graphic = new ImageView(new Image(this, "/images/reload.png"))
          tooltip = Tooltip("Reload Ctrl+R")
          onAction = handle {
            println("New toolbar button clicked")
          }
        }
      )
    }

    def getFiles: ObservableBuffer[FileTable] = {
      val files = DB.getFilesFrom(project)
      if (files.isEmpty) ObservableBuffer()
      else ObservableBuffer(files.map { f => new FileTable(f.name, f.format, f.phase) })
    }

    val fileTableNode = {
      // Create columns
      val nameColumn = new TableColumn[FileTable, String] {
        text = "Name"
        cellValueFactory = {
          _.value.name_
        }
        prefWidth = 180
      }
      val formatColumn = new TableColumn[FileTable, String] {
        text = "Format"
        cellValueFactory = {
          _.value.format_
        }
        prefWidth = 180
      }
      val phaseColumn = new TableColumn[FileTable, String] {
        text = "Phase"
        cellValueFactory = {
          _.value.phase_
        }
        prefWidth = 180
      }

      // Create table
      val table = new TableView[FileTable](getFiles) {
        columns +=(nameColumn, formatColumn, phaseColumn)
      }

      // Listen to row selection, and print values of the selected row
      table.selectionModel().selectedItem.onChange(
        (_, _, newValue) => println(newValue + " chosen in TableView")
      )

      table
    }

    val centerPane = new TabPane {
      tabs = List(
        new Tab {
          text = "Files"
          content = fileTableNode
          closable = false
        },
        new Tab {
          text = "Status"
          closable = false
        }
      )
    }

    val borderPane = new BorderPane {
      top = toolBar
      center = centerPane
    }

    MenuLayout(borderPane, user)
  }

  class FileTable(name: String, format: String, phase: String) extends File(name, format, phase) {
    val name_ = new StringProperty(this, "name", name)
    val format_ = new StringProperty(this, "format", format)
    val phase_ = new StringProperty(this, "phase", phase)
  }

}
