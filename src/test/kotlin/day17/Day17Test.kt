package day17

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day17Test {
  @Test
  fun `test part 1 - max vertical speed the probe can reach without missing the target`() {
    val input = "target area: x=20..30, y=-10..-5"
    val (_, rangeY) = parseInput(input)
    val (time, maxInitialVerticalSpeed) = findMaxInitialVerticalVelocity(rangeY)
    assertEquals(9, maxInitialVerticalSpeed)
    assertEquals(45, computeMaxHeight(time, maxInitialVerticalSpeed))
  }

  @Test
  fun `problem part 1 - max vertical speed the probe can reach without missing the target`() {
    val input = "target area: x=277..318, y=-92..-53"
    val (_, rangeY) = parseInput(input)
    val (time, maxInitialVerticalSpeed) = findMaxInitialVerticalVelocity(rangeY)
    assertEquals(91, maxInitialVerticalSpeed)
    assertEquals(4186, computeMaxHeight(time, maxInitialVerticalSpeed))
  }

  @Test
  fun `test part 2 - number of unique solutions`() {
    val input = "target area: x=20..30, y=-10..-5"
    val (rangeX, rangeY) = parseInput(input)
    assertEquals(112, findAllPossibleSolutions(rangeX, rangeY).size)
  }

  @Test
  fun `problem part 2 - number of unique solutions`() {
    val input = "target area: x=277..318, y=-92..-53"
    val (rangeX, rangeY) = parseInput(input)
    assertEquals(2709, findAllPossibleSolutions(rangeX, rangeY).size)
  }
}
