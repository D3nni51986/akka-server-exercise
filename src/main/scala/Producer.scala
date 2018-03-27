import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.{Connection, RawHeader}
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import models.Event
import support.ServiceJsonSupport
import scala.concurrent.ExecutionContext.Implicits.global

import scala.sys.process._
import scala.util.{Failure, Success}


object EventsProducer extends App{

  val system = ActorSystem("Producer")

  def run() = {
    val producerWorker = system.actorOf(Props(new EventsProducer()))
    "generator-windows-amd64.exe".lineStream.foreach(i => producerWorker ! i)
  }

  run

  class EventsProducer extends Actor with ServiceJsonSupport{

    implicit val materializer     = ActorMaterializer()
    implicit val executionContext = context.dispatcher

    override def receive: Receive = {
      case input:String => {
        println(s"Worker Received ${input}")
        Unmarshal(input).to[Event].map(_ match {
          case (event:Event) => {
            println(s"Created event => ${event}")
            fireHttpRequest(input)
          }
        })
      }
      case _           => println(s"Received UNKNOWN data")
    }
  }

  private def fireHttpRequest(req:String) = {
    val httpRequest = HttpRequest(
      HttpMethods.POST,
      uri = Uri("http://localhost:8050/service/"),
      entity = HttpEntity.apply(ContentType(MediaTypes.`application/json`), req),
      headers = List(Connection("keep-alive")/*RawHeader("x-openrtb-version", "2.3")*/)
    )

    val http = Http(system)
    http.singleRequest(httpRequest).onComplete{
      case Success(s) => println(s"Send successfully ${s}")
      case Failure(e) => println(s"Error in sending ${e}")
    }
  }
}
