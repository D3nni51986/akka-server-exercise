import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import akka.pattern.pipe

object Manager extends App{

  def managerActor:Props = Props(new Manager())

  val system = ActorSystem("Manager", ConfigFactory.load())
  system.actorOf(managerActor, "Manager")
}

class Manager
  extends Actor
    with EventServiceRoute{

  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = context.dispatcher

  val service = context.actorOf(Props(new EventsStorageService()))

  override def receive = {
    case msg:String     => println(s"$msg")
  }

  Http(context.system).bindAndHandle(route, "localhost", 8050).pipeTo(context.self)

}
