import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{Directive1, Rejection}
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer

trait ServiceRoute {

  val route = {
    pathPrefix("service" /){
      get{
        extractEventType{
          event => complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Get ${event}</h1>"))
        }
      }~post{
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Post</h1>"))
      }
    }
  }

  private def extractEventType:Directive1[String] = {
    parameter('countByEventType.?).flatMap{
      case Some(eventT) => provide(eventT)
      case _          => {
        extractUri.flatMap(uri => reject(new ServiceRejection(s"${uri}")))
      }
    }
  }

  class ServiceRejection(val error:String) extends Throwable with Rejection {
    override def toString = {
      s"Error ${error}"
    }
  }


  protected implicit def materializer: Materializer

}
