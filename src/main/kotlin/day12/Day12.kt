package day12

import java.io.File


fun parseInput(lines: List<String>): Map<String, List<String>> =
  lines.flatMap {
    val (cave1, cave2) = it.split("-")
    listOf(cave1 to cave2, cave2 to cave1)
  }.groupBy({ it.first }, { it.second })

private fun String.isCaveSmall(): Boolean = this.first().isLowerCase()

fun countDistinctPathsThatVisitSmallCavesAtMostOnce(map: Map<String, List<String>>): Int {
  fun rec(cave: String, visited: Set<String>): Int =
    when {
      cave == "end" -> 1
      visited.contains(cave) -> 0
      else -> {
        map[cave]?.sumOf { rec(it, if (cave.isCaveSmall()) visited + cave else visited) } ?: 0
      }
    }
  return rec("start", setOf())
}

fun countDistinctPathsThatVisitSmallCavesAtMostOnceExceptOneRepetition(map: Map<String, List<String>>): Int {
  fun rec(cave: String, smallCavesVisited: Set<String>, repetitionUsed: Boolean): Int {
    return when {
      cave == "end" -> 1
      smallCavesVisited.contains(cave) ->
        if (repetitionUsed || cave == "start") 0
        else map[cave]?.sumOf { rec(it, smallCavesVisited, true) } ?: 0
      else -> {
        val next = if (cave.isCaveSmall()) smallCavesVisited + cave else smallCavesVisited
        map[cave]?.sumOf { rec(it, next, repetitionUsed) } ?: 0
      }
    }
  }
  return rec("start", setOf(), false)
}

fun main() {
  File("./input/day12.txt").useLines { lines ->
    val map = parseInput(lines.toList())
    println(countDistinctPathsThatVisitSmallCavesAtMostOnce(map))
    println(countDistinctPathsThatVisitSmallCavesAtMostOnceExceptOneRepetition(map))
  }
}
