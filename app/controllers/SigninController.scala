package controllers
import javax.inject.{Inject, Singleton}
import models.{UserForms, UserInfoRepo}
import play.api.i18n.I18nSupport
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global


import scala.concurrent.Future


@Singleton
class SigninController @Inject()(userForms: UserForms, cc: ControllerComponents, userInfoRepo: UserInfoRepo) extends AbstractController(cc) with I18nSupport {
  implicit val message = cc.messagesApi

  def loginForm() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signin(userForms.userSignInForm))
  }

  def handleLogin() = Action.async{
    implicit request =>
      userForms.userSignInForm.bindFromRequest().fold(
        formWithError => {
          Future.successful(BadRequest(views.html.signin(formWithError)))
        },
        data => {
          for {
            isAdmin <- userInfoRepo.isAdminCheck(data.username)
            isEnable <- userInfoRepo.isUserEnabled(data.username)
            validUser <- userInfoRepo.validateUser(data.username, data.password)
          } yield {
            if (isAdmin == true) {
              validUser match {
              //  case Some(user) => Redirect(routes.UserProfileController.showUser()).withSession("username" -> data.username, "isAdmin" -> true.toString).flashing("logged in" -> "logged in successfully")
                case Some(user) => Redirect(routes.AdminController.adminProfile()).withSession("username" -> data.username).flashing("logged in" -> "logged in successfully")
                case None => Redirect(routes.SigninController.loginForm()).flashing("incorrect user" -> "invalid credentials")
              }
            }
              else if (isAdmin == false && isEnable == true) {
                validUser match {
                //  case Some(user) => Redirect(routes.AdminController.adminProfile()).withSession("username" -> data.username, "isAdmin" -> true.toString).flashing("logged in" -> "logged in successfully")
                  case Some(user) => Redirect(routes.UserProfileController.userProfile()).withSession("username" -> data.username, "isAdmin" -> true.toString).flashing("logged in" -> "logged in successfully")

                  case None => Redirect(routes.SigninController.loginForm()).flashing("incorrect user" -> "invalid credentials")
                }
              }
            else {
              Redirect(routes.SigninController.loginForm()).flashing("disabled" -> "can't login, account disabled")
            }
          }
        }
      )
  }

  def forgotPasswordForm() = Action {
    implicit request =>
      Ok(views.html.forgotpassword(userForms.forgotPasswordForm))
  }

  def changePassword() = Action.async{
    implicit request =>
      userForms.forgotPasswordForm.bindFromRequest().fold(
        formWithError => {
          Future.successful(BadRequest(views.html.forgotpassword(formWithError)))
        },
        data => {
          userInfoRepo.checkUserExists(data.username) flatMap {
            case true => userInfoRepo.updatePassword(data.username, data.newPassword).map {
              case true =>
                Redirect(routes.SigninController.loginForm()).withSession("userName" -> data.username).flashing("password updated" -> "password changed successfully")
              case false => InternalServerError("Could not update password")
            }
            case false => Future.successful(Redirect(routes.SigninController.forgotPasswordForm()).flashing("not exists" -> "user does not exist, try again.."))
          }
        })
  }

}
