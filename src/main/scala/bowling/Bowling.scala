package geezeo.bowling

import scala.util.{ Try, Success, Failure }

class BowlingGame(game: String) {

  def score: Try[Int] = ???

}

object BowlingGame {

  case class GameTooShort(missing: Int)
    extends IllegalStateException(s"Game too short by ${ missing } throws")

  case class GameTooLong(extra: Int)
    extends IllegalStateException(s"Game too long by ${ extra } throws")

  case class SpareTooEarly(frame: Int)
    extends IllegalStateException(s"Spare occured too early in frame ${ frame }")

  case class StrikeTooLate(frame: Int)
    extends IllegalStateException(s"Strike occured too late in frame ${ frame }")

  case class TooManyPins(frame: Int, pins: Int)
    extends IllegalStateException(s"Too many pins were knocked down in frame ${ frame }, found ${ pins }")

  case class UnrecognizedThrow(frame: Int, character: String)
    extends IllegalStateException(s"Unrecognized throw in frame ${ frame }, found '${ character }'")

}