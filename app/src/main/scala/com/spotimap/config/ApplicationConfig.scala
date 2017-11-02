package com.spotimap.config

import com.kubukoz.spotify.client.model.config.SpotifyConfig
import com.spotimap.config.ApplicationConfig.ServerConfig
import pureconfig._

case class ApplicationConfig(server: ServerConfig, spotify: SpotifyConfig) {
  val redirectUri: String = s"${server.host}:${server.port}/auth/code"
}

object ApplicationConfig {

  case class ServerConfig(port: Int, host: String)

  val config: ApplicationConfig = loadConfigOrThrow[ApplicationConfig]
}
