package com.spotimap

import akka.http.scaladsl.server.{Directive1, Directives}
import com.spotimap.model.external.SpotifyToken

package object directives {
  def spotifyToken: Directive1[SpotifyToken] = Directives.headerValueByName("SPOTIFY-TOKEN").map(SpotifyToken)
}
