package org.kastoestoramadus.ladders.model

import org.scalatest.{FeatureSpec, GivenWhenThen}

class ComputerPlayerSpec extends FeatureSpec with GivenWhenThen {
  val humanPlayerName = "Johny"

  feature("Computer Controlled Character") {
    info("As a player.I want to play against the computer.So that I can play even when I don't have friends.")
    scenario("Determining Play Order") {
      Given("the game is started")
      val game = Game.initForPlayers(Seq(humanPlayerName),
        noOfComputerPlayers = 1)
      When("it is the computer's turn")
      if (game.event == 0) assert(!game.nextMoves.endsWith(Game.COMPUTER_SUFFIX))
      game.performNextMove()

      Then("the computer should roll and move it's token")
      val computerPosition = game.playersPositions.filterNot(_ == humanPlayerName).head._2
      assert(computerPosition > 1)
      assert(game.playersPositions(humanPlayerName) > 1)
    }
    scenario("Computers only game finishes") {
      Given("Only computers")
      When("Game starts")
      val game = Game.initForPlayers(Seq(), noOfComputerPlayers = 9)
      Then("It ends with the winner")
      assert(game.isFinished)
    }
  }
}
