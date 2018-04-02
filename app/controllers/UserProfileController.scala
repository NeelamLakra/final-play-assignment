package controllers

import javax.inject.{Inject, Singleton}
import models.{UserForms, UserInfoRepo, UserProfile}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future


@Singleton
class UserProfileController @Inject()(userForms: UserForms, userInfoRepo: UserInfoRepo,cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {
  implicit val message = cc.messagesApi


  def showUser() = Action.async{
    implicit request =>
      val username = request.session.get("username")
      username match {
        case Some(username) => {
          val userData = userInfoRepo.getUserDetails(username)
          userData.map{
            userdata =>
              val userProfile = UserProfile(userdata.firstname, userdata.middlename,
                userdata.lastname,userdata.username, userdata.mobile, userdata.gender, userdata.age, userdata.hobbies)
              val filledProfileForm = userForms.userProfileForm.fill(userProfile)
              Ok(views.html.userprofile(filledProfileForm))
          }
        }
        case None => Future.successful(InternalServerError("session expired, user not found"))
      }
  }



  def updateUser() = Action.async{
    implicit request =>
      userForms.userProfileForm.bindFromRequest().fold(
        formWithError => {
          Future.successful(BadRequest(views.html.userprofile(formWithError)))
        },
        data => {
          val updatedUser = UserProfile(data.firstname, data.middlename, data.lastname,data.username, data.mobile, data.gender, data.age, data.hobbies)
          val username = request.session.get("username")
          username match {
            case Some(username) => {
              userInfoRepo.updateProfile(username, updatedUser).map {
                case true =>
                  Redirect(routes.UserProfileController.showUser()).flashing("profile updated" -> "profile updated successfully")
                case false => InternalServerError("Could not update user")
              }
            }
            case None => Future.successful(InternalServerError("session expired, user not found"))
          }

        }
      )
  }

  def logout() = Action {
    implicit request =>
      Redirect(routes.SigninController.loginForm()).withNewSession.flashing("logged out" -> "You have been successfully logged out...")
  }
}
