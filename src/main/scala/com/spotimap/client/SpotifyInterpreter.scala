package com.spotimap.client

import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.HttpMethods.{GET, POST}
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.{Monad, ~>}
import com.spotimap.client.SpotifyOp.{Get, Post}
import com.spotimap.model.external.SpotifyToken

import scala.language.higherKinds

class SpotifyInterpreter[F[_] : Monad](client: HttpClient[F]) extends (SpotifyOp ~> F) {
  private def toHeaders(token: SpotifyToken): List[HttpHeader] = {
    val authorization = Authorization(OAuth2BearerToken(token.value))
    List(authorization)
  }

  override def apply[A](fa: SpotifyOp[A]): F[A] = fa match {
    case Get(url, token, decoder) =>
      client.httpCallRaw[A](GET, url, None, toHeaders(token))(decoder)

    case Post(url, body, token, encoder, decoder) =>
      for {
        entity <- client.serialize(body)(encoder)
        result <- client.httpCallRaw[A](POST, url, Some(entity), toHeaders(token))(decoder)
      } yield result
  }

}
