package day22

import java.io.File

private const val OFF = false
private const val ON = true

typealias Cube = Triple<Int, Int, Int>

data class RebootStep(val x: IntRange, val y: IntRange, val z: IntRange, val state: Boolean)

data class LitRegion(val x: IntRange, val y: IntRange, val z: IntRange) {
  private fun isEmptyRegion(): Boolean = x.isEmpty() || y.isEmpty() || z.isEmpty()
  private fun overlapsWith(other: LitRegion): Boolean = TODO()
  fun subtract(other: LitRegion): List<LitRegion> {
    if (other.isEmptyRegion() || !overlapsWith(other)) return listOf(this)
    else {
      TODO()
    }
  }
}


fun parseInput(lines: List<String>): List<RebootStep> {
  fun parseRange(range: String): IntRange {
    val (n1, n2) = range.split("..").map(String::toInt)
    return n1..n2
  }

  fun extractRanges(ranges: String): Triple<IntRange, IntRange, IntRange> {
    val (x, y, z) = ranges.split(",").map { parseRange(it.drop(2)) }
    return Triple(x, y, z)
  }
  return lines.map { line ->
    when {
      line.startsWith("on") -> {
        val (x, y, z) = extractRanges(line.removePrefix("on "))
        RebootStep(x, y, z, ON)
      }
      line.startsWith("off") -> {
        val (x, y, z) = extractRanges(line.removePrefix("off "))
        RebootStep(x, y, z, OFF)
      }
      else -> TODO()
    }
  }
}

fun countCubesOnRestricted(steps: List<RebootStep>): Int {
  fun isOutOfRange(step: RebootStep): Boolean =
    listOf(step.x, step.y, step.z).any { it.first < -50 || it.last > 50 }

  val cubes = Array(101) { Array(101) { Array(101) { OFF } } }
  for (step in steps.filterNot(::isOutOfRange)) {
    step.x.forEach { x ->
      step.y.forEach { y ->
        step.z.forEach { z ->
          cubes[x + 50][y + 50][z + 50] = step.state
        }
      }
    }
  }
  return cubes.sumOf { it.sumOf { it.count { it == ON } } }
}

fun countCubesUnbounded(steps: List<RebootStep>): Long {
  val cubesOn = HashSet<Cube>()
  for (step in steps) {
    println(step)
    val cubes = step.x.flatMap { x -> step.y.flatMap { y -> step.z.map { z -> Cube(x, y, z) } } }
    if (step.state == ON) cubesOn.addAll(cubes)
    else cubesOn.removeAll(cubes)
  }
  return cubesOn.sumOf { 1L }
}


fun main() {
  File("./input/day22.txt").useLines { lines ->
    val rebootSteps = parseInput(lines.toList())
    println(countCubesOnRestricted(rebootSteps))
  }
}