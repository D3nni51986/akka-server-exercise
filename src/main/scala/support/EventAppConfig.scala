package support

trait EventAppConfig extends ConfigSupport{

  val server_host = getParamValue("server.host").getOrElse("localhost")
  val server_port = getParamValue("server.port").map(_.toInt).getOrElse(8085)

  val jsonGenerator = getParamValue("json.generatorPath").getOrElse("generator-windows-amd64.exe")
}
