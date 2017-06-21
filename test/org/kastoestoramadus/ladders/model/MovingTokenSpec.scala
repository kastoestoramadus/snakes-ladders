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

      When("the token is moved 3 spaces")
      game.move(firstPlayerName, 3)

      Then("the token is on square 4")
      game.playersPositions(firstPlayerName) == 4
    }
    scenario("further move") {
      Given("the token is on square 1")
      val game = Game.initForPlayers(Seq(firstPlayerName))

      When("the token is moved 3 spaces")
      game.move(firstPlayerName, 3)

      And("then it is moved 4 spaces")
      game.move(firstPlayerName, 4)

      Then("the token is on square 8")
      game.playersPositions(firstPlayerName) == 8
    }
  }
  feature("Moves Are Determined By Dice Rolls") {
    info("As a player. I want to move my token based on the roll of a die. So that there is an element of chance in the game")
    scenario("proper roll range") {
      Given("the game is started")
      val game = Game.initForPlayers(Seq(firstPlayerName))
      When("the player rolls a die")
      val move = game.rollDie() // not deterministic test
      Then("the result should be between 1-6 inclusive")
      (move <=6) && (move >= 1)
    }
    """



      |Given the player rolls a 4
      |When they move their token
      |Then the token should move 4 spaces
    """.stripMargin
  }
}
