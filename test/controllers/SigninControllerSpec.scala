package controllers

import models.{UserForms, UserInfoRepo}
import org.mockito.Mockito.when
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.ControllerComponents
import play.api.test.FakeRequest
import play.api.test.CSRFTokenHelper._
import akka.util.Timeout
import scala.concurrent.duration._


import play.api.test.Helpers.{OK, status, stubControllerComponents}

class SigninControllerSpec extends PlaySpec with Mockito{
  implicit val timeout = Timeout(20 seconds)

  "Display signin form" in {
    val controller = getMockedObject
    when(controller.userForms.userSignInForm) thenReturn { val form = new UserForms {}
      form.userSignInForm}
    val result = controller.signinController.loginForm().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)

  }


  def getMockedObject: TestObjects = {
    val mockedUserInfoRepo = mock[UserInfoRepo]
    val mockedUserForms = mock[UserForms]



    val controller = new SigninController(mockedUserForms,stubControllerComponents(),mockedUserInfoRepo)

    TestObjects(stubControllerComponents(),controller,mockedUserForms,mockedUserInfoRepo)
  }

  case class TestObjects(controllerComponents: ControllerComponents, signinController: SigninController,
                         userForms: UserForms, userInfoRepo: UserInfoRepo)

}
