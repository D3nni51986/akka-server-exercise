import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.Success

class EventsStorageService(implicit executionContext: ExecutionContext) extends Service{

  private val eventsList:ListBuffer[Event] = ListBuffer.empty[Event]

  override def postEvent(event: Event): Future[Unit] = ???

  override def getEventsCountById(eventType: String):Future[Option[Int]] = ???

  override def getDataCountByEventType(eventType: String):Future[Option[Int]] = ???

  def receive: Receive = {
    case (event:Promise[Event])                  => sender ! event.future.onComplete{ case Success(s) => eventsList.append(s); s.event_type}
    case (getEventCount:GetEventCount)           => retrieveEventCount(getEventCount.event_type)
    case (getEventsDataCount:GetEventsDataCount) => retrieveEventsDataCount(getEventsDataCount.event_type)
  }

  private def retrieveEventCount(event_type:String) = {
    eventsList.groupBy(e => e.event_type).get(event_type).map(_.length)
  }

  private def retrieveEventsDataCount(event_type:String) = {
    eventsList.groupBy(e => e.event_type).get(event_type).map(_.collect{
      case event => event.data
    }.distinct.length)
  }
}
