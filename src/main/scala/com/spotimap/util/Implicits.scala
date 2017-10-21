package com.spotimap.util

import akka.http.scaladsl.marshalling.{ToResponseMarshallable, ToResponseMarshaller}
import cats.Monad
import com.spotimap.SpotifyProgram
import com.spotimap.client.impl.SpotifyInterpreter

import scala.concurrent.ExecutionContext
import scala.language.{higherKinds, implicitConversions}

object Implicits {
  implicit val globalEC: ExecutionContext = ExecutionContext.Implicits.global

  implicit def convert[A, F[_]: Monad](program: SpotifyProgram[A])(
    implicit interpreter: SpotifyInterpreter[F],
    marshaller: ToResponseMarshaller[F[A]]): ToResponseMarshallable =
    ToResponseMarshallable(program.foldMap(interpreter))
}
