package day21

// Syntax sugar
private fun <N> Pair<N, N>.swap() = second to first
private fun Pair<Long, Long>.max() = if (first > second) first else second
private fun Pair<Long, Long>.addFirst(number: Long) = (first + number) to (second)
private fun Pair<Long, Long>.timesEach(number: Long) = (first * number) to (second * number)
private operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>) = (first + other.first) to (second + other.second)

data class GameState(val position: Int, val score: Int = 0)

class GameRules(private val minScoreToWin: Int) {
  fun move(gameState: GameState, increment: Int): GameState {
    val newPosition = ((gameState.position - 1 + increment) % 10) + 1
    return GameState(newPosition, gameState.score + newPosition)
  }

  fun hasWon(gameState: GameState): Boolean = gameState.score >= minScoreToWin
}

fun playDeterministicGame(initialPositionPlayer1: Int, initialPositionPlayer2: Int): Int {
  tailrec fun play(
    currentPlayer: GameState,
    otherPlayer: GameState,
    diceSeq: Sequence<Int>,
    diceRolledCount: Int,
    gameRules: GameRules
  ): Int {
    val p = gameRules.move(currentPlayer, diceSeq.take(3).sum())
    return if (gameRules.hasWon(p)) otherPlayer.score * (diceRolledCount + 3)
    else play(otherPlayer, p, diceSeq.drop(3), diceRolledCount + 3, gameRules)
  }

  val deterministicDice = sequence { while (true) yieldAll(1..100) }
  return play(GameState(initialPositionPlayer1), GameState(initialPositionPlayer2), deterministicDice, 0, GameRules(1_000))
}

fun countUniversesInWhichPlayerWins(initialPositionPlayer1: Int, initialPositionPlayer2: Int): Pair<Long, Long> {
  /*
  * Three dice with values 1, 2 and 3 generate the following 'universes'
  *  - 1x sum = 3; 1x[1, 1, 1]
  *  - 3x sum = 4; 3x[1, 1, 2]
  *  - 6x sum = 5; 3x[1, 1, 3] + 3x[1, 2, 2]
  *  - 7x sum = 6; 6x[1, 2, 3] + 1x[2, 2, 2]
  *  - 6x sum = 7; 3x[2, 2, 3] + 3x[1, 3 ,3]
  *  - 3x sum = 8; 3x[2, 3, 3]
  *  - 1x sum = 9; 1x[3, 3, 3]
  */
  fun incMultiplier(increment: Int): Long = when (increment) {
    3, 9 -> 1
    4, 8 -> 3
    5, 7 -> 6
    6 -> 7
    else -> 0
  }

  fun play(currentPlayer: GameState, otherPlayer: GameState, gameRules: GameRules): Pair<Long, Long> {
    var victoryCounter = 0L to 0L
    for (increment in 3..9) {
      val p = gameRules.move(currentPlayer, increment)
      if (gameRules.hasWon(p)) {
        victoryCounter = victoryCounter.addFirst(incMultiplier(increment))
      } else {
        victoryCounter += play(otherPlayer, p, gameRules).swap().timesEach(incMultiplier(increment))
      }
    }
    return victoryCounter
  }
  return play(GameState(initialPositionPlayer1), GameState(initialPositionPlayer2), GameRules(21))
}

fun main() {
  // I'm not parsing such a small input :)
  // Starting point for player 1 is 2
  // Starting point for player 2 is 5
  println("Part one: ${playDeterministicGame(2, 5)}")
  println("Part two: ${countUniversesInWhichPlayerWins(2, 5).max()}")
}