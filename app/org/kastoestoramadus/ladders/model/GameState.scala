package org.kastoestoramadus.ladders.model

case class Transformed(state: GameState, changed: Either[HaveWon, Moved])

case class HaveWon(player: PlayerId)

sealed trait GameState {
  def isFinished: Boolean

  def performNextMove(): Transformed

  def nextMoves: PlayerId

  def playersPositions: Map[PlayerId, BoardPosition]

  def players: Seq[PlayerId]
}

case class FinishedGame(playersPositions: Map[PlayerId, BoardPosition]) extends GameState {
  assert(playersPositions.exists(_._2 == 100), s"wrong posistions: $playersPositions") // Java assert is better

  val whoWon: PlayerId = playersPositions.find(_._2 == 100).get._1

  override def isFinished: Boolean = true

  override def performNextMove(): Transformed = Transformed(this, Left(HaveWon(whoWon)))

  override def nextMoves: PlayerId = whoWon

  override def players: Seq[PlayerId] = playersPositions.keys.toSeq
}

case class GameInProgress(playersPositions: Map[PlayerId, BoardPosition], nextPlayerNo: Int, players: Seq[PlayerId]) extends GameState {

  import Board.laddersAndSnakes
  import Game.rollDie

  private[model] def move(player: PlayerId, by: Int): Transformed = {
    val teleportedTo = {
      val byMove = playersPositions(player) + by
      if (laddersAndSnakes.isDefinedAt(byMove))
        laddersAndSnakes(byMove)
      else
        byMove
    }
    val moveEffect = Moved(player, teleportedTo - playersPositions(player))

    val withNewPosition = playersPositions.updated(player,
      Math.min(teleportedTo, Board.numberOfSquares))

    if (playersPositions(player) >= Board.numberOfSquares) {
      Transformed(
        FinishedGame(withNewPosition),
        Left(HaveWon(player))
      )
    } else {
      Transformed(
        GameInProgress(withNewPosition, (nextPlayerNo + 1) % players.size, players),
        Right(moveEffect)
      )
    }
  }

  def performNextMove(): Transformed = {
    val by = rollDie()
    val player = players(nextPlayerNo)
    move(player, by)
  }

  override def isFinished: Boolean = false

  override def nextMoves: PlayerId = players(nextPlayerNo)
}