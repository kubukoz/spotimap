package com.spotimap.util

import akka.http.scaladsl.marshalling.{ToResponseMarshallable, ToResponseMarshaller}
import cats.Monad
import cats.free.Free
import com.spotimap.client.{SpotifyInterpreter, SpotifyOp}

import scala.concurrent.ExecutionContext
import scala.language.higherKinds
import scala.language.implicitConversions

object Implicits {
  implicit val globalEC: ExecutionContext = ExecutionContext.Implicits.global

  implicit def convert[A, F[_] : Monad](op: Free[SpotifyOp, A])(implicit interpreter: SpotifyInterpreter[F],
                                                                marshaller: ToResponseMarshaller[F[A]]): ToResponseMarshallable =
    ToResponseMarshallable(op.foldMap(interpreter))
}
