package day12

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class Day12Test {
  @Test
  fun `builds graph`() {
    val lines = listOf(
      "start-A",
      "start-b",
      "A-c",
      "A-b",
      "b-d",
      "A-end",
      "b-end",
    )

    val expectedMap = mapOf(
      "start" to listOf("A", "b"),
      "A" to listOf("start", "c", "b", "end"),
      "b" to listOf("start", "A", "d", "end"),
      "c" to listOf("A"),
      "d" to listOf("b"),
      "end" to listOf("A", "b")
    )

    val actualMap = parseInput(lines)

    expectedMap.forEach { (cave, expectedConnections) ->
      assertContentEquals(expectedConnections, actualMap[cave])
    }
  }

  @Test
  fun `counts paths that use small caves only once - 1`() {
    val map = parseInput(
      listOf(
        "start-A",
        "start-b",
        "A-c",
        "A-b",
        "b-d",
        "A-end",
        "b-end",
      )
    )
    assertEquals(10, countDistinctPathsThatVisitSmallCavesAtMostOnce(map))
  }

  @Test
  fun `counts paths that use small caves only once - 2`() {
    val map = parseInput(
      listOf(
        "dc-end",
        "HN-start",
        "start-kj",
        "dc-start",
        "dc-HN",
        "LN-dc",
        "HN-end",
        "kj-sa",
        "kj-HN",
        "kj-dc",
      )
    )
    assertEquals(19, countDistinctPathsThatVisitSmallCavesAtMostOnce(map))
  }

  @Test
  fun `counts paths that use small caves only once - 3`() {
    val map = parseInput(
      listOf(
        "fs-end",
        "he-DX",
        "fs-he",
        "start-DX",
        "pj-DX",
        "end-zg",
        "zg-sl",
        "zg-pj",
        "pj-he",
        "RW-he",
        "fs-DX",
        "pj-RW",
        "zg-RW",
        "start-pj",
        "he-WI",
        "zg-he",
        "pj-fs",
        "start-RW",
      )
    )
    assertEquals(226, countDistinctPathsThatVisitSmallCavesAtMostOnce(map))
  }

  @Test
  fun `counts paths that use small caves only once expect one that can be visited twice - 1`() {
    val map = parseInput(
      listOf(
        "start-A",
        "start-b",
        "A-c",
        "A-b",
        "b-d",
        "A-end",
        "b-end",
      )
    )
    assertEquals(36, countDistinctPathsThatVisitSmallCavesAtMostOnceExceptOneRepetition(map))
  }

  @Test
  fun `counts paths that use small caves only once expect one that can be visited twice - 2`() {
    val map = parseInput(
      listOf(
        "dc-end",
        "HN-start",
        "start-kj",
        "dc-start",
        "dc-HN",
        "LN-dc",
        "HN-end",
        "kj-sa",
        "kj-HN",
        "kj-dc",
      )
    )
    assertEquals(19, countDistinctPathsThatVisitSmallCavesAtMostOnce(map))
  }

  @Test
  fun `counts paths that use small caves only once expect one that can be visited twice - 3`() {
    val map = parseInput(
      listOf(
        "fs-end",
        "he-DX",
        "fs-he",
        "start-DX",
        "pj-DX",
        "end-zg",
        "zg-sl",
        "zg-pj",
        "pj-he",
        "RW-he",
        "fs-DX",
        "pj-RW",
        "zg-RW",
        "start-pj",
        "he-WI",
        "zg-he",
        "pj-fs",
        "start-RW",
      )
    )
    assertEquals(226, countDistinctPathsThatVisitSmallCavesAtMostOnce(map))
  }
}