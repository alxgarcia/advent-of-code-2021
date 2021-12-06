package day6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class Day6Test {
  private val sampleData = "3,4,3,1,2"

  @Test
  fun `naively count lanternfish population`() {
    val lanternFish = parseInput(sampleData)

    assertEquals(26, naivelyCountLanternfishPopulationAfterNDays(lanternFish, 18))
    assertEquals(5934, naivelyCountLanternfishPopulationAfterNDays(lanternFish, 80))
    // do not even try the one below with this approach :)
    // assertEquals(26984457539, naivelyCountLanternfishPopulationAfterNDays(lanternFish, 256))
  }

  @Test
  fun `count lanternfish population`() {
    val lanternFish = parseInput(sampleData)

    assertEquals(26, countLanternfishPopulationAfterNDays(lanternFish, 18))
    assertEquals(5934, countLanternfishPopulationAfterNDays(lanternFish, 80))
    assertEquals(26984457539, countLanternfishPopulationAfterNDays(lanternFish, 256))
  }
}
