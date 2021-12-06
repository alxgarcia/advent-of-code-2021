package day1

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Test {
  @Test
  fun `counts increments in-between measurements`() {
    assertEquals(0, countIncreasingDeltas(listOf()))
    assertEquals(0, countIncreasingDeltas(listOf(1)))
    assertEquals(1, countIncreasingDeltas(listOf(1, 2)))
    assertEquals(0, countIncreasingDeltas(listOf(2, 1)))
    assertEquals(2, countIncreasingDeltas(listOf(1, 2, 2, 4)))
  }

  @Test
  fun `counts increments for aggregates of three consecutive measurements`() {
    assertEquals(0, countIncreasingDeltas2(listOf()))
    assertEquals(0, countIncreasingDeltas2(listOf(1)))
    assertEquals(0, countIncreasingDeltas2(listOf(1, 2)))
    assertEquals(0, countIncreasingDeltas2(listOf(1, 2, 3)))
    assertEquals(0, countIncreasingDeltas2(listOf(2, 1, 1, 1)))
    assertEquals(1, countIncreasingDeltas2(listOf(1, 2, 2, 4)))
  }
}