package com.spotimap.config

import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}
import com.spotimap.config.ApplicationConfig.SpotifyConfig.SpotifyClientConfig
import com.spotimap.config.ApplicationConfig.{ServerConfig, SpotifyConfig}
import pureconfig._

case class ApplicationConfig(server: ServerConfig, spotify: SpotifyConfig)

object ApplicationConfig {

  case class ServerConfig(port: Int)

  case class SpotifyConfig(client: SpotifyClientConfig, redirectUri: String)

  object SpotifyConfig {

    case class SpotifyClientConfig(clientId: String, clientSecret: String) {
      val authorizationHeader = Authorization(BasicHttpCredentials(clientId, clientSecret))
    }

  }

  val config: ApplicationConfig = loadConfigOrThrow[ApplicationConfig]
}
