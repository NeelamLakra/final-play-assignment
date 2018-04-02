package models

import play.api.data.Forms._
import play.api.data._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

import scala.util.matching.Regex

case class UserData(firstname: String, middlename:String, lastname: String,username: String, password: String, confirmPassword: String,mobile:String ,gender: String,age: Int,
                    hobbies:String)

case class SignInData(username:String, password:String)

case class UserProfile(firstname: String,
                       middlename: String,
                       lastname: String,
                       username :String,
                       mobile: String,
                       gender: String,
                       age: Int,
                       hobbies: String)

case class ForgotPassword(username: String,
                          newPassword: String,
                          confirmPassword: String)


case class AssignmentData(title: String,
                          description: String)

class UserForms {

  val userSignUpForm = Form {
    mapping(
      "firstname" -> nonEmptyText.verifying(allCharacterChecker()),
      "middlename" ->text,
      "lastname" ->nonEmptyText.verifying(allCharacterChecker()),
      "username" -> email,
      "password" -> nonEmptyText.verifying(passwordChecker()),
      "confirmPassword" -> nonEmptyText,
      "mobile" -> nonEmptyText.verifying(mobileChecker()),
      "gender" -> nonEmptyText,
      "age" -> number(min = 18, max = 75),
      "hobbies" -> nonEmptyText.verifying(hobbiesChecker())
    )(UserData.apply)(UserData.unapply).verifying("Password and confirm password donot match",
      verify => verify match{ case user => passwordMatch(user.password, user.confirmPassword)})
  }

  val userSignInForm =Form{
    mapping(
      "username" -> email,
      "password" -> nonEmptyText)(SignInData.apply)(SignInData.unapply)
  }

  val userProfileForm = Form{
    mapping(
    "firstname" -> nonEmptyText.verifying(allCharacterChecker()),
    "middlename" -> text,
    "lastname" -> nonEmptyText.verifying(allCharacterChecker()),
    "username" -> email,
    "mobile" -> text.verifying(mobileChecker()),
    "gender" -> nonEmptyText,
    "age" -> number(min = 18, max = 75),
    "hobbies" -> nonEmptyText
  )(UserProfile.apply)(UserProfile.unapply)
  }


  val forgotPasswordForm = Form(mapping(
    "username" -> email,
    "newPassword" -> nonEmptyText.verifying(passwordChecker()),
    "confirmPassword" -> nonEmptyText.verifying(passwordChecker()),
  )(ForgotPassword.apply)(ForgotPassword.unapply)
    verifying("Password fields do not match ", user => user.newPassword == user.confirmPassword)
  )

  val assignmentForm = Form(mapping(
    "title" -> nonEmptyText,
    "description" -> nonEmptyText.verifying("Length should not be more than 100 characters",
      description => if(description.length <=100) true else false)
  )(AssignmentData.apply)(AssignmentData.unapply))


  def passwordMatch(password: String, confirmPassword: String):Boolean = password == confirmPassword
  val allNumbers: Regex = """\d*""".r
  val allLetters = """[A-Za-z]*""".r

  def mobileChecker(): Constraint[String] = {
    Constraint("mobile")({
      mobile =>
        if (mobile.length == 10 && mobile.matches("""[0-9]+"""))
          Valid
        else
          Invalid(ValidationError("mobile number must be 10 digits"))
    })
  }
  def allCharacterChecker(): Constraint[String] = {
    Constraint("nameChecker")(
      name => if (name matches """[A-Za-z]+""")
        Valid
      else
        Invalid(ValidationError("name can only contain letters"))
    )
  }

  def hobbiesChecker(): Constraint[String] = {
    Constraint("nameChecker")(
      hobby => if (hobby matches """[A-Za-z,]+""")
        Valid
      else
        Invalid(ValidationError("name can only contain letters"))
    )
  }
  def passwordChecker(): Constraint[String] = {
    Constraint[String]("constraint.password") {
      Text =>
        val errors = Text match {
          case plaintext if plaintext.length < 8 => Seq(ValidationError("Password should be more than 8 characters"))
          case allNumbers() => Seq(ValidationError("Password is all numbers it should be alphanumeric"))
          case allLetters() => Seq(ValidationError("Password is all letters it should be alphanumeric"))
          case _ => Nil
        }
        if (errors.isEmpty) {
          Valid
        } else {
          Invalid(errors)
        }
    }
  }

}



