package day9

import java.io.File


fun parseMap(rows: List<String>): Array<IntArray> =
  rows.map { row -> row.trim().map { it - '0' }.toIntArray() }.toTypedArray()

fun expandAdjacent(row: Int, column: Int, map: Array<IntArray>): List<Pair<Int, Int>> =
  listOf(0 to -1, -1 to 0, 0 to 1, 1 to 0)
    .map { (r, c) -> (r + row) to (c + column) }
    .filterNot { (r, c) -> r < 0 || c < 0 || r >= map.size || c >= map[0].size }

fun findLocalMinimums(map: Array<IntArray>): List<Pair<Int, Int>> {
  fun isLocalMinimum(row: Int, column: Int): Boolean {
    val adjacent = expandAdjacent(row, column, map)
    val current = map[row][column]
    return adjacent.all { (r, c) -> map[r][c] > current }
  }

  val rows = map.indices
  val columns = map[0].indices

  val traverseMapIndices = rows.flatMap { r -> columns.map { c -> r to c } }
  return traverseMapIndices.filter { (r, c) -> isLocalMinimum(r, c) }
}

fun sumOfLocalMinimums(map: Array<IntArray>): Int =
  findLocalMinimums(map).sumOf { (r, c) -> map[r][c] + 1 }

fun computeBasinSize(row: Int, column: Int, map: Array<IntArray>): Int {
  val visited = mutableSetOf<Pair<Int, Int>>()
  val remaining = ArrayDeque(listOf(row to column))
  var size = 0
  do {
    val current = remaining.removeFirst()
    val currentValue = map[current.first][current.second]
    if (currentValue == 9 || visited.contains(current)) continue

    val nonVisitedAdjacent = expandAdjacent(current.first, current.second, map).filterNot { visited.contains(it) }
    val belongsToBasin = nonVisitedAdjacent.all { (r, c) -> map[r][c] >= currentValue }
    if (belongsToBasin) {
      size += 1
      visited.add(current)
      remaining.addAll(nonVisitedAdjacent)
    }
  } while (!remaining.isEmpty())
  return size
}

fun productOfSizeThreeLargestBasins(map: Array<IntArray>): Int =
  findLocalMinimums(map)
    .map { (r, c) -> computeBasinSize(r, c, map) }
    .sorted()
    .takeLast(3)
    .reduce(Int::times)

fun main() {
  File("./input/day9.txt").useLines { lines ->
    val map = parseMap(lines.toList())
    println(findLocalMinimums(map))
    println(productOfSizeThreeLargestBasins(map))
  }
}
