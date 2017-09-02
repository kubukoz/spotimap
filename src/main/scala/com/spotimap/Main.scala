package com.spotimap

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Main extends UserRoutes {
  implicit val system: ActorSystem = ActorSystem("spotimap")
  private val port = 8080

  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val routes: Route = userRoutes

  def main(args: Array[String]): Unit = {
    val serverBindingFuture = Http().bindAndHandle(routes, "localhost", port)

    println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")

    StdIn.readLine()

    serverBindingFuture
      .flatMap(_.unbind())
      .onComplete { _ =>
        system.terminate()
      }
  }
}

