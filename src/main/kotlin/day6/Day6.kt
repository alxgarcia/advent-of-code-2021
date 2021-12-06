package day6

import java.io.File


fun parseInput(line: String) = line.split(",").map { it.toInt() }

data class LanternFish(val daysToEngender: Int) {
  fun ageOneDay(): List<LanternFish> =
    if (daysToEngender == 0) listOf(LanternFish(6), LanternFish(8))
    else listOf(LanternFish(daysToEngender - 1))
}

fun naivelyCountLanternfishPopulationAfterNDays(listOfDays: List<Int>, days: Int): Int {
  tailrec fun loop(lanternFish: List<LanternFish>, remainingDays: Int): List<LanternFish> =
    if (remainingDays == 0) lanternFish
    else loop(lanternFish.flatMap { it.ageOneDay() }, remainingDays - 1)

  return loop(listOfDays.map { LanternFish(it) }, days).count()
}

fun countLanternfishPopulationAfterNDays(listOfDays: List<Int>, days: Int): Long {
  // cluster population by remaining days to give birth; 9 days for the first time, then 7 days
  val clusters = listOfDays.groupingBy { it }.eachCount()
  val population = Array(9) { clusters.getOrDefault(it, 0).toLong() }

  repeat(days) {
    val births = population[0]
    (1..8).forEach { i -> population[i - 1] = population[i] } // reduce days to give birth of population
    population[6] += births
    population[8] = births
  }

  return population.sum()
}

fun main() {
  File("./input/day6.txt").useLines { lines ->
    val lanternFish = parseInput(lines.first())
    println("First part: ${countLanternfishPopulationAfterNDays(lanternFish, 80)}")
    println("Second part: ${countLanternfishPopulationAfterNDays(lanternFish, 256)}")
  }
}

