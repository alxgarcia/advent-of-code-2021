package day11

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals


internal class Day11Test {
  private val sampleData = parseMap(
    listOf(
      "5483143223",
      "2745854711",
      "5264556173",
      "6141336146",
      "6357385478",
      "4167524645",
      "2176841721",
      "6882881134",
      "4846848554",
      "5283751526",
    )
  )

  @Test
  fun `simulation of a step for bioluminescent octopuses - small sample and one step`() {
    val smallSample = parseMap(listOf(
      "11111",
      "19991",
      "19191",
      "19991",
      "11111",
    ))


    val expectedFlashes = 9
    val expectedMap = parseMap(
      listOf(
        "34543",
        "40004",
        "50005",
        "40004",
        "34543",
      )
    )

    val actualFlashes = simulateSteps(1, smallSample)
    expectedMap.zip(smallSample).forEachIndexed { i, (expected, actual) ->
      assertContentEquals(expected, actual, "fails at row $i")
    }
    assertEquals(expectedFlashes, actualFlashes)
  }

  @Test
  fun `simulation of a step for bioluminescent octopuses - small sample and two steps`() {
    val smallSample = parseMap(listOf(
      "11111",
      "19991",
      "19191",
      "19991",
      "11111",
    ))


    val expectedFlashes = 9
    val expectedMap = parseMap(
      listOf(
        "45654",
        "51115",
        "61116",
        "51115",
        "45654"
      )
    )

    val actualFlashes = simulateSteps(2, smallSample)
    expectedMap.zip(smallSample).forEachIndexed { i, (expected, actual) ->
      assertContentEquals(expected, actual, "fails at row $i")
    }
    assertEquals(expectedFlashes, actualFlashes)
  }

  @Test
  fun `count flashes in 100 steps`() {
    assertEquals(1656, simulateSteps(100, sampleData))
  }

  @Test
  fun `count steps until all octopuses synchronize`() {
    assertEquals(195, countStepsUntilAllOctopusesSynchronize(sampleData))
  }
}
