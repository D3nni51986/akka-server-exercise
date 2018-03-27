package support

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.Event
import spray.json.DefaultJsonProtocol

trait ServiceJsonSupport extends DefaultJsonProtocol with SprayJsonSupport{
  implicit val eventFormat = jsonFormat3(Event)


}

