package com.spotimap.routes
import com.spotimap.Result
import com.spotimap.client.api.SpotifyAlgebra
import com.spotimap.client.impl.SpotifyInterpreter
import com.spotimap.config.ApplicationConfig
import com.spotimap.config.ApplicationConfig.SpotifyConfig.SpotifyClientConfig
import com.spotimap.config.ApplicationConfig.{ServerConfig, SpotifyConfig}

class AuthRoutesSpec extends BaseRouteSpec with AuthRoutes {
  override implicit val interpreter: SpotifyInterpreter[Result] = new SpotifyInterpreter[Result] {
    override def apply[A](fa: SpotifyAlgebra[A]): Result[A] = ???
  }

  override implicit protected val config: ApplicationConfig =
    new ApplicationConfig(ServerConfig(8080),
                          SpotifyConfig(SpotifyClientConfig("client-id", "client-secret"), "http://redirect-uri"))
}
