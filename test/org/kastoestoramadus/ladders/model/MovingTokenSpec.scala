package org.kastoestoramadus.ladders.model

import org.scalatest.{FeatureSpec, GivenWhenThen}

class MovingTokenSpec extends FeatureSpec with GivenWhenThen{
  val firstPlayerName = "Joe"

  feature("Token Can Move Across the Board") {
    info("As a player. I want to be able to move my token. So that I can get closer to the goal")
    scenario("Init player") {
      Given("the game is started")
      val game = Game.initForPlayers(Seq(firstPlayerName))

      When("When the token is placed on the board")
      Then("the token is on square 1")
      game.playersPositions(firstPlayerName) == 1
    }
    scenario("Move player") {
      Given("the token is on square 1")
      val game = Game.initForPlayers(Seq(firstPlayerName))
        .gameState.asInstanceOf[GameInProgress]

      When("the token is moved 3 spaces")
      val newState = game.asInstanceOf[GameInProgress]
        .move(firstPlayerName, 3).state

      Then("the token is on square 4")
      newState.playersPositions(firstPlayerName) == 4
    }
  }
  feature("Moves Are Determined By Dice Rolls") {
    info("As a player. I want to move my token based on the roll of a die. So that there is an element of chance in the game")
    scenario("proper roll range") {
      Given("the game is started")
      When("the player rolls a die")
      val move = Game.rollDie() // not deterministic test
      Then("the result should be between 1-6 inclusive")
      (move <=6) && (move >= 1)
    }
    scenario("move by roll") {
      Given("the player rolls") // scenario modified, alternative would be using ScalaCheck
      val game = Game.initForPlayers(Seq(firstPlayerName))
      When("they move their token")
      val by = game.nextMove().right.get.by
      Then("the token should move by rolled number of spaces")
      game.playersPositions(firstPlayerName) == 1 + by
    }
  }
  feature("Player Can Win the Game") {
    info("As a player. I want to be able to win the game. So that I can gloat to everyone around.")
    scenario("wins by exact move") {
      Given("the token is on square 99") // one case skipped, remained was modified
      val game = GameInProgress(Map(firstPlayerName -> 99), 0, Seq(firstPlayerName))
      val newState = game.move(firstPlayerName, 98).state
      When("the token is moved 3 spaces")
      val won = newState.nextMove().changed
      Then("the token is on square 100")
      newState.playersPositions(firstPlayerName) == 100
      And("the player has won the game")
      won.isLeft == true
    }
  }
}
