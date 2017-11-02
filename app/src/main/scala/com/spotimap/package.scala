package com

import scala.concurrent.Future
import scala.language.higherKinds

package object spotimap {
  type Result[+T] = Future[T]
}
