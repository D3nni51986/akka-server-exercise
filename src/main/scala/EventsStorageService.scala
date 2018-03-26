import scala.concurrent.Future

class EventsStorageService extends Service{
  override def postEvent(event: Event): Unit = ???

  override def getEventsCountById(eventType: String):Future[Option[Int]] = ???

  override def getDataCountByEventType(eventType: String):Future[Option[Int]] = ???
}
