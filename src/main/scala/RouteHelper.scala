import akka.http.scaladsl.marshalling.{ToResponseMarshallable, ToResponseMarshaller}
import akka.http.scaladsl.server.{Directives, Route}

import scala.concurrent.{ExecutionContext, Future}

trait RouteHelper extends Directives{

  implicit val executionContext:ExecutionContext

  def complete[T: ToResponseMarshaller](resource: Future[Option[T]]): Route =
    onSuccess(resource) {
      case Some(t) => complete(ToResponseMarshallable(t))
      case None => complete(None)
    }

  //def complete(resource: Future[Unit]): Route = onSuccess(resource) { complete(Future(Some(204))) }
}
