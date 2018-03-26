import akka.actor.ActorRef
import akka.http.scaladsl.server.Route

trait ServiceInterface extends RouteHelper{
  val service:ActorRef
  val route:Route
}
