package com.spotimap.client

import akka.http.scaladsl.model.{HttpHeader, HttpMethod, RequestEntity}
import cats.Monad
import io.circe.{Decoder, Encoder}

import scala.language.higherKinds

abstract class HttpClient[+F[_] : Monad]{
  private[client] def serialize[T : Encoder](input: T): F[RequestEntity]

  private[client] def httpCallRaw[T: Decoder](method: HttpMethod, url: String,
                                              body: Option[RequestEntity], headers: List[HttpHeader]): F[T]
}
