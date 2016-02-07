package common

import akka.actor.{ActorLogging, Actor}

/**
  * Created by kasonchan on 12/21/15.
  */
class Worker extends Actor with ActorLogging {

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("Pre-restart " + reason.toString)
  }

  override def postStop(): Unit = {
    log.info("Post-stop")
  }

  override def postRestart(reason: Throwable): Unit = {
    log.info("Post-restart " + reason.toString)
  }

  override def preStart(): Unit = {
    log.info("Pre-start")
  }

  def receive: PartialFunction[Any, Unit] = {
    case "quit" =>
      log.info(sender().path.name + ": " + "quit")
      context.system.terminate()
    case x =>
      log.info(sender().path.name + ": " + x.toString)
  }

}
