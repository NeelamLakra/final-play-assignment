package models

import javax.inject.Inject
import play.api.cache.AsyncCacheApi
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class UserInfo(id :Int,
                    firstname: String,
                    middlename:String,
                    lastname: String,
                    username: String,
                    password:String,
                    mobile:String,
                    gender:String,
                    age:Int,
                    hobbies:String,
                    isEnable:Boolean = true,
                    isAdmin:Boolean = false)


trait UserRepoData extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._
  class UserTable(tag:Tag) extends Table[UserInfo](tag,"userProfile"){

    def id:Rep[Int]=column[Int]("id",O.PrimaryKey,O.AutoInc)
    def firstname:Rep[String] =column[String]("firstname")
    def middlename:Rep[String]=column[String]("middlename")
    def lastname:Rep[String]=column[String]("lastname")
    def username:Rep[String]=column[String]("username",O.PrimaryKey)
    def password:Rep[String]=column[String]("password")
    def mobile:Rep[String]=column[String]("mobile")
    def gender:Rep[String]=column[String]("gender")
    def age:Rep[Int]=column[Int]("age")
    def hobbies:Rep[String] = column[String]("hobbies")
    def isEnable:Rep[Boolean]=column[Boolean]("isEnable")
    def isAdmin:Rep[Boolean]=column[Boolean]("isAdmin")

    def * : ProvenShape[UserInfo]=(id,firstname,middlename,lastname,username,password,mobile,gender,age,hobbies,isEnable,isAdmin) <> (UserInfo.tupled,UserInfo.unapply)
  }

  val UserQuery = TableQuery[UserTable]
}

trait UserProfileRepo {
  def store(userInfo: UserInfo): Future[Boolean]
  def checkUserExists(username:String):Future[Boolean]
  def updateProfile(userName: String,updatedUserData: UserProfile): Future[Boolean]
  def getUserDetails(userName: String): Future[UserProfile]
  def updatePassword(userName: String,newPassword: String): Future[Boolean]
  def isUserEnabled(userName: String): Future[Boolean]
  def enableDisableUser(userName: String,newValue: Boolean): Future[Boolean]
  def getAllUsers(): Future[List[UserInfo]]
  def validateUser(userName: String,password: String): Future[Option[UserInfo]]
  def isAdminCheck(userName: String): Future[Boolean]
  def getUser(): Future[List[UserInfo]]
  def enable(userName: String):Future[Boolean]
  def disable(userName: String):Future[Boolean]
  def isEnableCheck(userName:String): Future[Boolean]
  def validationOfUser(userName: String,password: String): Future[Boolean]


}

trait UserProfileRepoImple extends UserProfileRepo {
  self: UserRepoData =>

  import profile.api._

  def isAdminCheck(userName: String): Future[Boolean] = {
    db.run(UserQuery.filter(user=> user.username === userName && user.isAdmin).to[List].result).map(_.nonEmpty)
  }

  def getUser(): Future[List[UserInfo]] = {
    db.run(UserQuery.filter(_.isAdmin===true).to[List].result)
  }

  def enable(userName: String):Future[Boolean]=
    db.run(UserQuery.filter(_.username===userName).map(_.isEnable).update(true)).map(_>0)

  def disable(userName: String):Future[Boolean] =
    db.run(UserQuery.filter(_.username === userName).map(_.isEnable).update(false)).map(_>0)

  def isEnableCheck(userName:String): Future[Boolean] = {
    db.run(UserQuery.filter(user => user.username === userName && user.isEnable).to[List].result).map(_.nonEmpty)
  }

  def validationOfUser(userName: String,password: String): Future[Boolean] = {
    val queryResult = UserQuery.filter(user => user.username=== userName && user.password === password).result.headOption
    db.run(queryResult)
      .map{
        case Some(_) => true
        case None => false
      }
  }


  def getAllUsers(): Future[List[UserInfo]] =
    db.run(UserQuery.filter(user => user.isAdmin === false).to[List].result)

  def validateUser(userName: String,password: String): Future[Option[UserInfo]] =
    db.run(UserQuery.filter(user => user.username === userName && user.password === password ).result.headOption)


  def store(userInfo: UserInfo): Future[Boolean] =
    db.run(UserQuery += userInfo) map (_ > 0)

  def checkUserExists(userName: String): Future[Boolean] =
    db.run(UserQuery.filter(user => user.username === userName).to[List].result.map(user => user.nonEmpty))

  def getUserDetails(userName: String): Future[UserProfile] =
    db.run(UserQuery.filter(user => user.username === userName).result.head.map(
      user => UserProfile(user.firstname, user.middlename, user.lastname, user.username, user.mobile, user.gender, user.age, user.hobbies)))

  def updateProfile(userName: String, updatedUserData: UserProfile): Future[Boolean] =
    db.run(UserQuery.filter(user => user.username === userName).map(
      user => (user.firstname, user.middlename, user.lastname, user.username, user.mobile, user.gender, user.age, user.hobbies))
      .update(
        updatedUserData.firstname, updatedUserData.middlename, updatedUserData.lastname, updatedUserData.username, updatedUserData.mobile
        , updatedUserData.gender, updatedUserData.age, updatedUserData.hobbies))
      .map(_ > 0)

  def updatePassword(userName: String, newPassword: String): Future[Boolean] =
    db.run(UserQuery.filter(user => user.username === userName)
      .map(user => user.password).update(newPassword)).map(_ > 0)

  def isUserEnabled(userName: String): Future[Boolean] =
    db.run(UserQuery.filter(user => user.username === userName).map(user => user.isEnable).result.head)

  def enableDisableUser(userName: String, newValue: Boolean): Future[Boolean] =
    db.run(UserQuery.filter(user => user.username === userName).map(user => user.isEnable).update(newValue)).map(_ > 0)

}

class UserInfoRepo @Inject()(cache:AsyncCacheApi)(protected val dbConfigProvider: DatabaseConfigProvider) extends UserRepoData with UserProfileRepoImple {

}





