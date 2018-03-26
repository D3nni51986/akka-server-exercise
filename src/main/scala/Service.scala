import akka.actor.Actor

import scala.concurrent.Future

trait Service extends Actor{
  def postEvent(event:Event):Future[Unit]
  def getEventsCountById(eventType:String):Future[Option[Int]]
  def getDataCountByEventType(eventType:String):Future[Option[Int]]
}
