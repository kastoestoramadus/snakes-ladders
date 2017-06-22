package org.kastoestoramadus.ladders.model

import scala.util.Random

// TODO move everything else to same root package

object Board {
  // could be configurable
  val numberOfSquares = 100
  val laddersAndSnakes = Map(6 -> 36, 55 -> 70, 92 -> 100, 12 -> 2, 40 -> 24, 67 -> 51)
}

case class Moved(player: PlayerId, by: Int)

case class Game(players: Seq[PlayerId]) {
  require(players.size > 0)
  var pass = 0

  // FIXME weak type for adding Computer
  import Game.{COMPUTER_SUFFIX, determineWhoIsFirst}

  private[model] var gameState: GameState = GameInProgress(
    players.map(p => p -> 1).toMap[PlayerId, BoardPosition],
    determineWhoIsFirst(players),
    players)

  if (nextMoves.endsWith(COMPUTER_SUFFIX)) performNextMove()

  def performNextMove(): Either[HaveWon, Moved] = {
    val r = gameState.performNextMove()
    gameState = r.state
    pass += 1
    if (nextMoves.endsWith(COMPUTER_SUFFIX) && !isFinished)
      performNextMove()
    else
      r.changed
  }

  def playersPositions: Map[PlayerId, BoardPosition] = gameState.playersPositions

  def isFinished: Boolean = gameState.isFinished

  def nextMoves: PlayerId = gameState.nextMoves
}

object Game {
  def initForPlayers(playersNames: Seq[PlayerId],
                     noOfComputerPlayers: Int = 0): Game = {
    val players = playersNames ++ (1 to noOfComputerPlayers).map(_.toString + COMPUTER_SUFFIX)
    require(players.toSet.size == players.size, "Each player name has to be unique")
    new Game(players)
  }

  private val rand = new Random()

  def rollDie() = Math.abs(rand.nextInt()) % 6 + 1

  private[model] def determineWhoIsFirst(players: Seq[PlayerId]): Int = {
    assert(players.size > 0)
    val choosen = rand.shuffle(players).head
    players.indexOf(choosen)
  }

  private[model] val COMPUTER_SUFFIX = "#Computer"
}

