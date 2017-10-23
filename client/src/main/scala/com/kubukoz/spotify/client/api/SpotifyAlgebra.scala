package com.kubukoz.spotify.client.api

import akka.http.scaladsl.model.{FormData, HttpHeader}
import com.kubukoz.spotify.client.model.auth.SpotifyToken
import io.circe.Decoder

sealed trait SpotifyAlgebra[Response]

object SpotifyAlgebra {

  case class Get[Response](url: String, token: SpotifyToken, decoder: Decoder[Response])
      extends SpotifyAlgebra[Response]

  case class PostAsForm[Response](url: String, body: FormData, headers: List[HttpHeader], decoder: Decoder[Response])
      extends SpotifyAlgebra[Response]

}
