import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Failure}
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


  stage = View.login

  object View {
    // error message hidden
    val msgLabel = new Label {
      textFill = Color.Red
      font = Font.font("Helvetica", FontWeight.ExtraLight, 14)
    }

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

    val logoImg = new ImageView {
      image = new Image(
        this.getClass.getResourceAsStream("/images/logo.png"))
      margin = Insets(0, 0, 20, 0)
    }

    val titleLabel = new Label {
      text = "Software Project Management"
      font = Font.font("Helvetica", FontWeight.ExtraLight, 32)
    }

    val sbtitleLabel = new Label {
      text = "Sign in to get started"
      font = Font.font("Helvetica", FontWeight.Thin, 18)
    }
    val sc: Scene = new Scene(600, 600) {
      root = new VBox {
        _root =>
        spacing = 10
        padding = Insets(20)
        alignment = Pos.Center
        children = List(
          logoImg,
          titleLabel,
          sbtitleLabel,
          msgLabel,
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
              val username = usernameField.text.value.toLowerCase
              // get user password from database through future
              implicit val ec = ExecutionContext.global
              val f: Future[(String, Boolean)] = Future(
                DB.login(username)
              )
              f.onComplete {
                case Success(password, admin) => {
                  println(s"$password and $admin")
                  if (password == passwordField.text.value) {
                    Platform.runLater {
                      stage.hide()
                      stage.scene = Session(username, admin)
                      stage.title = "Choose Project"
                      stage.show()

                    }
                  }
                  else {
                    val msg: String = if (password == null) "That username doesnt exist"
                    else "Your password is incorrect. Please re-enter your password"
                    Platform.runLater {
                      msgLabel.text = msg
                      passwordField.text = ""
                      progressBar.visible = false
                      _root.disable = false
                    }
                  }
                }
                case Failure(e) => {

                }

              } // query on success
            } // button action event
          }
        ) // children list
      } // root
    } // scene

    /*
* Primary stage: Log in
* */
    val login = new PrimaryStage {
      title = "Software Project Management"
      scene = sc
    }
  }

}