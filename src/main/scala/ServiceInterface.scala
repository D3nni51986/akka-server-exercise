import akka.http.scaladsl.server.Route

trait ServiceInterface extends RouteHelper{
  val service:Service
  val route:Route
}
