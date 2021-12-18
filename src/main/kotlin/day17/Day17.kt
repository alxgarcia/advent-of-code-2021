package day17

import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.math.sqrt

fun parseInput(line: String): Pair<IntRange, IntRange> {
  val rawXRange = line.split("x=")[1].split(",")[0]
  val rawYRange = line.split("y=")[1]
  val (minX, maxX) = rawXRange.split("..").map(String::toInt)
  val (minY, maxY) = rawYRange.split("..").map(String::toInt)
  return minX..maxX to minY..maxY
}

private fun Double.hasDecimals(): Boolean = this % 1.0 != 0.0

/*
* Solving t^2 - (2*C + 1)*t + 2*Y = 0; where
*  - C is the vertical velocity
*  - Y is any of the valid positions in rangeY
*
* There's probably a better way, but I'm only OK-ish with Maths
*
* Returns a map time => all vertical velocities that will make it to the target area
*/
fun samplingInitialVy(rangeY: IntRange): Map<Int, List<Int>> {
  val minTheoreticalVelocityY = rangeY.minOf { it } // going straight from S0 to SX in one unit of time
  /*
   With the current formula, given an initial Vy, when time = 2*Vy + 1 the vertical space travelled is 0
     - If the target is below the Y axis then in the next 'moment' Vy(t) will be greater than the greatest distance
       from the Y axis which will cause the probe to miss the area
     - If the target is above the Y axis then we are in the same case but one step before reaching the 0 sum. The limit
       in that case will be the greatest distance to the Y axis as any greater speed will be short and then miss the target
   */
  val maxTheoreticalVelocity = rangeY.maxOf { it.absoluteValue } // the furthest distance from the Y axis
  var candidate = minTheoreticalVelocityY // starting with the min velocity possible
  val solutionsByTime = mutableListOf<Pair<Int, Int>>()
  do {
    val b = -((2 * candidate) + 1)
    val squaredB = 1.0 * b * b
    val solutions = mutableListOf<Int>()
    for (y in rangeY) {
      val ac4 = 4 * 2 * y
      if (ac4 > squaredB) continue
      val root = sqrt(squaredB - ac4)
      val localSolutions = listOf(
        (-b + root) / 2,
        (-b - root) / 2
      ).filterNot { it < 0 || it.hasDecimals() }.map { it.toInt() }
      // t can only be a positive integer because:
      //  [a] can't travel back in time
      //  [b] the granularity of the problem is in units of time
      solutions.addAll(localSolutions)
    }
    solutionsByTime.addAll(solutions.associateWith { candidate }.toList())
    candidate += 1;
  } while (candidate <= maxTheoreticalVelocity)
  return solutionsByTime.groupBy { it.first }.mapValues { (_, v) -> v.map { it.second } }
}

/*
* Values for initial horizontal velocity Vx, aka Vx(0), are determined by the elapsed time, given that:
*  - Vx(t) = min(0, Vx(t-1) - 1)
*  - After time t,
*       Sx(t) = Vx(0) * min(Vx(0), t) - [1, min(Vx(0), t)) # IMPORTANT, the range is end exclusive
*
* Sx(t) is the distance traveled at the given initial velocity during time t. For the velocity to be
*  considered valid, Sx(t) must belong to rangeX
*/
private fun samplingInitialVxForGivenTime(time: Int, rangeX: IntRange): List<Int> {
  val candidates = (rangeX.first / time)..rangeX.last
  return candidates.filter { vx ->
    val sx = vx * min(vx, time) - (1 until min(vx, time)).sum()
    sx in rangeX
  }
}

fun findAllPossibleSolutions(rangeX: IntRange, rangeY: IntRange): List<Pair<Int, Int>> =
  samplingInitialVy(rangeY).flatMap { (time, vys) ->
    val solutionSpaceForX = samplingInitialVxForGivenTime(time, rangeX)
    solutionSpaceForX.flatMap { vx -> vys.map { vy -> vx to vy } }
  }.distinct()

fun findMaxInitialVerticalVelocity(rangeY: IntRange) =
  samplingInitialVy(rangeY).mapValues { (_, vs) -> vs.maxOf { it } }.maxByOrNull { it.value }!!

fun computeMaxHeight(time: Int, initialVerticalVelocity: Int): Int = (initialVerticalVelocity downTo 0).take(time).sum()

fun main() {
  File("./input/day17.txt").useLines { lines ->
    val areaDefinition = lines.first()
    val (rangeX, rangeY) = parseInput(areaDefinition)
    val (time, maxInitialVerticalSpeed) = findMaxInitialVerticalVelocity(rangeY)
    println(computeMaxHeight(time, maxInitialVerticalSpeed))

    println(findAllPossibleSolutions(rangeX, rangeY).size)
  }
}
