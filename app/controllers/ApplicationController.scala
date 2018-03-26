package controllers

import forms.{UserData, UserForms}
import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc._

import scala.concurrent.Future

@Singleton
class ApplicationController @Inject()(userForms: UserForms,cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {
  implicit val message = cc.messagesApi

  def signup() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signup(userForms.userSignUpForm))
  }

  def register() =Action { implicit request: Request[AnyContent] =>
    userForms.userSignUpForm.bindFromRequest().fold(
      formWithErrors => {
        BadRequest(views.html.signup(formWithErrors))},
      data => {
        val createUser =UserData(
          data.firstName,
          data.middleName,
          data.lastName,
          data.username,
          data.password,
          data.confirmPassword,
          data.mobile,
          data.gender,
          data.age,
          data.hobbies
        )
        Ok(views.html.userprofile())
      })
  }


  def signin() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signin())
  }

  def forgotpassword() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.forgotpassword())
  }
}