import akka.http.javadsl.server.RouteResult
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{Directive1, Rejection, RequestContext, Route}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer

import scala.concurrent.{ExecutionContext, Future, Promise}

trait EventServiceRoute extends ServiceInterface with SprayJsonSupport{

  implicit def materializer: Materializer
  implicit val executionContext:ExecutionContext

  import MyJsonProtocol._
  import spray.json._

  val route = {
    pathPrefix("service" /){
      get{
        extractEventType{
          eventType => complete(eventType)
        }
      }~post{
        entity(as[Event]) {
          event => complete(postEvent(event))
        }
      }
    }
  }

  private def postEvent(event:Event):Future[String] = {
    val promise = Promise[Event]()
    service ! promise.success(event)
    promise.future.map(_.event_type)
  }

  private def extractEventType:Directive1[String] = {
    parameter('countByEventType.?).flatMap{
      case Some(eventT) => provide(eventT)
      case _          => {
        extractUri.flatMap(uri => reject(new ServiceRejection(s"${uri}")))
      }
    }
  }

  class ServiceRejection(val error:String) extends Throwable with Rejection {
    override def toString = {
      s"Error ${error}"
    }
  }
}
