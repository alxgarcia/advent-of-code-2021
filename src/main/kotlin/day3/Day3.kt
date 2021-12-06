package day3

import java.io.File

fun computeGammaAndEpsilon(diagnosticReport: List<String>): Pair<Int, Int> {
  val p = List(diagnosticReport[0].length) { -diagnosticReport.size / 2 }
  val sums = diagnosticReport.fold(p) { acc, s -> s.map { it - '0' }.zip(acc){ a, b -> a + b } }
  val (gammaRaw, epsilonRaw) = sums
    .map { if (it >= 0) 1 else 0 }
    .fold(Pair("", "")) { acc, d -> Pair(acc.first + d, acc.second + (1 - d)) }
  return Pair(gammaRaw.toInt(2), epsilonRaw.toInt(2))
}

fun computeOxygenGenerator(diagnosticReport: List<String>): Int {
  tailrec fun rec(remaining: List<String>, bitPosition: Int): String {
    // assuming input has always a single answer although after processing last bit
    // the remaining values are the same
    return if (remaining.size == 1) remaining.first()
    else {
      val (ones, zeros) = remaining.partition { it[bitPosition] == '1' } // assuming it's either 0 or 1)
      rec(if (ones.size >= zeros.size) ones else zeros, bitPosition + 1)
    }
  }
  return rec(diagnosticReport, 0).toInt(2)
}

fun computeCO2Scrubber(diagnosticReport: List<String>): Int {
  tailrec fun rec(remaining: List<String>, bitPosition: Int): String {
    // assuming input has always a single answer although after processing last bit
    // the remaining values are the same
    return if (remaining.size == 1) remaining.first()
    else {
      val (ones, zeros) = remaining.partition { it[bitPosition] == '1' } // assuming it's either 0 or 1)
      rec(if (ones.size < zeros.size) ones else zeros, bitPosition + 1)
    }
  }
  return rec(diagnosticReport, 0).toInt(2)
}

fun printProduct(pair: Pair<Int, Int>): Int = pair.first * pair.second

fun main() {
  File("./input/day3.txt").useLines { lines ->
    val diagnosticReport = lines.toList()
    println("First value: ${printProduct(computeGammaAndEpsilon(diagnosticReport))}")
    println("Second value: ${computeOxygenGenerator(diagnosticReport) * computeCO2Scrubber(diagnosticReport)}")
  }
}