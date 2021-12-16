package day16

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day16Test {
  @Test
  fun `decoding literal value payload`() {
    val iterator = "101111111000101000".iterator()
    assertEquals(2021L, decodeLiteralPayload(iterator))
  }

  @Test
  fun `decoding literal value`() {
    val testCases = listOf(
      //1101 0010 1111 1110 0010 1000 -> 0xD2FE28
      //VVVT TTAA AAAB BBBB CCCC Cxxx
      hexaToBits("D2FE28") to Literal(6, 2021),
      "11010001010".iterator() to Literal(6, 10),
      "0101001000100100".iterator() to Literal(2, 20),
    )

    for ((input, expected) in testCases) {
      assertEquals(expected, decodeNextPacket(input).get())
    }
  }

  @Test
  fun `decoding operator of length type 0 value`() {
    val input = hexaToBits("38006F45291200")
    assertEquals(
      LessThan(1, listOf(Literal(6, 10), Literal(2, 20))),
      decodeNextPacket(input).get()
    )
  }

  @Test
  fun `decoding operator of length type 1 value`() {
    val input = hexaToBits("EE00D40C82306")
    assertEquals(
      Max(7, listOf(Literal(2, 1), Literal(4, 2), Literal(1, 3))),
      decodeNextPacket(input).get()
    )
  }

  @Test
  fun `part 1 - sum versions`() {
    val testCases = listOf(
      hexaToBits("8A004A801A8002F478") to 16L,
      hexaToBits("620080001611562C8802118E34") to 12L,
      hexaToBits("C0015000016115A2E0802F182340") to 23L,
      hexaToBits("A0016C880162017C3686B18A3D4780") to 31L,
    )

    for ((input, expected) in testCases) {
      assertEquals(expected, sumAllVersionsOfTheTransmission(input))
    }
  }

  @Test
  fun `part 2 - evaluate`() {
    val testCases = listOf(
      hexaToBits("C200B40A82") to 3L,
      hexaToBits("04005AC33890") to 54L,
      hexaToBits("880086C3E88112") to 7L,
      hexaToBits("CE00C43D881120") to 9L,
      hexaToBits("D8005AC2A8F0") to 1L,
      hexaToBits("F600BC2D8F") to 0L,
      hexaToBits("9C005AC2F8F0") to 0L,
      hexaToBits("9C0141080250320F1802104A08") to 1L,
    )

    for ((input, expected) in testCases) {
      assertEquals(expected, evaluateTransmission(input))
    }
  }
}
