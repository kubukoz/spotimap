package com.spotimap

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.spotimap.Main.Result
import com.spotimap.client.SpotifyInterpreter
import org.scalatest.{Matchers, WordSpec}

class MapRoutesSpec extends WordSpec with ScalatestRouteTest with Matchers with MapRoutes {

  private val routes = mapRoutes

  "MapRoutes" should {

  }

  override implicit val interpreter: SpotifyInterpreter[Result] = ???
}

