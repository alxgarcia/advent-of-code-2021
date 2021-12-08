package day7

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day7Test {
  private val sampleData = "16,1,2,0,4,2,7,1,2,14"

  @Test
  fun `find cheapest position to align`() {
    val mapOfPositions = parseInput(sampleData)

    assertEquals(37, minFuelSpentToAlignedPosition(mapOfPositions))
  }

  @Test
  fun `find new cheapest position to align - part 2`() {
    val mapOfPositions = parseInput(sampleData)

    assertEquals(168, minFuelSpentToAlignedPositionPart2(mapOfPositions))
  }
}
