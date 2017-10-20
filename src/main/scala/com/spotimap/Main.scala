package com.spotimap

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.logRequestResult
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import cats.instances.future._
import com.spotimap.client.{SpotifyHttpInterpreter, _}
import com.spotimap.util.Implicits.globalEC

import scala.io.StdIn
import scala.language.higherKinds

object Main extends MapRoutes {
  private val port = 2137

  implicit val system: ActorSystem             = ActorSystem("spotimap")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val client: HttpClient[Result]               = new HttpClientImpl
  override val interpreter: SpotifyInterpreter[Result] = new SpotifyHttpInterpreter(client)

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

    server.flatMap(_.unbind()).onComplete { _ =>
      system.terminate()
    }
  }
}
