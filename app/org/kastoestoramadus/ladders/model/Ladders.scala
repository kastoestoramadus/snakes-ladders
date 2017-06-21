package org.kastoestoramadus.ladders.model

import scala.util.Random

// TODO move everything else to same root package

object Board {
  val numberOfSquares = 100
}

case class Moved(player: PlayerId, by: Int)

case class Game(players: Seq[PlayerId]) {
  var nextPlayer: PlayerId = players.head

  def nextMove(): Moved = {
    val r = (nextPlayer, rollDie())
    (move _).tupled(r)
    // TODO rotate nextPlayer
    Moved.tupled(r)
  }

  private[model] val rollDie: () => Int = {
    val rand = new Random()
    () => (rand.nextInt()) % 6 + 1
  }

  private[model] def move(player: PlayerId, by: Int) = {
    val position = playersPositions(player)
    playersPositions += player -> (position + by)
  }


  var playersPositions = players.map(p => p -> 1).toMap[PlayerId, BoardPosition]

}

object Game {
  def initForPlayers(playersNames: Seq[PlayerId]): Game = new Game(playersNames)
}