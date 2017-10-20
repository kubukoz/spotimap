package com

import cats.free.Free
import com.spotimap.client.SpotifyAlgebra

import scala.concurrent.Future
import scala.language.higherKinds

package object spotimap {
  type SpotifyInterpreter[F[_]] = client.SpotifyInterpreter[F]
  type Result[+T]               = Future[T]
  type SpotifyProgram[A]        = Free[SpotifyAlgebra, A]
}
