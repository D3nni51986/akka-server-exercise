import akka.http.scaladsl.server.Route

trait ServiceInterface {
  val service:Service
  val route:Route
}
