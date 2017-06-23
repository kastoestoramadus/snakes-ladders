package org.kastoestoramadus.ladders.model

import scala.util.Random

object Board {
  // could be configurable
  val numberOfSquares = 100
  val laddersAndSnakes = Map(6 -> 36, 55 -> 70, 92 -> 100, 12 -> 2, 40 -> 24, 67 -> 51)
}

case class Moved(player: PlayerId, by: Int)

case class Game(players: Seq[PlayerId],
                recorder: Transformed => Unit) {
  require(players.nonEmpty)
  var event = 0

  // FIXME weak type for adding Computer or to externalize AI (callbacks?)
  import Game.{COMPUTER_SUFFIX, determineWhoIsFirst}

  private[model] var gameState: GameState = GameInProgress(
    players.map(p => p -> 1).toMap[PlayerId, BoardPosition],
    determineWhoIsFirst(players),
    players)

  if (nextMoves.endsWith(COMPUTER_SUFFIX)) performNextMove()

  def performNextMove(): Either[HaveWon, Moved] = {
    val r = gameState.performNextMove()
    gameState = r.state
    event += 1
    recorder(r)
    if (nextMoves.endsWith(COMPUTER_SUFFIX) && !isFinished)
      performNextMove()
    else
      r.changed     // not returning all states ...
  }

  def loadSaveGame(saved: GameState) = {
    event += 1
    gameState = saved
  }

  def playersPositions: Map[PlayerId, BoardPosition] = gameState.playersPositions

  def isFinished: Boolean = gameState.isFinished

  def nextMoves: PlayerId = gameState.nextMoves
}

object Game {
  private val rand = new Random() // should I care for entropy ...

  private[model] val COMPUTER_SUFFIX = "#Computer"

  // TODO use logback
  val logger: Transformed => Unit = event => println(event.toString)
  private val dummy: Transformed => Unit = _ => ()

  def initForPlayers(playersNames: Seq[PlayerId],
                     noOfComputerPlayers: Int = 0,
                     recorder: Transformed => Unit = dummy
                    ): Game = {
    val players = playersNames ++ (1 to noOfComputerPlayers).map(_.toString + COMPUTER_SUFFIX)
    require(players.toSet.size == players.size, "Each player name has to be unique")
    new Game(players, recorder)
  }

  def rollDie(): Int = Math.abs(rand.nextInt()) % 6 + 1

  private[model] def determineWhoIsFirst(players: Seq[PlayerId]): Int = {
    assert(players.nonEmpty)
    val choosen = rand.shuffle(players).head
    players.indexOf(choosen)
  }
}

