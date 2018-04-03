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
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito

class UserProfileControllerSpec  extends PlaySpec with Mockito{


  "go to user profile navbar" in {
    val controller = getMockedObject
    val result = controller.userProfileController.userProfile().apply(FakeRequest())
    status(result) must equal(OK)
  }


  def getMockedObject: TestObjects = {
    val mockedUserInfoRepo = mock[UserInfoRepo]
    val mockedUserForms = mock[UserForms]
    val mockedAssignmentInfoRepo = mock[AssignmentInfoRepo]



    val controller = new UserProfileController(mockedUserForms,mockedUserInfoRepo,stubControllerComponents(),mockedAssignmentInfoRepo)

    TestObjects(stubControllerComponents(),controller,mockedUserForms,mockedUserInfoRepo,mockedAssignmentInfoRepo)
  }

  case class TestObjects(controllerComponents: ControllerComponents, userProfileController: UserProfileController,
                         userForms: UserForms, userInfoRepo: UserInfoRepo,assignmentInfoRepo: AssignmentInfoRepo
                         )
}
