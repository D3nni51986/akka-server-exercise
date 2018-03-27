package consumer.storage

import models.Event

import scala.concurrent.Future

trait StorageService{
  def postEvent(event:Event):Future[String]
  def getEventsCountByEvenType(eventType:String):Future[Option[Int]]
  def getDataCountByEventType(eventType:String):Future[Option[Int]]
}
