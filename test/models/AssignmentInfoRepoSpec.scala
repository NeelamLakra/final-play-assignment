//package models
//
//import org.specs2.mutable.Specification
//
//import scala.concurrent.Await
//import scala.concurrent.duration.Duration
//class AssignmentInfoRepoSpec extends Specification {
//
//
//    val repo = new ModelTest[AssignmentInfoRepo]
//
//
//    "Assignment Repository" should {
//
//      "Adding new assignment in the assignment list" in {
//        val assignment = Assignment(1,"hello","write a hello world progrsm ")
//        val storeResult = Await.result(repo.repository.addAssignment(assignment), Duration.Inf)
//        storeResult must equalTo(true)
//      }
//
//
//      "Delete existing assignment from the list" in {
//        val assignment = Assignment(1,"hello","write a hello world program")
//        val storeResult = Await.result(repo.repository.deleteAssignment(1), Duration.Inf)
//        storeResult must equalTo(true)
//      }
//
//
//      "fetching list of assignment" in {
//        val assignment = List(Assignment(1,"hello","write a hello world program"))
//        val storeResult = Await.result(repo.repository.getAssignment(),Duration.Inf)
//        storeResult must equalTo(assignment)
//      }
//    }
//
//}
