package route

import akka.http.scaladsl.server.{Directive1, Rejection}
import models.Event
import storage.EventsStorageService
import support.ServiceJsonSupport

import scala.concurrent.Future

trait EventServiceRoute extends RouteInterface with ServiceJsonSupport{

  val route = {
    pathPrefix("service" /){
      post{
        entity(as[Event]) {
          event => complete(postEvent(event))
        }
      }~ get{
        extractCountByEventType{
          eventType => complete(countByEventType(eventType))
        }
      }~ get{
        extractCountDataByEventType{
          e => complete(countDataByEventType(e))
        }
      }
    }
  }

  private def countByEventType(eventType:String):Future[Option[Int]] = {
    EventsStorageService.getEventsCountByEvenType(eventType)
  }

  private def countDataByEventType(eventType:String):Future[Option[Int]] = {
    EventsStorageService.getDataCountByEventType(eventType)
  }

  private def postEvent(event:Event):Future[String] = {
    EventsStorageService.postEvent(event)
  }

  private def extractCountByEventType:Directive1[String] = {
    parameter('numOf.?).flatMap{
      case Some(eventType) => provide(eventType)
      case _          => {
        extractUri.flatMap(uri => reject(new ServiceRejection(s"${uri}")))
      }
    }
  }

  private def extractCountDataByEventType:Directive1[String] = {
    parameter('dataNumberOf.?).flatMap{
      case Some(eventType) => provide(eventType)
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
