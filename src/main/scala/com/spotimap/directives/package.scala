package com.spotimap

import akka.http.scaladsl.server.{Directive1, Directives}
import com.spotimap.model.external.SpotifyToken.UserToken

package object directives {

  val spotifyToken: Directive1[UserToken] =
    Directives.headerValueByName("SPOTIFY-TOKEN").map(UserToken)
}
