package consumer

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.pattern.pipe
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import route.EventServiceRoute

object EventsConsumerManager extends App{

  def managerActor:Props = Props(new EventsConsumerManager())

  val system = ActorSystem("EventsConsumerManager", ConfigFactory.load())
  system.actorOf(managerActor, "EventsConsumerManager")
}

class EventsConsumerManager extends Actor with EventServiceRoute{

  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = context.dispatcher

  override def receive = {
    case s:String  => println(s"Handling requests => ${s}")
  }

  Http(context.system).bindAndHandle(route, "localhost", 8050).pipeTo(context.self)

}
