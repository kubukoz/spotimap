package com.kubukoz.spotify

import cats.free.Free
import com.kubukoz.spotify.client.api.SpotifyAlgebra

package object client {
  type SpotifyProgram[A] = Free[SpotifyAlgebra, A]
}
