package controllers

import javax.inject.Singleton
import play.api.http.HttpErrorHandler
import play.api.mvc.RequestHeader
import play.api.mvc.Results.{InternalServerError, Status}

import scala.concurrent.Future

@Singleton
class ErrorHandler  extends HttpErrorHandler{
  def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful(
      Status(statusCode)("A client error occurred: " + message)
    )
  }

  def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful(
      InternalServerError("A server error occurred: " + exception.getMessage)
    )
  }

}
