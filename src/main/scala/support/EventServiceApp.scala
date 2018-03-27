package support

import akka.actor.{ActorSystem, Props}

trait EventServiceApp extends App with EventAppConfig{
  def workerName:String
  def worker:Props
  val system = ActorSystem(workerName)
  val workerActor = system.actorOf(worker, workerName)
}
