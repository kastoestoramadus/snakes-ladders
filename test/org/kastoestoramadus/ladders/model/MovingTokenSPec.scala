package org.kastoestoramadus.ladders.model

import org.scalatest.{FeatureSpec, GivenWhenThen}

class MovingTokenSPec extends FeatureSpec with GivenWhenThen{
  val firstPlayerName = "Joe"

  feature("Token Can Move Across the Board") {
    scenario(
      """As a player.
        | I want to be able to move my token.
        |  So that I can get closer to the goal""".stripMargin) { // TODO better string interpolator
      Given("the game is started")
      val game = Game.initForPlayers(Seq(firstPlayerName))

      When("When the token is placed on the board")
      // done during init

      Then("the token is on square 1")
      game.playersPositions(firstPlayerName) == 1
    }
  }
"""
  Given

    Then
  Given the token is on square 1
  When the token is moved 3 spaces
  Then the token is on square 4
  Given the token is on square 1
  When the token is moved 3 spaces
  And then it is moved 4 spaces
  Then the token is on square 8
  """
}
