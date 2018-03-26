import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import akka.pattern.pipe

object Manager extends App{

  def serviceActor:Props = Props(new Manager())

  val system = ActorSystem("Manager", ConfigFactory.load())
  system.actorOf(serviceActor, "Manager")
}

class Manager
  extends Actor
    with EventServiceInterface{

  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = context.dispatcher

  val service = new EventsStorageService()

  override def receive = {
    case msg:String     => println(s"$msg")
  }

  Http(context.system).bindAndHandle(route, "localhost", 8050).pipeTo(context.self)

}
