package day19

import java.io.File
import kotlin.math.absoluteValue

typealias Position = Triple<Int, Int, Int>
typealias Transformation = (Triple<Int, Int, Int>) -> Triple<Int, Int, Int>

private operator fun Position.plus(offset: Position): Position =
  Position(first + offset.first, second + offset.second, third + offset.third)

private operator fun Position.minus(other: Position) =
  Position(first - other.first, second - other.second, third - other.third)

data class Scanner(val beacons: List<Position>) {
  val distanceMatrix: Array<Array<Position>>
    get() = beacons.map { position -> beacons.map { (position - it) }.toTypedArray() }.toTypedArray()

  fun transform(t: Transformation): Scanner = this.copy(beacons = beacons.map(t))
}

fun parseInput(lines: List<String>): List<Scanner> {
  fun parseBeacon(line: String): Position {
    val (x, y, z) = line.split(",").map(String::toInt)
    return Position(x, y, z)
  }

  val scanners = mutableListOf<Scanner>()
  val beacons = mutableListOf<Position>()
  for (line in lines) {
    when {
      line.isBlank() -> scanners.add(Scanner(beacons.toList()))
      line.startsWith("--- ") -> beacons.clear()
      else -> beacons.add(parseBeacon(line))
    }
  }
  if (beacons.isNotEmpty()) scanners.add(Scanner(beacons.toList()))
  return scanners
}

/*
  Orientations over Z
   x  y  z
   y -x  z
  -x -y  z
  -y  x  z

  Orientations over -Z
   x -y -z
  -y -x -z
  -x  y -z
   y  x -z

  Orientations over X
  -z  y  x
  -y -z  x
   z -y  x
   y  z  x

  Orientations over -X
   z  y -x
   y -z -x
  -z -y -x
  -y  z -x

  Orientations over Y
   x -z  y
  -z -x  y
  -x  z  y
   z  x  y

  Orientations over -Y
   x  z -y
   z -x -y
  -x -z -y
  -z  x -y
*/
val orientations = listOf<Transformation>(
  // Orientations over  Z
  { (x, y, z) -> Triple(x, y, z) },
  { (x, y, z) -> Triple(y, -x, z) },
  { (x, y, z) -> Triple(-x, -y, z) },
  { (x, y, z) -> Triple(-y, x, z) },
  // Orientations over -Z
  { (x, y, z) -> Triple(x, -y, -z) },
  { (x, y, z) -> Triple(-y, -x, -z) },
  { (x, y, z) -> Triple(-x, y, -z) },
  { (x, y, z) -> Triple(y, x, -z) },
  // Orientations over  X
  { (x, y, z) -> Triple(-z, y, x) },
  { (x, y, z) -> Triple(-y, -z, x) },
  { (x, y, z) -> Triple(z, -y, x) },
  { (x, y, z) -> Triple(y, z, x) },
  // Orientations over -X
  { (x, y, z) -> Triple(z, y, -x) },
  { (x, y, z) -> Triple(y, -z, -x) },
  { (x, y, z) -> Triple(-z, -y, -x) },
  { (x, y, z) -> Triple(-y, z, -x) },
  // Orientations over  Y
  { (x, y, z) -> Triple(x, -z, y) },
  { (x, y, z) -> Triple(-z, -x, y) },
  { (x, y, z) -> Triple(-x, z, y) },
  { (x, y, z) -> Triple(z, x, y) },
  // Orientations over -Y
  { (x, y, z) -> Triple(x, z, -y) },
  { (x, y, z) -> Triple(z, -x, -y) },
  { (x, y, z) -> Triple(-x, -z, -y) },
  { (x, y, z) -> Triple(-z, x, -y) },
)

/*
  * For scanner s1, for every beacon compute the distance with the others
  * For scanner s2, apply one of the rotations and compute the distance matrix too
  * For each pair of matrix, check if for one row in s1 there's a matching row in s2 with at least minOverlap matching distances
  * If so, we got a pair of overlapping scanners given a certain orientation.
  * Subtracting both positions will return the distance between the position of the two scanners
  */
fun determineScannerLocation(s1: Scanner, s2: Scanner, minOverlaps: Int): Pair<Position, Scanner>? {
  val matrix1 = s1.distanceMatrix
  for (orientation in orientations) {
    val matrix2 = s2.transform(orientation).distanceMatrix
    val matchingBeacons = matrix1.mapIndexed { index, beaconDistances ->
      index to matrix2.indexOfFirst { distances -> beaconDistances.count { distances.contains(it) } >= minOverlaps }
    }.filterNot { it.second == -1 }
    if (matchingBeacons.size < minOverlaps) continue
    val (b1, b2) = matchingBeacons.first()
    val s2Center = s1.beacons[b1] - orientation(s2.beacons[b2]) // compute the discrepancy between the two positions of the same beacon
    val transformation = { position: Position -> orientation(position) + s2Center } // transforming to the PoV of scanner s1
    return s2Center to s2.transform(transformation)
  }
  return null
}

/*
* Returns the positions of the scanners and the position of the beacons from the PoV of the first scanner
*/
fun locateScannersAndBeacons(scanners: List<Scanner>, minOverlaps: Int): Pair<Set<Position>, Set<Position>> {
  val scannerPositions = mutableSetOf(Position(0, 0, 0)) // assuming the first scanner is at (0, 0, 0)
  val beaconPositions = scanners.first().beacons.toMutableSet()

  val recognisedScanners = mutableListOf(scanners.first())
  val unrecognisedScanners = scanners.drop(1).toMutableList()

  while (unrecognisedScanners.isNotEmpty()) {
    val scanner = recognisedScanners.removeFirst()

    val scannersToCheck = unrecognisedScanners.listIterator()
    while (scannersToCheck.hasNext()) {
      val unrecognised = scannersToCheck.next()
      determineScannerLocation(scanner, unrecognised, minOverlaps)?.let { (position, transformedScanner) ->
        scannersToCheck.remove()
        recognisedScanners.add(transformedScanner)
        scannerPositions.add(position)
        beaconPositions.addAll(transformedScanner.beacons)
      }
    }
  }
  return scannerPositions to beaconPositions
}

private fun euclideanDistance(one: Position, other: Position): Int =
  (one.first - other.first).absoluteValue +
      (one.second - other.second).absoluteValue +
      (one.third - other.third).absoluteValue

private fun findMaxEuclideanDistance(positions: Iterable<Position>): Int =
  positions.maxOf { p1 -> positions.maxOf { p2 -> euclideanDistance(p1, p2) } }

fun findMaxEuclideanDistance(scanners: List<Scanner>, minOverlaps: Int): Int {
  val (centers, _) = locateScannersAndBeacons(scanners, minOverlaps)
  return findMaxEuclideanDistance(centers)
}

fun main() {
  File("./input/day19.txt").useLines { lines ->
    val scanners = parseInput(lines.toList())

    val (scannerPositions, beaconPositions) = locateScannersAndBeacons(scanners, 12)
    println(beaconPositions.size)
    println(findMaxEuclideanDistance(scannerPositions))
  }
}