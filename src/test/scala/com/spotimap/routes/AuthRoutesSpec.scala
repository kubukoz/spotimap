package com.spotimap.routes
import akka.http.scaladsl.model.FormData
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}
import cats.instances.future._
import com.spotimap.Result
import com.spotimap.client.api.SpotifyAlgebra
import com.spotimap.client.api.SpotifyAlgebra.PostAsForm
import com.spotimap.client.impl.SpotifyInterpreter
import com.spotimap.config.ApplicationConfig.SpotifyConfig.SpotifyClientConfig
import com.spotimap.config.ApplicationConfig.{ServerConfig, SpotifyConfig}
import com.spotimap.config.{ApplicationConfig, SpotifyConstants}
import com.spotimap.model.external.auth.Tokens
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._

class AuthRoutesSpec extends BaseRouteSpec with AuthRoutes {
  private val userToken         = "my-token"
  private val clientId          = "client-id"
  private val clientSecret      = "client-secret"
  private val authorizationCode = "auth-code"
  private val redirectUri       = "http://spotimap.com:80/auth/code"
  private val scopes            = "some scopes"
  private val refreshToken      = "refresh-token"

  private val returnedTokens = Tokens(
    accessToken = userToken,
    tokenType = "Bearer",
    scope = scopes,
    expiresIn = 3600,
    refreshToken = refreshToken
  )

  val AuthHeader = Authorization(BasicHttpCredentials(clientId, clientSecret))

  "AuthRoutes" should {
    "request the token properly" in {
      Get(s"/auth/code?code=$authorizationCode") ~> authRoutes ~> check {
        status shouldBe OK
        responseAs[Tokens] shouldBe returnedTokens
      }
    }
  }

  override implicit val interpreter: SpotifyInterpreter[Result] = new SpotifyInterpreter[Result] {
    override def apply[A](fa: SpotifyAlgebra[A]): Result[A] = fa match {
      case PostAsForm(SpotifyConstants.TokensUrl, formData, List(AuthHeader), _)
          if formData == FormData(
            "grant_type"   -> "authorization_code",
            "code"         -> authorizationCode,
            "redirect_uri" -> redirectUri
          ) =>
        wrap {
          returnedTokens
        }
    }
  }

  override implicit protected val config: ApplicationConfig =
    new ApplicationConfig(ServerConfig(80, "http://spotimap.com"),
                          SpotifyConfig(SpotifyClientConfig(clientId, clientSecret)))
}
