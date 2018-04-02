package controllers

import models.{UserForms, UserInfo, UserInfoRepo}
import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global


import scala.concurrent.Future

@Singleton
class RegistrationController @Inject()(userForms: UserForms, cc: ControllerComponents, userInfoRepo: UserInfoRepo) extends AbstractController(cc) with I18nSupport {
  implicit val message = cc.messagesApi

  def signup() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signup(userForms.userSignUpForm))
  }



  def register() = Action.async { implicit request: Request[AnyContent] =>
    userForms.userSignUpForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.signup(formWithErrors)))
      },
      input => {
        val user = UserInfo(
          0,
          input.firstname,
          input.middlename, input.lastName, input.username,
          input.password, input.mobile, input.gender, input.age, input.hobbies, "valid", "yes"
        )
        userInfoRepo.checkUserExists(input.username) flatMap {
          case true => Future.successful(Redirect(routes.SigninController.loginForm()).flashing("user exists" -> "user already exists, log in"))
          case false => userInfoRepo.store(user).map {
            case true =>
              Redirect(routes.UserProfileController.showUser()).withSession("username" -> input.username, "isAdmin" -> "yes").flashing("success" -> "user created successfully")
            case false => InternalServerError("Could not create user")
            //        userInfoRepo.store(user).map{
            //          case true  =>      Redirect(routes.RegistrationController.userprofile())
            //          case false =>       Redirect(routes.RegistrationController.signup())
            //        }
          }
        }
      })
  }

  }