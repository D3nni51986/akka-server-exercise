package producer

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Connection
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import models.Event
import support.{EventServiceApp, ServiceJsonSupport}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.sys.process._
import scala.util.{Failure, Success}


object EventsProducer extends EventServiceApp{
  def workerName: String = "EventsProducer"
  val worker:Props = Props(new EventsProducer())

  def produce() = {
    jsonGenerator.lineStream.foreach(i => workerActor ! i)
  }

  produce()

  class EventsProducer extends Actor with ServiceJsonSupport{

    implicit val materializer     = ActorMaterializer()
    implicit val executionContext = context.dispatcher

    override def receive: Receive = {
      case input:String => Unmarshal(input).to[Event].map(_ match {
        case (event: Event) => {
          //Valid json event passed unmarshaling
          fireHttpRequest(input)
        }
      })
      case _           => println(s"Received UNKNOWN data")
    }
  }

  private def fireHttpRequest(reqJson:String) = {
    val httpRequest = HttpRequest(
      HttpMethods.POST,
      uri = Uri(s"http://${server_host}:${server_port}/service/"),
      entity = HttpEntity.apply(ContentType(MediaTypes.`application/json`), reqJson),
      headers = List(Connection("keep-alive"))
    )

    val http = Http(system)
    http.singleRequest(httpRequest).onComplete{
      case Success(s) => println(s"Sent successfully ${s}")
      case Failure(e) => println(s"Error in sending ${e}")
    }
  }
}
