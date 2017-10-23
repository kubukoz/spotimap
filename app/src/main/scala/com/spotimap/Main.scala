package com.spotimap

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import cats.instances.future._
import com.kubukoz.spotify.client.impl.{HttpClient, HttpClientImpl, SpotifyHttpInterpreter, SpotifyInterpreter}
import com.kubukoz.spotify.client.model.auth.Scope
import com.kubukoz.spotify.client.model.config.{SpotifyConfig, SpotifyConstants}
import com.spotimap.config.ApplicationConfig
import com.spotimap.routes.{AuthRoutes, TrackRoutes}
import com.spotimap.util.Implicits.globalEC

import scala.io.StdIn
import scala.language.higherKinds

object Main extends TrackRoutes with AuthRoutes {
  implicit protected val config: ApplicationConfig  = ApplicationConfig.config
  implicit private val spotifyConfig: SpotifyConfig = config.spotify

  implicit private val system: ActorSystem             = ActorSystem("spotimap")
  implicit private val materializer: ActorMaterializer = ActorMaterializer()

  private val client: HttpClient[Result]                         = new HttpClientImpl()
  override protected val interpreter: SpotifyInterpreter[Result] = new SpotifyHttpInterpreter(client)

  private val routes: Route = (logRequest("Request") & logResult("Result")) {
    trackRoutes ~ authRoutes
  }

  def main(args: Array[String]): Unit = {
    runServer()
  }

  private def runServer(): Unit = {
    val port   = config.server.port
    val server = Http().bindAndHandle(routes, "localhost", port)

    val loginUrl = SpotifyConstants.loginUrl(config.redirectUri, Scope.values.toList)

    println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
    println(s"Open $loginUrl to get a token")

    StdIn.readLine()

    server.flatMap(_.unbind()).onComplete { _ =>
      system.terminate()
    }
  }
}
