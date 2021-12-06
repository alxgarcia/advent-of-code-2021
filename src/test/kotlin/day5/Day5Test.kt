package day5

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class Day5Test {
  private val sampleData = buildMatrix(
    listOf(
      "0,9 -> 5,9",
      "8,0 -> 0,8",
      "9,4 -> 3,4",
      "2,2 -> 2,1",
      "7,0 -> 7,4",
      "6,4 -> 2,0",
      "0,9 -> 2,9",
      "3,4 -> 1,4",
      "0,0 -> 8,8",
      "5,5 -> 8,2"
    )
  )

  @Test
  fun regex() {
    assertEquals(listOf(0, 9, 5, 9), parseLine("0,9 -> 5,9"))
    assertEquals(listOf(0, 19, 15, 9), parseLine("0,19 -> 15,9"))
  }

  @Test
  fun expandRange() {
    assertEquals(listOf(1 to 0, 1 to 1, 1 to 2, 1 to 3, 1 to 4), expandSegmentRange(1, 0, 1, 4))
    assertEquals(listOf(1 to 0, 2 to 0, 3 to 0), expandSegmentRange(1, 0, 3, 0))
    assertEquals(listOf(1 to 0, 2 to 1, 3 to 2), expandSegmentRange(1, 0, 3, 2))

    assertEquals(listOf(3 to 0, 2 to 1, 1 to 2), expandSegmentRange(3, 0, 1, 2))
  }

  @Test
  fun `build matrix from cloudy segments`() {
    val input1 = listOf("0,0 -> 0,0")
    val expected1 = mapOf((0 to 0) to 1)
    assertEquals(expected1, buildMatrix(input1))

    val input2a = listOf("0,0 -> 0,3")
    val input2b = listOf("0,3 -> 0,0")
    val expected2 = mapOf(
      (0 to 0) to 1,
      (0 to 1) to 1,
      (0 to 2) to 1,
      (0 to 3) to 1,
    )
    assertEquals(expected2, buildMatrix(input2a))
    assertEquals(expected2, buildMatrix(input2b))

    val input3 = listOf("0,0 -> 0,3", "0,1 -> 0,2")
    val expected3 = mapOf(
      (0 to 0) to 1,
      (0 to 1) to 2,
      (0 to 2) to 2,
      (0 to 3) to 1,
    )
    assertEquals(expected3, buildMatrix(input3))
  }

  @Test
  fun `number of points where at least two cloudy segments overlap`() {
    assertEquals(12, countCloudSegmentOverlappingPoints(sampleData))
  }
}