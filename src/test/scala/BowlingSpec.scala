package geezeo.bowling

import org.scalatest.{FunSpec, Matchers, TryValues}

import scala.util.{ Try, Success, Failure }

class BowlingSpec extends FunSpec with Matchers {

  import TryValues._

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

    val TooShort     = List("-------------------", "XXXXXXXXXX")
    val TooEarly     = List("/-------------------")
    val Pins         = List("55------------------")
    val TooLate      = List("-X------------------")
    val Unrecognized = List("---------foo--------")
    val TooLong      = List("---------------------")

    TooShort.foreach { frame =>
      it (s"${ frame } is invalid for reason: Game too short") {
        val bowl = new BowlingGame(frame)

        bowl.score.failure.exception shouldBe a [GameTooShort]
      }
    }

    TooEarly.foreach { frame =>
      it (s"${ frame } is invalid for reason: Spare too early") {
        val bowl = new BowlingGame(frame)

        bowl.score.failure.exception shouldBe a [SpareTooEarly]
      }
    }

    Pins.foreach { frame =>
      it (s"${ frame } is invalid for reason: Too many pins") {
        val bowl = new BowlingGame(frame)

        bowl.score.failure.exception shouldBe a [TooManyPins]
      }
    }

    TooLate.foreach { frame =>
      it (s"${ frame } is invalid for reason: Strike too late") {
        val bowl = new BowlingGame(frame)

        bowl.score.failure.exception shouldBe a [StrikeTooLate]
      }
    }

    Unrecognized.foreach { frame =>
      it (s"${ frame } is invalid for reason: Unrecognized throw") {
        val bowl = new BowlingGame(frame)

        bowl.score.failure.exception shouldBe a [UnrecognizedThrow]
      }
    }

    TooLong.foreach { frame =>
      it (s"${ frame } is invalid for reason: Game too long") {
        val bowl = new BowlingGame(frame)

        bowl.score.failure.exception shouldBe a [GameTooLong]
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

      val TooShort = List("X4")
      val TooLong  = List("444")
      val TooEarly = List("/44", "X/4", "4//")
      val TooLate  = List("X4X", "4X4")
      val Pins     = List("99", "X99")

      TooShort.foreach { case frame =>
        it (s"${ frame } is an invalid tenth frame for reason: Game too short") {
          val bowl = new BowlingGame("------------------" + frame)

          bowl.score.failure.exception shouldBe a [GameTooShort]
        }
      }

      TooLong.foreach { case frame =>
        it (s"${ frame } is an invalid tenth frame for reason: Game too long") {
          val bowl = new BowlingGame("------------------" + frame)

          bowl.score.failure.exception shouldBe a [GameTooLong]
        }
      }

      TooEarly.foreach { case frame =>
        it (s"${ frame } is an invalid tenth frame for reason: Spare too early") {
          val bowl = new BowlingGame("------------------" + frame)

          bowl.score.failure.exception shouldBe a [SpareTooEarly]
        }
      }

      TooLate.foreach { case frame =>
        it (s"${ frame } is an invalid tenth frame for reason: Spare too late") {
          val bowl = new BowlingGame("------------------" + frame)

          bowl.score.failure.exception shouldBe a [StrikeTooLate]
        }
      }

      Pins.foreach { case frame =>
        it (s"${ frame } is an invalid tenth frame for reason: Too many pins") {
          val bowl = new BowlingGame("------------------" + frame)

          bowl.score.failure.exception shouldBe a [TooManyPins]
        }
      }

    }
  }

}
