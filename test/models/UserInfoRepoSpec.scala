package models

import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class UserInfoRepoSpec extends Specification{


  val repo = new ModelTest[UserInfoRepo]


  "User Information Repository" should {

    "store associate details of a user" in {
      val userInfo = UserInfo(1,"saurav","","agarwal","saurav@07","sauneel2109","1234567890","male",22,"dancing",true,false)
      val storeResult = Await.result(repo.repository.store(userInfo), Duration.Inf)
      storeResult must equalTo(true)
    }

    "check the existance of user in the databse" in {
      val username = "neel@21"
      val storeResult = Await.result(repo.repository.checkUserExists(username), Duration.Inf)
      storeResult must equalTo(false)
    }

    "updating the user profile" in {
      val updateProfile = UserProfile("arjun","","agarwal","saurav@07","1234567890","male",22,"dancing")
      val username = "saurav@07"
      val storeResult = Await.result(repo.repository.updateProfile(username,updateProfile),Duration.Inf)
      storeResult must equalTo(true)
    }
    "get the user details" in {
      val userDetails = UserProfile("arjun","","agarwal","saurav@07","1234567890","male",22,"dancing")
      val username = "saurav@07"
      val storeResult = Await.result(repo.repository.getUserDetails(username),Duration.Inf)
      storeResult must equalTo(userDetails)
    }

    "updating the user password" in {
      val newPassword = "sauneel2109"
      val username = "saurav@07"
      val storeResult = Await.result(repo.repository.updatePassword(username,newPassword),Duration.Inf)
      storeResult must equalTo(true)
    }

    "check whether user is enabled or not" in {
      val username = "saurav@07"
      val storeResult = Await.result(repo.repository.isUserEnabled(username),Duration.Inf)
      storeResult must equalTo(true)
    }

    "enabling or disabling user by admin" in {
      val username = "saurav@07"
      val status=false
      val storeResult = Await.result(repo.repository.enableDisableUser(username,status),Duration.Inf)
      storeResult must equalTo(true)
    }

    "fetching all the users by admin" in {
      val userInfo = List(UserInfo(1,"arjun","","agarwal","saurav@07","sauneel2109","1234567890","male",22,"dancing",false,false))
      val storeResult = Await.result(repo.repository.getAllUsers(),Duration.Inf)
      storeResult must equalTo(userInfo)
    }

    "authenticating a user" in {
      val userInfo = Seq(UserInfo(1,"neel","","agarwal","neelam@21","sauneel21","1234567890","female",22,"dancing",true,false))
      val username="neelam@21"
      val password="sauneel21"
      val storeResult = Await.result(repo.repository.validateUser(username,password),Duration.Inf)
      storeResult must equalTo(None)
    }

    "authenticating admin" in {
      val username="`neel@21"
      val storeResult = Await.result(repo.repository.isAdminCheck(username),Duration.Inf)
      storeResult must equalTo(false)
    }

    "fetching the user record" in {
      val userInfo = List(UserInfo(1,"saurav","","agarwal","saurav@07","sauneel2109","1234567890","male",22,"dancing",true,false))
      val storeResult = Await.result(repo.repository.getUser(),Duration.Inf)
      storeResult must equalTo(List())
    }


    "for enabling user by admin" in {
      val username="`nancy@jain.com"
      val storeResult = Await.result(repo.repository.enable(username),Duration.Inf)
      storeResult must equalTo(false)
    }

    "for disabling user by admin" in {
      val username="`nancy@jain.com"
      val storeResult = Await.result(repo.repository.disable(username),Duration.Inf)
      storeResult must equalTo(false)
    }

    "for checking whether user is enabled or not" in {
      val username="`nancy@jain.com"
      val storeResult = Await.result(repo.repository.isEnableCheck(username),Duration.Inf)
      storeResult must equalTo(false)
    }


    "validating a user" in {
      val username="neelam@21"
      val password="sauneel21"
      val storeResult = Await.result(repo.repository.validationOfUser(username,password),Duration.Inf)
      storeResult must equalTo(false)
    }

  }
}
