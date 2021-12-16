package day14

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day14Test {
  private val sampleData = listOf(
    "NNCB",
    "",
    "CH -> B",
    "HH -> N",
    "CB -> H",
    "NH -> C",
    "HB -> C",
    "HC -> B",
    "HN -> C",
    "NN -> C",
    "BH -> H",
    "NC -> B",
    "NB -> B",
    "BN -> B",
    "BB -> N",
    "BC -> B",
    "CC -> N",
    "CN -> C",
  )

  @Test
  fun `part 1`() {
    val (initialPolymer, insertions) = parseInput(sampleData)
    assertEquals(1588, solve(initialPolymer, insertions, 10))

  }

  @Test
  fun `part 2`() {
    val (initialPolymer, insertions) = parseInput(sampleData)
    assertEquals(2188189693529, solve(initialPolymer, insertions, 40))
  }
}