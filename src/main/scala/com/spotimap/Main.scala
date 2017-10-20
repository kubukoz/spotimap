package com.spotimap

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.logRequestResult
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import cats.free.Free
import cats.instances.future._
import com.spotimap.client._
import com.spotimap.util.Implicits.globalEC

import scala.concurrent.Future
import scala.io.StdIn

object Main extends MapRoutes {
  type Result[+T] = Future[T]
  type HttpCall[A] = Free[SpotifyOp, A]

  private val port = 2137

  implicit val system: ActorSystem = ActorSystem("spotimap")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val client: HttpClient[Result] = new HttpClientImpl
  override val interpreter: SpotifyInterpreter[Result] = new SpotifyInterpreter(client)

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


