package com.spotimap.client

import cats.instances.future._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.spotimap.client.SpotifyOp.{Get, Post}
import com.spotimap.model.external.SpotifyToken
import io.circe.{Decoder, Encoder}

import scala.language.higherKinds

object SpotifyClient {
  private val fullUrl: String => String = "https://api.spotify.com" + _

  private[client] def get[Response: Decoder](path: String, absolute: Boolean = false)
                                            (implicit token: SpotifyToken): SpotifyOp[Response] = {
    val url = transformUrl(absolute)(path)
    Get(url, token, Decoder.apply)
  }

  private[client] def post[Request: Encoder, Response: Decoder](path: String, body: Request, absolute: Boolean = false)
                                                               (implicit token: SpotifyToken): SpotifyOp[Response] = {
    val url = transformUrl(absolute)(path)
    Post[Request, Response](url, body, token, Encoder.apply, Decoder.apply)
  }

  private def transformUrl(absolute: Boolean): String => String = if (absolute) identity else fullUrl
}
