package com.spotimap

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.logRequestResult
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import cats.instances.future._
import com.spotimap.client.{SpotifyApi, SpotifyClientImpl}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn

object Main extends MapRoutes {
  private val port = 8080

  implicit val system: ActorSystem = ActorSystem("spotimap")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  private implicit val client = new SpotifyClientImpl()
  protected val api: SpotifyApi[Future] = new SpotifyApi[Future]()

  val routes: Route = logRequestResult("request/result") {
    mapRoutes
  }

  def main(args: Array[String]): Unit = {
    runServer()
  }

  def runServer(): Unit = {
    val server = Http().bindAndHandle(routes, "localhost", port)

    println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")

    StdIn.readLine()

    server
      .flatMap(_.unbind())
      .onComplete { _ =>
        system.terminate()
      }
  }
}


