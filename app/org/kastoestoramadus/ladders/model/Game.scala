package org.kastoestoramadus.ladders.model

import scala.util.Random

// TODO move everything else to same root package

object Board {
  val numberOfSquares = 100
}

case class Moved(player: PlayerId, by: Int)

case class Game(players: Seq[PlayerId]){

  private[model] var gameState: GameState = GameInProgress(
    players.map(p => p -> 1).toMap[PlayerId, BoardPosition], 0, players)

  def nextMove(): Either[HaveWon, Moved] = {
    val r = gameState.nextMove()
    gameState = r.state
    r.changed
  }
  def playersPositions = gameState.playersPositions
  def isFinished: Boolean = gameState.isFinished
}

object Game {
  def initForPlayers(playersNames: Seq[PlayerId]): Game = new Game(playersNames)

  private[model] val rollDie: () => Int = {
    val rand = new Random()
    () => Math.abs(rand.nextInt()) % 6 + 1
  }
}

