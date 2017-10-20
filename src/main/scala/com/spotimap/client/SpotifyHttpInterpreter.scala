package com.spotimap.client

import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.HttpMethods.GET
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.{Monad, ~>}
import com.spotimap.client.SpotifyAlgebra.Get
import com.spotimap.model.external.SpotifyToken

import scala.language.higherKinds

trait SpotifyInterpreter[F[_]] extends (SpotifyAlgebra ~> F)

/**
  * An interpreter of the [[SpotifyAlgebra]] algebra using a HTTP Client.
  * */
class SpotifyHttpInterpreter[F[_]: Monad](client: HttpClient[F]) extends SpotifyInterpreter[F] {
  private def toHeaders(token: SpotifyToken): List[HttpHeader] = {
    val authorization = Authorization(OAuth2BearerToken(token.value))
    List(authorization)
  }

  override def apply[A](fa: SpotifyAlgebra[A]): F[A] = fa match {
    case Get(url, token, decoder) =>
      client.httpCallRaw[A](GET, url, body = None, toHeaders(token))(decoder)
  }

}
