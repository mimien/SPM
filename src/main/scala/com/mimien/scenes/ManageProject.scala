package com.mimien.scenes

import com.mimien.{DB, File, Project, User}
import slick.model
import scalafx.Includes._
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer

import scalafx.event.Event
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{TableView, TableColumn, Tab, TabPane}
import scalafx.scene.layout.{VBox, BorderPane}
import scalafx.scene.web.WebView

/**
  * @author emiliocornejo
  * @version 26/11/15
  *          @(#)Project.scala
  */
object ManageProject {

  def apply(project: Project, user: User): Scene = {

    def getFiles: ObservableBuffer[FileTable] = {
      val files = DB.getFilesFrom(project)
      if (files.isEmpty) ObservableBuffer()
      else ObservableBuffer(files.map(_.asInstanceOf[FileTable]))
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
    MenuLayout(centerPane, user)
  }

  class FileTable(name: String, format: String, phase: String) extends File(name, format, phase) {
    val name_ = new StringProperty(this, "name", name)
    val format_ = new StringProperty(this, "format", format)
    val phase_ = new StringProperty(this, "phase", phase)
  }

}
