package controllers

import models._
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.ControllerComponents
import play.api.test.FakeRequest
import play.api.test.Helpers.stubControllerComponents
import play.api.test.Helpers._
import org.mockito.Mockito.when
import play.api.test.CSRFTokenHelper._

import scala.concurrent.Future



class AdminControllerSpec  extends PlaySpec with Mockito{


  "go to admin profile navbar" in {
    val controller = getMockedObject
    val result = controller.adminController.adminProfile().apply(FakeRequest())
    status(result) must equal(OK)
  }

  "displaying assignment form" in {
    val controller = getMockedObject
    when(controller.userForms.assignmentForm) thenReturn { val form = new UserForms{}
      form.assignmentForm}
    val result = controller.adminController.showAssignmentForm().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }
  "display all the users to admin" in {
    val controller = getMockedObject
    val user= List(UserInfo(1,"arjun","","agarwal","saurav@07","sauneel2109","1234567890","male",22,"dancing",true,false))
    when(controller.userInfoRepo.getAllUsers()) thenReturn Future.successful(user)
    val result = controller.adminController.displayUsers().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }

//
//  "enable or disable user by admin" in {
//    val controller = getMockedObject
//    val username = "saurav@07"
//    val status = false
//    when(controller.userInfoRepo.enableDisableUser(username,status)) thenReturn Future.successful(assignment)
//    val result = controller.adminController.viewAssignments().apply(FakeRequest().withCSRFToken)
//    status(result) must equal(OK)
//  }



  "viewing the assignments" in {
    val controller = getMockedObject
    val assignment = List(Assignment(1,"hello","write a paragraph"))
    when(controller.assignmentInfoRepo.getAssignment()) thenReturn Future.successful(assignment)
    val result = controller.adminController.viewAssignments().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }






  def getMockedObject: TestObjects = {
    val mockedAssignmentInfoRepo = mock[AssignmentInfoRepo]
    val mockedUserInfoRepo = mock[UserInfoRepo]
    val mockedUserForms = mock[UserForms]



    val controller = new AdminController(stubControllerComponents(),mockedAssignmentInfoRepo,mockedUserInfoRepo,mockedUserForms)

    TestObjects(stubControllerComponents(),controller,mockedAssignmentInfoRepo,mockedUserInfoRepo,mockedUserForms)
  }

  case class TestObjects(controllerComponents: ControllerComponents, adminController: AdminController,
                         assignmentInfoRepo: AssignmentInfoRepo,
                         userInfoRepo: UserInfoRepo,
                         userForms: UserForms)

}
