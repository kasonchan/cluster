package second

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.annotation.tailrec

/**
  * Created by kasonchan on 12/12/15.
  */
object SecondCluster {

  def main(args: Array[String]): Unit = {

    val port1 = "2551"

    val config1 = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port1).
      withFallback(ConfigFactory.load())

    // Creates an Akka system for the first node
    val system = ActorSystem("ClusterSystem", config1)

    // Creates an actor that handles cluster domain events
    val node1 = system.actorOf(Props[NodeListener], name = "clusterListener")

    // Send clusterListen messages
    node1 ! Msg("Hi")

    val port2 = "2552"

    val config2 = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port2").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]")).
      withFallback(ConfigFactory.load())

    // Creates an Akka system for the second node
    val system2 = ActorSystem("ClusterSystem", config2)

    // Creates an actor that handles cluster domain events
    val node2: ActorRef = system2.actorOf(Props[NodeListener], name = "clusterListener")

    // Send clusterListen messages
    node2 ! Msg("Hi")

    val nodes = List(node1, node2)

    read(nodes) match {
      case "Exit" => println("Closing")
      case _ => read(nodes)
    }

    println("Closed")
  }

  @tailrec
  def read(nodes: List[ActorRef]): Any = {
    scala.io.StdIn.readLine() match {
      case "Exit node 1" =>
        nodes(0) ! Exit
      case "Exit node 2" =>
        nodes(1) ! Exit
      case "Exit all" =>
        for (n <- nodes)
          n ! ExitAll
        "Exit"
      case s: String =>
        nodes(1) ! Msg(s)
        nodes foreach (x => x ! Msg(s))
        read(nodes)
      case _ =>
    }
  }

}
