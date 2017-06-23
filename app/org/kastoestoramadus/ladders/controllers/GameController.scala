package org.kastoestoramadus.ladders.controllers

import javax.inject._

import org.kastoestoramadus.ladders.{Entry, SaveGameManger}
import org.kastoestoramadus.ladders.model.{Board, Game, GameState, Transformed}
import play.api.mvc._

// improvement: central module could be at dedicated space outside of controller
@Singleton
class GameController @Inject() extends Controller {
  var game: Option[Game] = {
    if(SaveGameManger.isMongoAvailable) {
      SaveGameManger.getLast().map(_.toGameState)
        .map{state => // ugly...
          val r = Game.initForPlayers(
            state.players, 0, getGameRecorder.get
          )
          r.loadSaveGame(state)
          r
        }
    } else None
  }
  // ? limit threads to 1 | actor ~many parallel games

  // In browser health-check
  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def resetTheGame(players: Seq[String], computersNo: Int) = Action { implicit request =>
    if (players.size + Math.abs(computersNo) > 0) {
      game = Some(Game.initForPlayers(
        players,
        computersNo,
        getGameRecorder.getOrElse(Game.logger)))
      Ok("Game (re)created")
    } else BadRequest("Provide a positive number of players. Ex.: ?players=Joe&noOfComputerPlayers=1")
  }

  // ? players authentication
  def makeNextMove() = Action { implicit request =>
    game match {
      case Some(g) => // continue
        val effect = g.performNextMove()
        Ok(s"Effect of move trial: $effect")
      case None => // there is no game started by you
        BadRequest("You have not created any game.")
    }
  }

  def playersPositions = Action { implicit request =>
    Ok("playersPositions:" + game.map(_.playersPositions))
  }

  def isFinished = Action { implicit request =>
    Ok("isFinished:" + game.map(_.isFinished))
  }

  def nextMoves = Action { implicit request =>
    Ok("nextMoves:" + game.map(_.nextMoves))
  }

  def board = Action { implicit request =>
    import Board._
    Ok(
      s"""
         |Board settings:
         | numberOfSquares = $numberOfSquares
         | laddersAndSnakes = $laddersAndSnakes
      """.stripMargin)
  }

  def entry(state: GameState): org.kastoestoramadus.ladders.Entry = {
    Entry(state.players, state.playersPositions, state.nextMoves)
  }

  def getGameRecorder: Option[Transformed => Unit] =
    if (SaveGameManger.isMongoAvailable)
      Some(
        trans => SaveGameManger.addEntry(entry(trans.state))
      )
    else None
}
