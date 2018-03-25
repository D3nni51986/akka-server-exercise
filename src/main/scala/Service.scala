import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import akka.pattern.pipe

object Service extends App{

  def serviceActor:Props = Props(new Service())

  val system = ActorSystem("ServiceSystem", ConfigFactory.load())
  system.actorOf(serviceActor, "Service")
}

class Service
  extends Actor
    with ServiceRoute{

  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = context.dispatcher

  override def receive = {
    case msg:String     => println(s"$msg")
  }

  Http(context.system).bindAndHandle(route, "localhost", 8050).pipeTo(context.self)

}
