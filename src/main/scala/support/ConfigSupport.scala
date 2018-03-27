package support

import java.io.File

import com.typesafe.config.ConfigFactory

import scala.util.{Failure, Success, Try}

trait ConfigSupport {
  val conf = ConfigFactory.parseFile(new File("config\\application.conf"))

  def getParamValue(param:String) = Try(conf.getString(param)) match {
    case Success(value) => Some(value)
    case Failure(e)     => None
  }
}
