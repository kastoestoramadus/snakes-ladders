package controllers

import org.scalatestplus.play._
import play.api.mvc.Result
import play.api.test._
import play.api.test.Helpers.{contentAsString, _}

import scala.concurrent.Future

class GameControllerSpec extends PlaySpec with OneAppPerTest {
  // free tests from template
  "HomeController GET" should {
    "render the index page from a new instance of controller" in {
      val controller = new GameController
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the index page from the application" in {
      val controller = app.injector.instanceOf[GameController]
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the index page from the router" in {
      // Need to specify Host header to get through AllowedHostsFilter
      val request = FakeRequest(GET, "/").withHeaders("Host" -> "localhost")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }
  }

  val playerName = "Boe"
  // relevant tests to the game
  "Game Controller" should {
    "start the game" in {
      val controller = new GameController
      val stepR = route(app,
        makeGetRequest(s"/reset-game?players=$playerName")
      ).get
      status(stepR) mustBe OK
      contentAsString(stepR) must include ("Game")
      contentAsString(stepR) must include ("created")
    }

    "make new moves" in {
      val contr = new GameController
      route(app,
        makeGetRequest(s"/reset-game?players=$playerName")
      ).get
      val request = makeGetRequest("/make-move")
      val result = route(app, request).get
      status(result) mustBe OK
      contentAsString(result) must include ("Moved")
      contentAsString(result) must include (playerName)
    }

    "lead to the end of the game" in {
      import scala.concurrent.ExecutionContext.Implicits.global
      val contr = new GameController
      route(app,
        makeGetRequest(s"/reset-game?players=$playerName")
      ).get
      val batch: Seq[Future[Result]] = for{
        i <- 1 to 99
        r <- route(app, makeGetRequest("/make-move"))
      } yield r

      batch.foreach(f => status(f) mustBe OK)
      val result = batch.last
      contentAsString(result) must include ("HaveWon")
      contentAsString(result) must include (playerName)
    }
  }

  def makeGetRequest(endpoint: String) =
    FakeRequest(GET, endpoint)
      .withHeaders("Host" -> "localhost")
}
