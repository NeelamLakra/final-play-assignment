package controllers

import models.{ UserForms, UserInfoRepo}
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.ControllerComponents
import play.api.test.FakeRequest
import play.api.test.Helpers.stubControllerComponents
import play.api.test.Helpers._
import org.mockito.Mockito.when
import play.api.test.CSRFTokenHelper._
import scala.concurrent.duration._
import scala.concurrent.Future
class RegistrationControllerSpec  extends PlaySpec with Mockito {


  "Display signup form" in {
    val controller = getMockedObject
    when(controller.userForms.userSignUpForm) thenReturn { val form = new UserForms {}
    form.userSignUpForm}
    val result = controller.registrationController.signup().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)

  }





  def getMockedObject: TestObjects = {
    val mockedUserInfoRepo = mock[UserInfoRepo]
    val mockedUserForms = mock[UserForms]



    val controller = new RegistrationController(mockedUserForms,stubControllerComponents(),mockedUserInfoRepo)

    TestObjects(stubControllerComponents(),controller,mockedUserForms,mockedUserInfoRepo)
  }

  case class TestObjects(controllerComponents: ControllerComponents, registrationController: RegistrationController,
                         userForms: UserForms, userInfoRepo: UserInfoRepo)


}
