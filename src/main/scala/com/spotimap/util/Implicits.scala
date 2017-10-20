package com.spotimap.util

import akka.http.scaladsl.marshalling.{ToResponseMarshallable, ToResponseMarshaller}
import cats.Monad
import cats.free.Free
import com.spotimap.{SpotifyProgram, SpotifyInterpreter}
import com.spotimap.client.SpotifyAlgebra

import scala.concurrent.ExecutionContext
import scala.language.{higherKinds, implicitConversions}

object Implicits {
  implicit val globalEC: ExecutionContext = ExecutionContext.Implicits.global

  implicit def convert[A, F[_] : Monad](program: SpotifyProgram[A])(implicit interpreter: SpotifyInterpreter[F],
                                                                    marshaller: ToResponseMarshaller[F[A]]): ToResponseMarshallable =
    ToResponseMarshallable(program.foldMap(interpreter))
}
