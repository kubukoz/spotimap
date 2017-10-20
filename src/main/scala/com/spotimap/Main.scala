package com.spotimap

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.logRequestResult
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import cats.instances.future._
import com.spotimap.client.{HttpClientImpl, SpotifyApi, SpotifyClient}
import com.spotimap.util.Implicits.globalEC

import scala.concurrent.Future
import scala.io.StdIn

object Main extends MapRoutes {
  type Result[+T] = Future[T]
  private val port = 2137

  implicit val system: ActorSystem = ActorSystem("spotimap")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private implicit val client = new SpotifyClient(new HttpClientImpl)

  protected val api: SpotifyApi[Result] = new SpotifyApi[Result]()

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


