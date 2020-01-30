package geezeo.bowling

import org.scalatest.{FunSpec, Matchers}

import scala.util.{ Try, Success, Failure }

class BowlingSpec extends FunSpec with Matchers {

  describe("Scoring valid games") {

    val ValidGames = Map(
      "--------------------"  -> 0,
      "XXXXXXXXXXXX"          -> 300,
      "9-9-9-9-9-9-9-9-9-9-"  -> 90,
      "9/9/9/9/9/9/9/9/9/9/9" -> 190,
      "X7/729/XXX236/7/3"     -> 168,
      "-98--88-6/---59/8-7-"  -> 81,
      "X6/-67-8-8-X7/X35"     -> 125,
      "6-1761318-7-7/9-1613"  -> 79,
      "1/X7-9/8/446-5-7-71"   -> 110
    )

    ValidGames.foreach { case (game, expectedScore) =>
      it (s"${ game } is worth ${ expectedScore } points") {
        val bowl = new BowlingGame(game)

        bowl.score match {
          case Success(score) => score should be(expectedScore)
          case _              => fail
        }
      }
    }
  }

  describe("Identifying invalid games") {

    import BowlingGame._

    val InvalidGames = Map(
      "-------------------"   -> GameTooShort,
      "XXXXXXXXXX"            -> GameTooShort,
      "/-------------------"  -> SpareTooEarly,
      "55------------------"  -> TooManyPins,
      "-X------------------"  -> StrikeTooLate,
      "---------foo--------"  -> UnrecognizedThrow,
      "---------------------" -> GameTooLong
    )

    InvalidGames.foreach { case (game, error) =>
      it (s"${ game } is invalid for reason: ${ error.getClass.getSimpleName }") {
        val bowl = new BowlingGame(game)

        bowl.score match {
          case Success(_)       => fail("Game should be invalid")
          case Failure(`error`) => succeed
          case Failure(err)     => fail(s"Game failed for reason ${ err.getClass.getSimpleName } instead")
        }
      }
    }
  }

  describe("Tenth Frames") {

    describe("Valid Tenth Frames") {

      val ValidFrames = List("44", "9/9", "4/X", "X44", "X4/", "XX4", "XXX")

      ValidFrames.foreach { frame =>
        it(s"${ frame } is a valid tenth frame") {
          val bowl = new BowlingGame("------------------" + frame)

          bowl.score match {
            case Success(_)   => succeed
            case Failure(err) => fail(s"Game should not have failed for reason ${ err.getClass.getSimpleName }")
          }
        }
      }
    }

    describe("Invalid Tenth Frames") {

      import BowlingGame._

      val InvalidFrames = Map(
        "X4"  -> GameTooShort,
        "444" -> GameTooLong,
        "/44" -> SpareTooEarly,
        "X/4" -> SpareTooEarly,
        "4//" -> SpareTooEarly,
        "X4X" -> StrikeTooLate,
        "4X4" -> StrikeTooLate,
        "99"  -> TooManyPins,
        "X99" -> TooManyPins
      )

      InvalidFrames.foreach { case (game, error) =>
        it (s"${ game } is an invalid tenth frame for reason: ${ error.getClass.getSimpleName }") {
          val bowl = new BowlingGame("------------------" + game)

          bowl.score match {
            case Success(_)       => fail("Frame should be invalid")
            case Failure(`error`) => succeed
            case Failure(err)     => fail(s"Frame failed for reason ${ err.getClass.getSimpleName } instead")
          }
        }
      }

    }
  }

}
