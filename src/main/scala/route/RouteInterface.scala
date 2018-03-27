package route

import akka.http.scaladsl.server.Route

trait RouteInterface extends RouteHelper{
  val route:Route
}
