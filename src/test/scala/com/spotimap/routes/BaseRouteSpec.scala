package com.spotimap.routes

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

trait BaseRouteSpec extends WordSpec with ScalatestRouteTest with Matchers
