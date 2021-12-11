package day11

import java.io.File
import java.util.LinkedList

fun parseMap(rows: List<String>): Array<IntArray> =
  rows.map { row -> row.trim().map { it - '0' }.toIntArray() }.toTypedArray()

private fun expandAdjacentWithDiagonals(row: Int, column: Int, map: Array<IntArray>): List<Pair<Int, Int>> =
  (-1..1).flatMap { r -> (-1..1).map { c -> (r + row) to (c + column) } }
    .filterNot { (r, c) -> r < 0 || c < 0 || r >= map.size || c >= map[0].size || (r == row && c == column) }

fun simulateStep(octopusMap: Array<IntArray>): Int {
  val traverseIndices = octopusMap.flatMapIndexed { index, row -> row.indices.map { index to it } }
  val cellsToVisit = LinkedList(traverseIndices)
  val visited = mutableSetOf<Pair<Int, Int>>()
  var flashes = 0

  while (cellsToVisit.isNotEmpty()) {
    val current = cellsToVisit.removeFirst()
    val (x, y) = current
    when (octopusMap[x][y]) {
      9 -> {
        octopusMap[x][y] = 0
        visited.add(current)
        flashes += 1
        cellsToVisit.addAll(expandAdjacentWithDiagonals(x, y, octopusMap))
      }
      0 -> if (!visited.contains(current)) octopusMap[x][y] += 1 // Can only flash once
      in (1..8) -> octopusMap[x][y] += 1 // 0 is intentionally ignored
    }
  }
  return flashes
}

fun simulateSteps(steps: Int, octopusMap: Array<IntArray>): Int =
  (1..steps).fold(0) { acc, _ -> acc + simulateStep(octopusMap) }

fun countStepsUntilAllOctopusesSynchronize(octopusMap: Array<IntArray>): Int {
  fun allFlashed(map: Array<IntArray>): Boolean =
    map.all { energies -> energies.all { it == 0 } }

  return generateSequence(1) { it + 1 }.first {
    simulateStep(octopusMap)
    allFlashed(octopusMap)
  }.or(0)
}

fun main() {
  File("./input/day11.txt").useLines { lines ->
    val input = lines.toList()
    println(simulateSteps(100, parseMap(input)))
    println(countStepsUntilAllOctopusesSynchronize(parseMap(input)))
  }
}
