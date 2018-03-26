package forms

import play.api.data.Forms._
import play.api.data._

case class UserData(firstName: String, middleName: Option[String], lastName: String,username: String, password: String, confirmPassword: String,mobile:String ,gender: String,age: Int,
                    hobbies:String)

class UserForms {

  val userSignUpForm = Form {
    mapping(
      "firstname" -> text.verifying("please enter your first name", firstname =>firstname.nonEmpty && firstname.matches("[a-zA-Z]")),
      "middlename" -> optional(text),
      "lastname" -> text.verifying("please enter your last name", lastname => lastname.nonEmpty && lastname.matches("[a-zA-Z]")),
      "username" -> email,
      "password" -> text.verifying(password =>password.length>=8 && password.matches("[0-9]")),
      "confirmPassword" -> text.verifying(confirmPassword => confirmPassword.length>=8 && confirmPassword.matches("[0-9]")),
      "mobile" -> text.verifying("enter valid contact number",mobile => mobile.length==10 && mobile.matches("[0-9]")),
      "gender" -> nonEmptyText,
      "age" -> number(min = 18, max = 75),
      "hobbies" -> text.verifying(" please enter your hobbies", hobbies => hobbies.nonEmpty)
    )(UserData.apply)(UserData.unapply).verifying("Password and confirm password donot match", verify => verify.password == verify.confirmPassword)
  }
}
