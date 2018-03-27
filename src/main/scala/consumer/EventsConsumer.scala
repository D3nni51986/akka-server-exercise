package consumer

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.pattern.pipe
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
    with EventAppConfig{

  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = context.dispatcher

  override def receive = {
    case s:String  => println(s"Handling requests => ${s}")
  }

  Http(context.system).bindAndHandle(route, server_host, server_port).pipeTo(context.self)

}
