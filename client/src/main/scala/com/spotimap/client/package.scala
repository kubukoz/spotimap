package com.spotimap

import cats.free.Free
import com.spotimap.client.api.SpotifyAlgebra

package object client {
  type SpotifyProgram[A] = Free[SpotifyAlgebra, A]
}
