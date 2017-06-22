package controllers

import javax.inject._

import org.kastoestoramadus.ladders.model.{Game, Moved, PlayerId}
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class GameController @Inject() extends Controller {

  var game: Option[Game] = None // not atomic, TODO
  // synch | actor ~many parallel games
  /**
   * In browser health-check
   */
  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def resetTheGame(players: Seq[String]) = Action { implicit request =>
    game = Some(Game(players))
    Ok("Game (re)created")
  }
  def makeNextMove() = Action { implicit request =>
    game match {
      case Some(g) => // continue
        val effect = g.nextMove()
        Ok(s"Effect of move trial: $effect")
      case None => // there is no game started by you
        BadRequest("You have not created any game.")
    }
  }

  // TODO Pretty board state print endpoint
}
