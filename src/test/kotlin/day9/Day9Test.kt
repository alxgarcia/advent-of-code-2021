package day9

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day9Test {
  private val sampleData = parseMap(
    listOf(
      "2199943110",
      "3987894921",
      "9856789892",
      "8767896789",
      "9899965678"
    )
  )

  @Test
  fun `build map from input`() {
    val input = listOf("01", "23")
    val expected = arrayOf(arrayOf(0, 1), arrayOf(2, 3))

    val result = parseMap(input)

    expected.forEachIndexed { ir, r ->
      r.forEachIndexed { ic, c ->
        val actual = result[ir][ic]
        assertEquals(c, actual, "map[$ir][$ic] should be $c but it's $actual")
      }
    }
  }

  @Test
  fun `sum of local minimums`() {
    assertEquals(15, sumOfLocalMinimums(sampleData))
  }

  @Test
  fun `size of a basin`() {
    assertEquals(9, computeBasinSize(0, 9, sampleData))
    assertEquals(3, computeBasinSize(0, 1, sampleData))
    assertEquals(9, computeBasinSize(4, 6, sampleData))
    assertEquals(14, computeBasinSize(2, 2, sampleData))
  }

  @Test
  fun `product of the size of the three largest basins`() {
    assertEquals(1134, productOfSizeThreeLargestBasins(sampleData))
  }
}
