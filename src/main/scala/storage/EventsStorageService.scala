package storage

import java.util.concurrent.ConcurrentHashMap
import akka.actor.{Actor, Props}
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.Success
import models.Event
import consumer.EventsConsumerManager._

object EventsStorageService extends StorageService{

  val map:ConcurrentHashMap[String, ListBuffer[Event]] = new ConcurrentHashMap[String, ListBuffer[Event]]()

  val storageUpdater = system.actorOf(Props(new EventsStorageWorker(map)))

  override def postEvent(event: Event): Future[String] = {
    val promise = Promise[Event]()
    storageUpdater ! promise.success(event)
    promise.future.map(ev => s"Event ${ev.event_type} posted successfully")
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
    case (event:Promise[Event]) => sender ! event.future.onComplete{
      case Success(s) => {
        Option(storage.get(s.event_type)) match {
          case Some(l) => l.append(s)
          case None    => storage.put(s.event_type,ListBuffer(s))}}
    }
  }
}
