package com.spotimap.client

import cats.instances.future._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.spotimap.client.SpotifyAlgebra.Get
import com.spotimap.config.SpotifyConfig.ApiPrefix
import com.spotimap.model.external.SpotifyToken
import io.circe.Decoder

import scala.language.higherKinds

object SpotifyClient {
  private val fullUrl: String => String = ApiPrefix + _

  private[client] def get[Response: Decoder](path: String, absolute: Boolean = false)(
    implicit token: SpotifyToken): SpotifyAlgebra[Response] = {

    val url = transformUrl(absolute)(path)
    Get(url, token, Decoder.apply)
  }

  private def transformUrl(absolute: Boolean): String => String = if (absolute) identity else fullUrl
}
