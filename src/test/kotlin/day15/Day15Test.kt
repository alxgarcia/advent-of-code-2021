package day15

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day15Test {
  private val sampleData = listOf(
    "1163751742",
    "1381373672",
    "2136511328",
    "3694931569",
    "7463417111",
    "1319128137",
    "1359912421",
    "3125421639",
    "1293138521",
    "2311944581",
  )

  @Test
  fun `part 1`() {
    val riskMatrix = parseInput(sampleData)
    assertEquals(40, solve1(riskMatrix))
  }

  @Test
  fun `risk function part 2`() {
    val riskMatrix = parseInput(listOf("8"))
    val riskFn = riskFn2(riskMatrix)
    val riskFnExpectedResultMatrix = listOf(
      listOf(8, 9, 1, 2, 3),
      listOf(9, 1, 2, 3, 4),
      listOf(1, 2, 3, 4, 5),
      listOf(2, 3, 4, 5, 6),
      listOf(3, 4, 5, 6, 7),
    )

    (0..4).forEach { r ->
      (0..4).forEach { c ->
        val expected = riskFnExpectedResultMatrix[r][c]
        val got = riskFn(r to c)
        assertEquals(expected, got, "Expected $expected at ($r,$c) but got $got")
      }
    }
  }

  @Test
  fun `part 2`() {
    val riskMatrix = parseInput(sampleData)
    assertEquals(315, solve2(riskMatrix))
  }
}