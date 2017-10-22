package com.spotimap.routes

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.spotimap.Result
import org.scalatest.{Matchers, WordSpec}
import cats.instances.future._
import cats.syntax.applicative._

import scala.concurrent.ExecutionContext

trait BaseRouteSpec extends WordSpec with ScalatestRouteTest with Matchers {

  def wrap[A, B](value: A)(implicit ec: ExecutionContext): Result[B] =
    value.asInstanceOf[B].pure[Result]
}
