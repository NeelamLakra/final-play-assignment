package controllers

import javax.inject.Inject
import models._
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AdminController @Inject()(cc: ControllerComponents,
                                assignmentInfoRepo: AssignmentInfoRepo,
                                userInfoRepo: UserInfoRepo,
                                userForms: UserForms)
  extends AbstractController(cc) with I18nSupport {


  def adminProfile() = Action {
    implicit request =>
      Ok(views.html.adminnavbar())
  }
  def showAssignmentForm() = Action {
    implicit request =>
      Ok(views.html.assignments(userForms.assignmentForm))
  }

  def showAdmin() = Action.async{
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
        case None => Future.successful(InternalServerError("password cannot be changed"))
      }
  }

  def addAssignment() = Action.async {
    implicit request =>
      userForms.assignmentForm.bindFromRequest().fold(
        formWithError => {
          Future.successful(BadRequest(views.html.assignments(formWithError)))
        },
        data => {
          val assignment = Assignment(0,data.title,data.description)
          assignmentInfoRepo.addAssignment(assignment).map{
            case true => Redirect(routes.AdminController.viewAssignments())
            case false => InternalServerError("couldn't add assignment")
          }
        }
      )
  }

  def displayUsers() = Action.async {
    implicit request =>
      userInfoRepo.getAllUsers.map {
        usersList => Ok(views.html.displayUsers(usersList))
      }
  }

  def enableOrDisableUser(username: String,updatedValue: Boolean) = Action.async {
    implicit request =>
      userInfoRepo.enableDisableUser(username,updatedValue).map{
        case true => Redirect(routes.AdminController.displayUsers())
        case false => InternalServerError("couldn't enable or disable user")
      }
  }

  def viewAssignments() = Action.async {
    implicit request =>
      assignmentInfoRepo.getAssignment.map {
        assignmentsList => Ok(views.html.displayassignments(assignmentsList))
      }
  }

  def deleteAssignment(id: Int) = Action.async {
    implicit request =>
      assignmentInfoRepo.deleteAssignment(id).map {
        case true => Redirect(routes.AdminController.viewAssignments())
        case false => InternalServerError("couldn't delete assignment from database")
      }
  }
}
