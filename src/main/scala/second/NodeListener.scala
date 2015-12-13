package second

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._

case object Exit

case object ExitAll

case class Msg(text: String)

/**
 * Created by kasonchan on 12/12/15.
 */
class NodeListener extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  // Subscribes to cluster changes
  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember])
  }

  // Resubscribes when restart
  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case MemberUp(member) =>
      log.info("Member is Up: {}", member.address)
    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}",
        member.address, previousStatus)
    case Exit =>
      val address = Cluster(context.system).selfAddress
      Cluster(context.system).leave(address)
    case ExitAll =>
      context.system.terminate()
    case Msg(text: String) =>
      log.info("received: {}", text)
    case _: MemberEvent =>
  }

}
