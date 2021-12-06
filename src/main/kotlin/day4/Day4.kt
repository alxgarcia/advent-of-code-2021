package day4

import java.io.File

class BingoBoard(private val numbers: List<List<Int>>) {
  private var visited = MutableList(numbers.size) { MutableList(numbers[0].size) { false } }

  fun visit(number: Int) {
    val traversingCellsIndices = visited.indices.flatMap { r -> visited[0].indices.map { c -> Pair(r, c) } }
    val maybeCell = traversingCellsIndices.firstOrNull { (r, c) -> numbers[r][c] == number }
    if (maybeCell != null) visited[maybeCell.first][maybeCell.second] = true
  }

  val isCompleted: Boolean
    get() = hasRowCompleted || hasColumnCompleted

  private val hasRowCompleted: Boolean
    get() = visited.any { row -> row.all { it /* it == true */ } }

  private val hasColumnCompleted: Boolean
    get() {
      val traversingColumnsIndices = visited[0].indices.map { c -> visited.indices.map { r -> Pair(r, c) } }
      return traversingColumnsIndices.any { column -> column.all { (r, c) -> visited[r][c] } }
    }

  fun sumUnvisited(): Int {
    val traversingCellsIndices = visited.indices.flatMap { r -> visited[0].indices.map { c -> Pair(r, c) } }
    return traversingCellsIndices
      .filterNot { (r, c) -> visited[r][c] }
      .sumOf { (r, c) -> numbers[r][c] }
  }
}

fun findWinnerBoardScore(drawNumbers: List<Int>, boards: List<BingoBoard>): Int {
  drawNumbers.forEach { number ->
    boards.forEach { board ->
      board.visit(number)
      if (board.isCompleted) return number * board.sumUnvisited()
    }
  }
  return 0 // should never reach this point
}

fun findLoserBoardScore(drawNumbers: List<Int>, boards: List<BingoBoard>): Int {
  var mutBoards = boards
  drawNumbers.forEach { number ->
    mutBoards.forEach { board ->
      board.visit(number)
      if (mutBoards.size == 1 && board.isCompleted) return mutBoards.first().sumUnvisited() * number
    }
    mutBoards = mutBoards.filterNot { it.isCompleted }
  }
  return 0 // should never reach this point
}

fun parseBingoData(data: List<String>): Pair<List<Int>, List<BingoBoard>> {
  fun parseLine(line: String) = line.split(" ").filterNot(String::isEmpty).map(String::toInt)
  fun parseBoard(boardRaw: List<String>) = BingoBoard(boardRaw.map(::parseLine))

  val drawNumbers = data.first().split(",").map(String::toInt)
  val boards = data
    .drop(1) // remove draw numbers
    .chunked(6)
    .map { parseBoard(it.drop(1)/* removing separator*/) }
  return Pair(drawNumbers, boards)
}

fun main() {
  File("./input/day4.txt").useLines { lines ->
    val data = lines.toList()
    val (drawNumbers, boards) = parseBingoData(data)

    println("First value: ${findWinnerBoardScore(drawNumbers, boards)}")
    println("Second value: ${findLoserBoardScore(drawNumbers, boards)}")
  }
}