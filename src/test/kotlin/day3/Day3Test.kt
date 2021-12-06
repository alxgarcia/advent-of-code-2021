package day3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3Test {
  private val sampleInput = listOf(
    "00100",
    "11110",
    "10110",
    "10111",
    "10101",
    "01111",
    "00111",
    "11100",
    "10000",
    "11001",
    "00010",
    "01010"
  )

  @Test
  fun `compute oxygen generator rating`() {
    assertEquals(23, computeOxygenGenerator(sampleInput))
  }

  @Test
  fun `compute CO2 scrubber rating`() {
    assertEquals(10, computeCO2Scrubber(sampleInput))
  }

  @Test
  fun `gamma is comprised of the most common bit in every position and epsilon the contrary`() {
    val singleBitInput = listOf("1", "0", "1", "1")
    assertEquals(Pair(1, 0), computeGammaAndEpsilon(singleBitInput))

    assertEquals(Pair(22, 9), computeGammaAndEpsilon(sampleInput))
  }
}
