package day16

import java.io.File
import java.util.Optional


sealed interface Packet {
  val version: Int
  val value: Long
}
sealed interface Operation : Packet {
  val packets: List<Packet>
}

data class Literal(override val version: Int, override val value: Long) : Packet
data class Sum(override val version: Int, override val packets: List<Packet>) : Operation {
  override val value: Long
    get() = packets.sumOf { it.value }
}
data class Product(override val version: Int, override val packets: List<Packet>) : Operation {
  override val value: Long
    get() = packets.fold(1L) { acc, packet -> acc * packet.value }
}
data class Min(override val version: Int, override val packets: List<Packet>) : Operation {
  override val value: Long
    get() = packets.minOf { it.value }
}
data class Max(override val version: Int, override val packets: List<Packet>) : Operation {
  override val value: Long
    get() = packets.maxOf { it.value }
}
data class GreaterThan(override val version: Int, override val packets: List<Packet>) : Operation {
  override val value: Long
    get() = if (packets[0].value > packets[1].value) 1 else 0
}
data class LessThan(override val version: Int, override val packets: List<Packet>) : Operation {
  override val value: Long
    get() = if (packets[0].value < packets[1].value) 1 else 0
}
data class Equal(override val version: Int, override val packets: List<Packet>) : Operation {
  override val value: Long
    get() = if (packets[0].value == packets[1].value) 1 else 0
}

private val hexBinMap = mapOf(
  '0' to "0000",
  '1' to "0001",
  '2' to "0010",
  '3' to "0011",
  '4' to "0100",
  '5' to "0101",
  '6' to "0110",
  '7' to "0111",
  '8' to "1000",
  '9' to "1001",
  'A' to "1010",
  'B' to "1011",
  'C' to "1100",
  'D' to "1101",
  'E' to "1110",
  'F' to "1111",
)

fun hexaToBits(input: String): Iterator<Char> = input.flatMap { hexBinMap[it]!!.asIterable() }.iterator()

private fun Iterator<Char>.take(bits: Int): String = (1..bits).map { next() }.joinToString("")
private fun Iterator<Char>.takeInt(bits: Int): Int = take(bits).toInt(2)

fun decodePackets(input: Iterator<Char>): List<Packet> {
  val packets = mutableListOf<Packet>()
  while (input.hasNext()) {
    val next = decodeNextPacket(input)
    if (next.isPresent) packets.add(next.get())
  }
  return packets
}

fun decodeNPackets(input: Iterator<Char>, number: Int) = (1..number).map { decodeNextPacket(input).get() }

fun decodeNextPacket(input: Iterator<Char>): Optional<Packet> =
  decodePacketHeader(input)
    .map { (version, typeId) ->
      when (typeId) {
        0 -> Sum(version, decodeOperatorPayload(input))
        1 -> Product(version, decodeOperatorPayload(input))
        2 -> Min(version, decodeOperatorPayload(input))
        3 -> Max(version, decodeOperatorPayload(input))
        4 -> Literal(version, decodeLiteralPayload(input))
        5 -> GreaterThan(version, decodeOperatorPayload(input))
        6 -> LessThan(version, decodeOperatorPayload(input))
        7 -> Equal(version, decodeOperatorPayload(input))
        else -> TODO()
      }
    }

fun decodePacketHeader(input: Iterator<Char>): Optional<Pair<Int, Int>> =
  runCatching {
    val version = input.takeInt(3)
    val typeId = input.takeInt(3)
    Optional.of(version to typeId)
  }.getOrElse { Optional.empty() }

fun decodeLiteralPayload(input: Iterator<Char>): Long {
  tailrec fun rec(input: Iterator<Char>, number: Long): Long {
    val controlBit = input.next()
    val n = number.shl(4) + input.takeInt(4)
    return if (controlBit == '0') n else rec(input, n)
  }
  return rec(input, 0)
}

fun decodeOperatorPayload(input: Iterator<Char>): List<Packet> =
  runCatching {
    when (input.next()) { // O -> 15, 1 -> 11
      '0' -> {
        val limit = input.takeInt(15)
        decodePackets(boundedIteratorWrap(input, limit))
      }
      '1' -> {
        val number = input.takeInt(11)
        decodeNPackets(input, number)
      }
      else -> TODO()
    }
  }.getOrElse { emptyList() }

private fun boundedIteratorWrap(iterator: Iterator<Char>, limit: Int) = object : Iterator<Char> {
  private var counter = 0
  override fun hasNext(): Boolean = iterator.hasNext() && counter < limit

  override fun next(): Char =
    if (hasNext()) {
      counter += 1
      iterator.next()
    } else throw NoSuchElementException("End of bounded iterator")
}

fun sumAllVersionsOfTheTransmission(input: Iterator<Char>): Long {
  fun sumVersions(p: Packet): Long = when (p) {
    is Literal -> p.version.toLong()
    is Operation -> p.packets.fold(p.version.toLong()) { acc, packet -> acc + sumVersions(packet) }
  }

  return decodePackets(input).fold(0L) { acc, packet -> acc + sumVersions(packet) }
}

fun evaluateTransmission(input: Iterator<Char>): Long {
  val decodePackets = decodePackets(input)
  return decodePackets.first().value
}

fun main() {
  File("./input/day16.txt").useLines { lines ->
    val transmission = lines.first()
    println(sumAllVersionsOfTheTransmission(hexaToBits(transmission)))
    println(evaluateTransmission(hexaToBits(transmission)))
  }
}
