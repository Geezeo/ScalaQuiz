# ScalaQuiz

Scala Test for applying to Geezeo

## Instructions

Implement the method `def geezeo.bowling.BowlingGame.score: Try[Int]` so that all the tests pass.  Feel free to add whatever additional methods, classes, etc. that you find helpful.

Run the tests using `sbt test`.  There are 32 tests, all should pass.

Throws are indicated as follows:

* "-": gutterball or miss.  0 pins were knocked down
* "1" - "9": that many pins were knocked down.
* "/": Spare
* "X": Strike

## Useful information

Here's a useful page to better understand bowling scoring: http://www.fryes4fun.com/Bowling/scoring.htm

Note that the 10th frame is a little trickier than the rest, so there is an additional bunch of tests to focus on those issues.
