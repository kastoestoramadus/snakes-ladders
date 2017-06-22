package org.kastoestoramadus.ladders.model

import scala.util.Random

// TODO move everything else to same root package

object Board {
  // could be configurable
  val numberOfSquares = 100
  val laddersAndSnakes = Map(6 -> 36, 55 -> 70, 92 -> 100, 12 -> 2, 40 -> 24, 67 -> 51)
}

case class Moved(player: PlayerId, by: Int)

case class Game(players: Seq[PlayerId]){
  import Game.determineWhoIsFirst

  private[model] var gameState: GameState = GameInProgress(
    players.map(p => p -> 1).toMap[PlayerId, BoardPosition], determineWhoIsFirst(players), players)

  def nextMove(): Either[HaveWon, Moved] = {
    val r = gameState.performNextMove()
    gameState = r.state
    r.changed
  }
  def playersPositions: Map[PlayerId, BoardPosition] = gameState.playersPositions
  def isFinished: Boolean = gameState.isFinished
  def nextMoves: PlayerId = gameState.nextMoves
}

object Game {
  def initForPlayers(playersNames: Seq[PlayerId]): Game = new Game(playersNames)
  private val rand = new Random()

  def rollDie() = Math.abs(rand.nextInt()) % 6 + 1

  private[model] def determineWhoIsFirst(players: Seq[PlayerId]): Int = {
    val choosen = rand.shuffle(players).head
    players.indexOf(choosen)
  }
}

