package day1

import java.io.File

fun countIncreasingDeltas(measurements: List<Int>): Int =
  measurements.zipWithNext().count { (m1, m2) -> m1 < m2 }

/*
* 1) -> a
* 2) -> a b
* 3) -> a b c
* 4) -> b c d
*
* Simply checks a < d as b and c are included in both sums
*/
fun countIncreasingDeltas2(measurements: List<Int>): Int =
  measurements.zip(measurements.drop(3)).count { (m1, m2) -> m1 < m2 }

fun main() {
  File("./input/day1.txt").useLines { lines ->
    val measurements = lines.map { it.toInt() }.toList()
    println("First part: ${countIncreasingDeltas(measurements)}")
    println("Second part: ${countIncreasingDeltas2(measurements)}")
  }
}