package com.spotimap.client

import com.spotimap.model.external.SpotifyToken
import io.circe.Decoder

sealed trait SpotifyAlgebra[Response]

object SpotifyAlgebra {
  case class Get[Response](url: String, token: SpotifyToken, decoder: Decoder[Response])
      extends SpotifyAlgebra[Response]
}
