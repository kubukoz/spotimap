package com.kubukoz.spotify.client.impl

import akka.http.scaladsl.model.{HttpHeader, HttpMethod, RequestEntity}
import io.circe.{Decoder, Encoder}

import scala.language.higherKinds

trait HttpClient[F[_]] {
  private[client] def serialize[T: Encoder](input: T): F[RequestEntity]

  private[client] def httpCallRaw[T: Decoder](method: HttpMethod,
                                              url: String,
                                              body: Option[RequestEntity],
                                              headers: List[HttpHeader]): F[T]
}
