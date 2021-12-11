package day5

import java.io.File

typealias Coordinates = Pair<Int, Int>
typealias Matrix = Map<Coordinates, Int>

private val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()

fun parseLine(line: String): List<Int> =
  regex.matchEntire(line)!!
    .groupValues.drop(1) // dropping first item because it's the whole line
    .map(String::toInt)

private fun expandRange(start: Int, end: Int) = if (start <= end) start.rangeTo(end) else start.downTo(end)

// required for part 1
fun expandSegmentRangeWithoutDiagonals(x1: Int, y1: Int, x2: Int, y2: Int): Iterable<Coordinates> = when {
  x1 == x2 -> expandRange(y1, y2).map { x1 to it }
  y1 == y2 -> expandRange(x1, x2).map { it to y1 }
  else -> emptyList() // ignore
}

fun expandSegmentRange(x1: Int, y1: Int, x2: Int, y2: Int): Iterable<Coordinates> = when {
  x1 == x2 -> expandRange(y1, y2).map { x1 to it }
  y1 == y2 -> expandRange(x1, x2).map { it to y1 }
  else -> expandRange(x1, x2).zip(expandRange(y1, y2)) // diagonal, update both coordinates
}

fun buildMatrix(segments: List<String>): Matrix =
  segments
    .map { segment -> parseLine(segment) }
    .flatMap { (x1, y1, x2, y2) -> expandSegmentRange(x1, y1, x2, y2) }
    .groupingBy { it }.eachCount()

fun countCloudSegmentOverlappingPoints(matrix: Matrix): Int = matrix.values.count { it > 1 }

fun main() {
  File("./input/day5.txt").useLines { lines ->
    val matrix = buildMatrix(lines.toList())
    println(countCloudSegmentOverlappingPoints(matrix))
  }
}
