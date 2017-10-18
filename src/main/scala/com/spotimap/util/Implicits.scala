package com.spotimap.util

import scala.concurrent.ExecutionContext

object Implicits {
  implicit val globalEC: ExecutionContext = ExecutionContext.Implicits.global
}
