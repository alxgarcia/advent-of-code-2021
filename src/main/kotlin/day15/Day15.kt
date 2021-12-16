package day15

import java.io.File
import java.util.PriorityQueue

// Syntax sugar
typealias Matrix<T> = Array<Array<T>>
typealias RiskFn = (Pair<Int, Int>) -> Int

private fun <T> Matrix<T>.get(pair: Pair<Int, Int>) = this[pair.first][pair.second]
private fun <T> Matrix<T>.put(pair: Pair<Int, Int>, element: T) {
  this[pair.first][pair.second] = element
}

fun parseInput(lines: List<String>): Matrix<Int> =
  lines
    .map { it.map(Char::digitToInt).toTypedArray() }
    .toTypedArray()

private fun Pair<Int, Int>.withinBoundaries(limit: Int) =
  (0 until limit).let { range -> range.contains(this.first) && range.contains(this.second) }

// I decided to navigate from destination to start: (N, N) to (0, 0)
// Let's try to go up and left first even though there is a priority queue that
// will ultimately decide the order
private fun Pair<Int, Int>.adjacent(boundaries: Int) =
  listOf(-1 to 0, 0 to -1, 0 to 1, 1 to 0) // up, left, right, down
    .map { (first, second) -> (first + this.first) to (second + this.second) }
    .filter { it.withinBoundaries(boundaries) }


private fun solve(dimension: Int, riskFn: RiskFn): Int {
  val minPathRiskTabulation = Array(dimension) { Array(dimension) { Int.MAX_VALUE } }
  val toVisit = PriorityQueue<Pair<Pair<Int, Int>, Int>> { (_, risk1), (_, risk2) -> risk1.compareTo(risk2) }

  val start = 0 to 0
  val destination = (dimension - 1) to (dimension - 1)
  toVisit.add(destination to 0)
  do {
    val (current, accumulatedRisk) = toVisit.poll()
    if (current == start) return accumulatedRisk
    val pathRisk = riskFn(current) + accumulatedRisk
    val minPathRisk = minPathRiskTabulation.get(current)
    if (pathRisk < minPathRisk) {
      minPathRiskTabulation.put(current, pathRisk)
      toVisit.addAll(current.adjacent(dimension).associateWith { pathRisk }.toList())
    }
  } while (true)
}

fun solve1(riskMatrix: Matrix<Int>): Int = solve(riskMatrix.size, riskMatrix::get)

fun riskFn2(riskMatrix: Matrix<Int>): RiskFn = { (r, c) ->
  val additionalRisk = (r / riskMatrix.size) + (c / riskMatrix.size)
  val risk = riskMatrix[r % riskMatrix.size][c % riskMatrix.size] + additionalRisk
  if (risk > 9) risk - 9 else risk // only 1..9 (10 resets to 1)
}

fun solve2(riskMatrix: Matrix<Int>): Int = solve(riskMatrix.size * 5, riskFn2(riskMatrix))

fun main() {
  File("./input/day15.txt").useLines { lines ->
    val riskMatrix = parseInput(lines.toList())
    println(solve1(riskMatrix))
    println(solve2(riskMatrix))
  }
}