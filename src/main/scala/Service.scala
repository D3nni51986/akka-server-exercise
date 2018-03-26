import scala.concurrent.Future

trait Service {
  def postEvent(event:Event):Unit
  def getEventsCountById(eventType:String):Future[Option[Int]]
  def getDataCountByEventType(eventType:String):Future[Option[Int]]
}
