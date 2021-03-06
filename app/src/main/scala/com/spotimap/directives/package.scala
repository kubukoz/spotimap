package com.spotimap

import akka.http.scaladsl.server.{Directive1, Directives}
import com.kubukoz.spotify.client.model.auth.SpotifyToken.UserToken

package object directives {

  def spotifyToken: Directive1[UserToken] =
    Directives.headerValueByName("SPOTIFY-TOKEN").map(UserToken)
}
