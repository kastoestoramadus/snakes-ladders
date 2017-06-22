package org.kastoestoramadus.ladders.model

import org.scalatest.{FeatureSpec, GivenWhenThen}

class MultiplePlayersSpec extends FeatureSpec with GivenWhenThen {
  val firstPlayerName = "May"
  val secondPlayerName = "Tay"

  feature("Many players") {
    info("As a player. I want to influence the play order. So that player 1 doesn't always go first.")
    scenario("Determining Play Order") {
      Given("there are two players")
      val players = Seq(firstPlayerName, secondPlayerName)
      When("the game is started")
      val games = (1 to 20).map(_ => Game.initForPlayers(players))
      Then("the players must roll dice to determine their play order")
      val playersWithFirstMove = games.map(_.nextMoves)
      assert(playersWithFirstMove.contains(firstPlayerName))
      assert(playersWithFirstMove.contains(secondPlayerName))
    }
    scenario("Both players are advancing") {
      Given("The game is started with two players")
      val players = Seq(firstPlayerName, secondPlayerName)
      val game = Game.initForPlayers(players)
      When("Two moves is performed")
      game.performNextMove()
      game.performNextMove()
      Then("Two players has moved")
      game.playersPositions.values.foreach(pos => assert(pos > 1))
    }
  }
}

