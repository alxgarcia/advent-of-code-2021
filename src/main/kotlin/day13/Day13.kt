package day13

import java.io.File

/* Syntax sugar */
typealias Dot = Pair<Int, Int>
typealias PaperFold = Pair<Char, Int>

private val Dot.x: Int
  get() = this.first

private val Dot.y: Int
  get() = this.second
/* end */

fun parseInput(lines: List<String>): Pair<List<Dot>, List<PaperFold>> {
  val dots =
    lines
      .takeWhile { it.isNotEmpty() && it.first().isDigit() }
      .map {
        val (x, y) = it.split(",").map(String::toInt)
        x to y
      }

  fun extractAxisAndValue(string: String): Pair<Char, Int> {
    val (sentence, value) = string.split("=")
    return (sentence.last() to value.toInt())
  }

  val paperFolds = lines
    .drop(dots.size + 1) // removing also the empty line separator
    .map(::extractAxisAndValue)

  return Pair(dots, paperFolds)
}

fun fold(dots: List<Dot>, paperFold: PaperFold): List<Dot> {
  val (axis, value) = paperFold
  return when (axis) {
    'x' -> {
      val (base, foldingSide) = dots
        .filterNot { it.x == value }
        .partition { it.x < value }

      (base + foldingSide.map { (value - (it.x - value)) to it.y }).distinct()
    }
    'y' -> {
      val (base, foldingSide) = dots
        .filterNot { it.y == value }
        .partition { it.y < value }

      (base + foldingSide.map { it.x to (value - (it.y - value)) }).distinct()
    }
    else -> TODO("Not supported axis :)")
  }
}

// Actually not needed
fun countVisibleDotsAfterFolding(dots: List<Dot>, paperFolds: List<PaperFold>): Int =
  paperFolds.fold(dots, ::fold).size

fun countVisibleDotsAfterFirstFolding(dots: List<Dot>, paperFolds: List<PaperFold>): Int =
  fold(dots, paperFolds.first()).size

fun printAfterAllFolds(dots: List<Dot>, paperFolds: List<PaperFold>) {
  fun print(dots: List<Dot>) {
    val rowMax = dots.maxOf { it.y }
    val columnMax = dots.maxOf { it.x }

    (0..rowMax).forEach { y ->
      (0..columnMax).forEach { x ->
        if (dots.contains(x to y)) print('#')
        else print('.')
      }
      println()
    }
  }

  print(paperFolds.fold(dots, ::fold))
}


fun main() {
  File("./input/day13.txt").useLines { lines ->
    val (dots, folds) = parseInput(lines.toList())
    println(countVisibleDotsAfterFirstFolding(dots, folds))
    printAfterAllFolds(dots, folds)
  }
}

// ARHZPCUH