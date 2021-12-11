package day8

import java.io.File

/*
* An entry has the following format: <representation of the ten digits> | <display output>
* i.e:
* acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
*/
fun parseOnlyDigitsOutput(entries: List<String>): List<List<String>> =
  entries.map { entry -> entry.split(" | ")[1].split(" ") }

fun countOneFourSevenAndEights(listOfDisplayDigitRows: List<List<String>>): Int {
  val listOfSizes = listOf(2, 4, 3, 7) // The amount of signals that 1, 4, 7 and 8 require respectively
  return listOfDisplayDigitRows.sumOf { digitsRow -> digitsRow.count { digit -> listOfSizes.contains(digit.length) } }
}
/*
* Digits representation
*
*     0:      1:      2:      3:      4:
*    aaaa    ....    aaaa    aaaa    ....
*   b    c  .    c  .    c  .    c  b    c
*   b    c  .    c  .    c  .    c  b    c
*    ....    ....    dddd    dddd    dddd
*   e    f  .    f  e    .  .    f  .    f
*   e    f  .    f  e    .  .    f  .    f
*    gggg    ....    gggg    gggg    ....
*
*     5:      6:      7:      8:      9:
*    aaaa    aaaa    aaaa    aaaa    aaaa
*   b    .  b    .  .    c  b    c  b    c
*   b    .  b    .  .    c  b    c  b    c
*    dddd    dddd    ....    dddd    dddd
*   .    f  e    f  .    f  e    f  .    f
*   .    f  e    f  .    f  e    f  .    f
*    gggg    gggg    ....    gggg    gggg
*/
private val digitMap = mapOf(
  "abcefg" to 0,
  "cf" to 1,
  "acdeg" to 2,
  "acdfg" to 3,
  "bcdf" to 4,
  "abdfg" to 5,
  "abdefg" to 6,
  "acf" to 7,
  "abcdefg" to 8,
  "abcdfg" to 9,
)

/*
* The input contains the representation of all digits.
* With that information and the `digitMap` above we can already infer a few things:
* - Only edge 'b' appears 6 times
* - Only edge 'e' appears 4 times
* - Only edge 'f' appears 9 times
* - Edges 'a' and 'c' appear 8 times
* - Edges 'd' and 'g' appear 7 times
*
* Also comparing the edges of numbers we can disambiguate edges in the last two statements:
* - 7.edges - 4.edges = 'a'
* - 4.edges - 7.edges = 'b' and 'd'
*
* Although the last subtraction will produce two edges, we've already inferred 'b' so we can discard it in favour of 'd'
*/
fun computeEquivalenceMap(digitsSignals: String): Map<Char, Char> {
  val translations = mutableMapOf<Char, Char>()

  // Get some equivalences due to unique number of occurrences
  val occurrences = digitsSignals.replace(" ", "").groupingBy { it }.eachCount()
  occurrences.forEach { (c, n) ->
    if (n == 4) translations[c] = 'e'
    if (n == 6) translations[c] = 'b'
    if (n == 9) translations[c] = 'f'
  }

  // Get the rest by comparing numbers with subtle differences
  val signals = digitsSignals.split(" ").groupBy { it.length }
  val four = signals[4]!!.first().toSet() // 4 the only digit that requires four signals to be represented
  val seven = signals[3]!!.first().toSet()  // 7 the only digit that requires three signals to be represented

  // 7 - 4 will output edge 'a'
  val a = (seven - four).first()
  translations[a] = 'a'

  // 'c' is the other edge with 8 occurrences that is not 'a'
  val c = occurrences.entries.first { (c, n) -> n == 8 && c != a }.key
  translations[c] = 'c'

  // 4 - 7 will generate edges 'b' and 'd' but 'b' is already in the translations map
  val d = (four - seven).first { c -> !translations.contains(c) }
  translations[d] = 'd'

  // 'g' is the other edge with 7 occurrences that is not 'd'
  val g = occurrences.entries.first { (c, n) -> n == 7 && c != d }.key
  translations[g] = 'g'

  return translations
}

fun decipherSignal(entry: String): Int {
  val (signals, display) = entry.split(" | ")
  val equivalenceMap = computeEquivalenceMap(signals)
  val outputDigits = display
    .split(" ")
    .map { digit ->
      val translation = digit.map(equivalenceMap::get).sortedBy { it }.joinToString("")
      digitMap[translation]
    }

  return outputDigits.joinToString("").toInt()
}

fun main() {
  File("./input/day8.txt").useLines { lines ->
    val entries = lines.toList()
    val listOfDisplayDigitRows = parseOnlyDigitsOutput(entries)
    println("First part: ${countOneFourSevenAndEights(listOfDisplayDigitRows)}")
    println("First part: ${entries.sumOf { decipherSignal(it) }}")
  }
}
