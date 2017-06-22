package org.kastoestoramadus.ladders.model

import org.scalatest.{FeatureSpec, GivenWhenThen}

class SnakesAndLaddersSpec extends FeatureSpec with GivenWhenThen {
  val firstPlayerName = "Zoe"

  feature("Snakes Go Down, Not Up") {
    info("As a player. I want snakes to move my token down. So that the game is more fun.")
    scenario("Go down with snake") {
      Given("Given there is a snake connecting squares 2 and 12")
      val init = GameInProgress(
        Map(firstPlayerName -> 10), 0, Seq(firstPlayerName))
      When("the token lands on square 12")
      val result = init.move(firstPlayerName, 2)
      Then("the token is on square 2")
      assert(result.state.playersPositions(firstPlayerName) == 2)
      assert(result.changed.right.get.by == -8)
    }
    scenario("Don't go up with snake") {
      Given("there is a snake connecting squares 2 and 12")
      val init = GameInProgress(
        Map(firstPlayerName -> 1), 0, Seq(firstPlayerName))
      When("the token lands on square 2")
      val result = init.move(firstPlayerName, 1).state
      Then("the token is on square 2")
      assert(result.playersPositions(firstPlayerName) == 2)
    }
  }
  feature("Ladders Go Up, Not Down") {
    info("As a player.I want ladders to move my token up.So that the game is more fun.")
    scenario("Go up with ladder") {
      Given("there is a ladder connecting squares 6 and 36")
      val init = GameInProgress(
        Map(firstPlayerName -> 2), 0, Seq(firstPlayerName))
      When("the token lands on square 6")
      val result = init.move(firstPlayerName, 4).state
      Then("the token is on square 36")
      assert(result.playersPositions(firstPlayerName) == 36)
    }
    scenario("Don't go down with ladder") {
      Given("there is a ladder connecting squares 6 and 36")
      val init = GameInProgress(
        Map(firstPlayerName -> 31), 0, Seq(firstPlayerName))
      When("the token lands on square 36")
      val result = init.move(firstPlayerName, 5)
      Then("the token is on square 36")
      assert(result.state.playersPositions(firstPlayerName) == 36)
      assert(result.changed.right.get.by == 5)
    }
  }
}












