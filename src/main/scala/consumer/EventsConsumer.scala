package consumer

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import route.EventServiceRoute
import support.{EventAppConfig, EventServiceApp}

object EventsConsumer extends EventServiceApp{
  def workerName = "EventsConsumer"
  def worker = Props(new EventsConsumer())
}

class EventsConsumer
  extends Actor
    with EventServiceRoute
    with EventAppConfig
    with ActorLogging{

  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = context.dispatcher

  override def receive = {
    case s:String  => log.info(s"Handling requests => ${s}")
  }

  Http(context.system).bindAndHandle(route, server_host, server_port)

}
