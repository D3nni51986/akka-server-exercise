package consumer.storage

import java.util.concurrent.ConcurrentHashMap
import akka.actor.{Actor, Props}
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import models.Event
import consumer.EventsConsumer._

object EventsStorageService extends StorageService{

  val map:ConcurrentHashMap[String, ListBuffer[Event]] = new ConcurrentHashMap[String, ListBuffer[Event]]()

  val storageUpdater = system.actorOf(Props(new EventsStorageWorker(map)))

  override def postEvent(event: Event): Future[String] = Future{
    storageUpdater ! event
    s"Event ${event.event_type} was posted"
  }

  override def getEventsCountByEvenType(eventType: String):Future[Option[Int]] = Future{
    Option(map.get(eventType)).map(_.length)
  }

  override def getDataCountByEventType(eventType: String):Future[Option[Int]] = Future{
    Option(map.get(eventType)).map(_.collect{
      case event:Event => event.data
    }.distinct.length)
  }
}

class EventsStorageWorker(val storage:ConcurrentHashMap[String, ListBuffer[Event]]) extends Actor{
  def receive: Receive = {
    case (event:Event) => Option(storage.get(event.event_type)) match {
      case Some(l) => l.append(event)
      case None    => storage.put(event.event_type,ListBuffer(event))}
  }
}
