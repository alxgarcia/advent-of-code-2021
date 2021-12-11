package day10

import java.io.File
import java.util.LinkedList


// Poor man's Either -> Pair<A?, B?>
fun findMissingSequenceOrOffendingChar(line: String): Pair<Char?, String?> {
  val missingSequence = LinkedList<Char>()
  line.forEach { c ->
    when (c) {
      '(' -> missingSequence.addFirst(')')
      '[' -> missingSequence.addFirst(']')
      '{' -> missingSequence.addFirst('}')
      '<' -> missingSequence.addFirst('>')
      ')', ']', '}', '>' -> if (missingSequence.firstOrNull() == c) missingSequence.removeFirst() else return (c to null)
    }
  }
  return null to missingSequence.joinToString("")
}

private val syntaxCheckerScoreMap = mutableMapOf(
  ')' to 3,
  ']' to 57,
  '}' to 1197,
  '>' to 25137,
)

fun sumScoreOfCorruptedLines(lines: List<String>): Int =
  lines
    .map { findMissingSequenceOrOffendingChar(it).first }
    .sumOf { maybeChar -> maybeChar?.let { c -> syntaxCheckerScoreMap[c] } ?: 0 }


private val autocompleteScoreMap = mapOf(
  ')' to 1,
  ']' to 2,
  '}' to 3,
  '>' to 4,
)

fun computeMiddleScoreOfIncompleteLinesMissingChars(lines: List<String>): Long {
  fun computeScoreOfAutocomplete(sequence: String): Long =
    sequence.map { autocompleteScoreMap[it]!! }.fold(0L) { acc, score -> (acc * 5L) + score }

  val scores =
    lines
      .map { findMissingSequenceOrOffendingChar(it).second }
      .filterNot { it.isNullOrBlank() }
      .map { computeScoreOfAutocomplete(it!!) }
      .sorted()

  return scores[scores.size / 2]
}

fun main() {
  File("./input/day10.txt").useLines { lines ->
    val input = lines.toList()
    println(sumScoreOfCorruptedLines(input))
    println(computeMiddleScoreOfIncompleteLinesMissingChars(input))
  }
}
