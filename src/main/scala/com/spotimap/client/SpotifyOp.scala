package com.spotimap.client

import com.spotimap.model.external.SpotifyToken
import io.circe.{Decoder, Encoder}

sealed trait SpotifyOp[Response]

object SpotifyOp {

  case class Get[Response](url: String, token: SpotifyToken, decoder: Decoder[Response]) extends SpotifyOp[Response]

  case class Post[Request, Response](url: String, body: Request, token: SpotifyToken,
                                     encoder: Encoder[Request],
                                     decoder: Decoder[Response]) extends SpotifyOp[Response]

}
