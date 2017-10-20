package com.spotimap.client

import akka.http.scaladsl.model.HttpMethods.{GET, POST}
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import cats.Monad
import cats.instances.future._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.spotimap.model.external.SpotifyToken
import io.circe.{Decoder, Encoder}

import scala.language.higherKinds

class SpotifyClient[+F[_] : Monad](http: HttpClient[F]) {
  private val fullUrl: String => String = "https://api.spotify.com" + _

  private[client] def get[Response: Decoder](path: String, absolute: Boolean = false)
                                            (implicit token: SpotifyToken): F[Response] = {
    val url = transformUrl(absolute)(path)

    http.httpCallRaw[Response](GET, url, body = None, toHeaders(token))
  }

  private[client] def post[Request: Encoder, Response: Decoder](path: String, body: Request, absolute: Boolean = false)
                                                               (implicit token: SpotifyToken): F[Response] = {
    val url = transformUrl(absolute)(path)

    for {
      entity <- http.serialize(Encoder[Request])(body)
      result <- http.httpCallRaw[Response](POST, url, Some(entity), toHeaders(token))
    } yield result
  }

  private def transformUrl(absolute: Boolean): String => String = if (absolute) identity else fullUrl

  private def toHeaders(token: SpotifyToken): List[HttpHeader] = {
    val authorization = Authorization(OAuth2BearerToken(token.value))
    List(authorization)
  }

}
