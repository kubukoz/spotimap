package com.spotimap

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

trait BaseSpec extends WordSpec with ScalatestRouteTest with Matchers
