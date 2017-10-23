package com.spotimap.client.impl

import akka.http.scaladsl.model.HttpMethods.{GET, POST}
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.{~>, Monad}
import com.spotimap.client.api.SpotifyAlgebra
import com.spotimap.client.api.SpotifyAlgebra.{Get, PostAsForm}
import com.spotimap.client.model.auth.SpotifyToken

import scala.language.higherKinds

trait SpotifyInterpreter[F[_]] extends (SpotifyAlgebra ~> F)

/**
  * An interpreter of the [[SpotifyAlgebra]] algebra using a HTTP Client.
  * */
class SpotifyHttpInterpreter[F[_]: Monad](client: HttpClient[F]) extends SpotifyInterpreter[F] {

  private def toHeaders(token: SpotifyToken): List[HttpHeader] = {
    token.value.map(OAuth2BearerToken).map(Authorization(_)).toList
  }

  override def apply[A](fa: SpotifyAlgebra[A]): F[A] = fa match {
    case Get(url, token, decoder) =>
      client.httpCallRaw[A](GET, url, body = None, toHeaders(token))(decoder)
    case PostAsForm(url, body, headers, decoder) =>
      client.httpCallRaw[A](POST, url, Some(body.toEntity), headers = headers)(decoder)
  }

}
