package day7

import java.io.File
import kotlin.math.abs

fun parseInput(input: String) = input.split(",").groupingBy(String::toInt).eachCount()

private fun minFuelSpentToAlignedPosition(data: Map<Int, Int>, costFn: (Int, Int) -> Int): Int {
  val min = data.keys.minOf { it }
  val max = data.keys.maxOf { it }

  return (min..max)
    .map { destination -> data.map { (currentPosition, crabs) -> costFn(destination, currentPosition) * crabs } }
    .minOf { it.sum() }
}

fun minFuelSpentToAlignedPosition(data: Map<Int, Int>): Int =
  minFuelSpentToAlignedPosition(data){ destination, currentPosition -> abs(destination - currentPosition)}

fun minFuelSpentToAlignedPositionPart2(data: Map<Int, Int>): Int =
  minFuelSpentToAlignedPosition(data){ destination: Int, currentPosition: Int ->
    val steps = abs(currentPosition - destination)
    ((steps * (steps + 1)) / 2)
  }

fun main() {
  File("./input/day7.txt").useLines { lines ->
    val crabsMap = parseInput(lines.first())
    println("First part: ${minFuelSpentToAlignedPosition(crabsMap)}")
    println("Second part: ${minFuelSpentToAlignedPositionPart2(crabsMap)}")
  }
}

