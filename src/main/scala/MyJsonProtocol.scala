import spray.json.DefaultJsonProtocol

trait MyJsonProtocol extends DefaultJsonProtocol {
  implicit val eventFormat = jsonFormat3(Event)
}


object MyJsonProtocol extends MyJsonProtocol

