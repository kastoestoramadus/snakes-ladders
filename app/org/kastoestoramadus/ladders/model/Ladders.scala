package org.kastoestoramadus.ladders.model

import scala.util.Random

// TODO move everything else to same root package

object Board {
  val numberOfSquares = 100
}

class PlayerState(position: BoardPosition) {
  def advance(by: Int) = ???
}

case class Game(players: Seq[PlayerId]) {
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