package day16

import java.io.File
import java.util.Optional


sealed interface Packet {
  val version: Int
  val value: Long
}

data class Literal(override val version: Int, override val value: Long) : Packet
data class Op(override val version: Int, private val type: Int, val packets: List<Packet>) : Packet {
  override val value: Long
    get() = when (type) {
      0 -> packets.sumOf { it.value }
      1 -> packets.fold(1L) { acc, packet -> acc * packet.value }
      2 -> packets.minOf { it.value }
      3 -> packets.maxOf { it.value }
      5 -> if (packets[0].value > packets[1].value) 1 else 0
      6 -> if (packets[0].value < packets[1].value) 1 else 0
      7 -> if (packets[0].value == packets[1].value) 1 else 0
      else -> TODO("Operation $type is not supported")
    }
}

private val hexBinMap2 = (0..0xF).associate { n -> n.toString(16).first().uppercaseChar() to n.toString(2).padStart(4, '0') }
fun hexToBits(input: String): Iterator<Char> = input.flatMap { hexBinMap2[it]!!.asIterable() }.iterator()

// Syntax sugar methods that will carelessly throw exception if there aren't enough bits
private fun Iterator<Char>.take(bits: Int): String = (1..bits).map { next() }.joinToString("")
private fun Iterator<Char>.takeInt(bits: Int): Int = take(bits).toInt(2)

fun decodePackets(input: Iterator<Char>): List<Packet> = sequence {
  while (input.hasNext()) {
    val next = decodeNextPacket(input)
    if (next.isPresent) yield(next.get())
  }
}.toList()

fun decodeNPackets(input: Iterator<Char>, number: Int) = (1..number).map { decodeNextPacket(input).get() }

fun decodeNextPacket(input: Iterator<Char>): Optional<Packet> = runCatching {
  val version = input.takeInt(3)
  val typeId = input.takeInt(3)
  return Optional.ofNullable(
    when (typeId) {
      4 -> Literal(version, decodeLiteralPayload(input))
      else -> Op(version, typeId, decodeOperatorPayload(input))
    }
  )
}.getOrElse { Optional.empty() }

private fun decodeLiteralPayload(input: Iterator<Char>): Long {
  tailrec fun rec(input: Iterator<Char>, number: Long): Long {
    val controlBit = input.next()
    val n = number.shl(4) + input.takeInt(4)
    return if (controlBit == '0') n else rec(input, n)
  }
  return rec(input, 0)
}

private fun decodeOperatorPayload(input: Iterator<Char>): List<Packet> = runCatching {
  when (input.next()) {
    '0' -> { // takes 15 bits to parse the size of the payload in bits
      decodePackets(boundedIteratorWrap(input, input.takeInt(15)))
    }
    '1' -> { // takes 11 bits to parse the number of packets in its payload
      decodeNPackets(input, input.takeInt(11))
    }
    else -> TODO("Should never happen")
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
    is Op -> p.packets.fold(p.version.toLong()) { acc, packet -> acc + sumVersions(packet) }
  }

  return decodePackets(input).fold(0L) { acc, packet -> acc + sumVersions(packet) }
}

fun evaluateTransmission(input: Iterator<Char>): Long = decodeNextPacket(input).get().value

fun main() {
  File("./input/day16.txt").useLines { lines ->
    val transmission = lines.first()
    println(sumAllVersionsOfTheTransmission(hexToBits(transmission)))
    println(evaluateTransmission(hexToBits(transmission)))
  }
}
