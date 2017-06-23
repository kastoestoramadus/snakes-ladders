package controllers

import org.kastoestoramadus.ladders.controllers.GameController
import org.kastoestoramadus.ladders.model.Board
import org.scalatestplus.play._
import play.api.mvc.Result
import play.api.test.Helpers.{contentAsString, _}
import play.api.test._

import scala.concurrent.Future

class GameControllerSpec extends PlaySpec with OneAppPerTest {

  val playerName = "Boe"
  val startString = s"/reset-game?players=$playerName&computersNo=0"

  // relevant tests to the game
  "Game Controller" should {
    "start the game" in {
      val controller = new GameController
      val stepR = route(app,
        makeGetRequest(startString)
      ).get
      status(stepR) mustBe OK
      contentAsString(stepR) must include("Game")
      contentAsString(stepR) must include("created")
    }

    "make new moves" in {
      val contr = new GameController
      route(app,
        makeGetRequest(startString)
      ).get
      val request = makeGetRequest("/make-move")
      val result = route(app, request).get
      status(result) mustBe OK
      contentAsString(result) must include("Moved")
      contentAsString(result) must include(playerName)
    }

    "lead to the end of the game" in {
      val contr = new GameController
      route(app,
        makeGetRequest(startString)
      ).get
      val batch: Seq[Future[Result]] = for {
        i <- 1 to (Board.numberOfSquares/2 )// might be not enough to finish but less logs
        r <- route(app, makeGetRequest("/make-move"))
      } yield r

      batch.foreach(f => status(f) mustBe OK)
      val result = batch.last
      contentAsString(result) must include("HaveWon")
      contentAsString(result) must include(playerName)
    }
  }

  def makeGetRequest(endpoint: String) =
    FakeRequest(GET, endpoint)
      .withHeaders("Host" -> "localhost")

  // skip check, free tests from template
  "HomeController GET" should {
    "render the index page from a new instance of controller" in {
      val controller = new GameController
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }

    "render the index page from the application" in {
      val controller = app.injector.instanceOf[GameController]
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }

    "render the index page from the router" in {
      // Need to specify Host header to get through AllowedHostsFilter
      val request = FakeRequest(GET, "/").withHeaders("Host" -> "localhost")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }
  }
  // end of skip
}
