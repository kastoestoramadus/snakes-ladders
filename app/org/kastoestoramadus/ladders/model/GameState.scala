package org.kastoestoramadus.ladders.model

case class Transformed(state: GameState, changed: Either[HaveWon, Moved])
case class HaveWon(player: PlayerId)

sealed trait GameState {
  def isFinished: Boolean
  def nextMove(): Transformed
  def playersPositions: Map[PlayerId, BoardPosition]
}

case class FinishedGame(playersPositions: Map[PlayerId, BoardPosition]) extends GameState {
  val whoWon = playersPositions.find(_._2 == 100).get._1
  override def isFinished: Boolean = true

  override def nextMove(): Transformed = Transformed(this, Left(HaveWon(whoWon)))
}

case class GameInProgress(playersPositions: Map[PlayerId, BoardPosition], nextPlayerNo: Int, players: Seq[PlayerId]) extends GameState{
  import Game.rollDie

  private[model] def move(player: PlayerId, by: Int): Transformed = {
    val r = Moved(player, by)
    val withNewPosition = playersPositions.updated(
      player, Math.min(playersPositions(player) + r.by, Board.numberOfSquares))
    if(playersPositions(player) >= Board.numberOfSquares) {
      Transformed(
        FinishedGame(withNewPosition),
        Left(HaveWon(player))
      )
    } else {
      Transformed(
        GameInProgress(withNewPosition, (nextPlayerNo + 1) % players.size, players),
        Right(r)
      )
    }
  }

  def nextMove(): Transformed = {
    val by = rollDie()
    val player = players(nextPlayerNo)
    move(player, by)
  }

  override def isFinished: Boolean = false
}