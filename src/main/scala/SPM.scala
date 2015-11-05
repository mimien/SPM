import scala.concurrent.{ExecutionContext, Future}
import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.{JFXApp, Platform}
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}


/**
  * @author emiliocornejo
  * @version 06/10/15
  *          @(#)SPM.scala
  */
object SPM extends JFXApp {

  /*
  * Primary stage: Log in
  * */
  stage = new PrimaryStage {
    // error message hidden label
    val errorLabel = new Label()
    errorLabel.textFill = Color.Red
    errorLabel.font = Font.font("Helvetica", FontWeight.ExtraLight, 12)

    val usernameField = new TextField {
      promptText = "User"
      maxWidth = 250
      prefHeight = 35
    }

    val passwordField = new PasswordField() {
      promptText = "Password"
      maxWidth = 250
      prefHeight = 35
    }

    val progressBar = new ProgressBar {
      maxWidth = 300
      visible = false
    }
    title = "Software Project Management"
    scene = new Scene(800, 600) {
      root = new VBox { _root =>
        spacing = 10
        padding = Insets(20)
        alignment = Pos.Center
        children = List(
          new ImageView {
            image = new Image(
              this.getClass.getResourceAsStream("/images/logo.png"))
            margin = Insets(0, 0, 20, 0)
          },
          new Label {
            text = "Software Project Management"
            font = Font.font("Helvetica", FontWeight.ExtraLight, 32)
          },
          new Label {
            text = "Sign in to get started"
            font = Font.font("Helvetica", FontWeight.Thin, 18)
          },
          errorLabel,
          progressBar,
          usernameField,
          passwordField,
          new Button {
            text = "Enter"
            defaultButton = true
            prefHeight = 35
            font = Font.font("Helvetica", FontWeight.Thin, 18)
            maxWidth = 250
            onAction = (ae: ActionEvent) => {
              progressBar.visible = true
              _root.disable = true
              implicit val ec = ExecutionContext.global
              val passwordQuery: Future[String] = Future(DB.checkPassword(usernameField.text.value.toLowerCase))

              passwordQuery.onSuccess {
                case password =>
                  if (password == passwordField.text.value) root = chooseProject
                  else {
                    val msg: String = if (password == null) "That username doesnt exist"
                    else "Your password is incorrect. Please re-enter your password"
                    Platform.runLater {
                      errorLabel.text = msg
                      passwordField.text = ""
                      progressBar.visible = false
                      _root.disable = false
                    }
                  }
              } // query on success
            } // button action event
          }
        ) // children list
        styleClass += "application"
      } // root
    } // scene
  }

  // Primary stage

  def chooseProject = {
    new VBox {
      alignment = Pos.Center
      children = List(
        new Label {
          text = "Welcome!"
          font = new Font("Helvetica", 22)
        }
      )
    }
  }
}