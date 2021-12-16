package day13

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day13Test {
  private val sampleData = listOf(
    "6,10",
    "0,14",
    "9,10",
    "0,3",
    "10,4",
    "4,11",
    "6,0",
    "6,12",
    "4,1",
    "0,13",
    "10,12",
    "3,4",
    "3,0",
    "8,4",
    "1,10",
    "2,14",
    "8,10",
    "9,0",
    "",
    "fold along y=7",
    "fold along x=5",
  )

  @Test
  fun `folds one time`() {
    val (dots, folds) = parseInput(sampleData)
    assertEquals(17, countVisibleDotsAfterFirstFolding(dots, folds))
  }

  @Test
  fun `folds two times, one on each axis`() {
    val (dots, folds) = parseInput(sampleData)
    assertEquals(16, countVisibleDotsAfterFolding(dots, folds))
  }
}