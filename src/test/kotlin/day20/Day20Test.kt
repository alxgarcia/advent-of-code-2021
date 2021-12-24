package day20

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.test.assertContentEquals

internal class Day20Test {
  @Test
  fun `count light pixels in an image`() {
    val image = listOf(
      "..........#....",
      "....#..#.#.....",
    )
    assertEquals(4, countLightPixels(image))
  }

  @Test
  fun `enhances image in some iterations`() {
    val iterations = 2
    val (enhancement, image) = parseInput(sampleData.lines())

    val expectedImage = """
      .......#...
      .#..#.#....
      #.#...###..
      #...##.#...
      #.....#.#..
      .#.#####...
      ..#.#####..
      ...##.##...
      ....###....
    """.trimIndent().lines()

    assertContentEquals(expectedImage, enhanceImage(enhancement, image, iterations))
  }

  private val sampleData = """
    ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

    #..#.
    #....
    ##..#
    ..#..
    ..###
  """.trimIndent()
}