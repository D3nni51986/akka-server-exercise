package support

import akka.actor.{ActorSystem, Props}

trait EventServiceApp extends App {
  def workerName:String
  def worker:Props
  val system = ActorSystem(workerName)
  val workerActor = system.actorOf(worker, workerName)
}
