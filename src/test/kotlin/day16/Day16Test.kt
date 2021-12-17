package day16

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day16Test {
  @Test
  fun `decoding literal value`() {
    val testCases = listOf(
      //1101 0010 1111 1110 0010 1000 -> 0xD2FE28
      //VVVT TTAA AAAB BBBB CCCC Cxxx
      hexToBits("D2FE28") to Literal(6, 2021),
      "11010001010".iterator() to Literal(6, 10),
      "0101001000100100".iterator() to Literal(2, 20),
    )

    for ((input, expected) in testCases) {
      assertEquals(expected, decodeNextPacket(input).get())
    }
  }

  @Test
  fun `decoding operator of length type 0 value`() {
    val input = hexToBits("38006F45291200")
    assertEquals(
      Op(1, 6, listOf(Literal(6, 10), Literal(2, 20))),
      decodeNextPacket(input).get()
    )
  }

  @Test
  fun `decoding operator of length type 1 value`() {
    val input = hexToBits("EE00D40C82306")
    assertEquals(
      Op(7, 3, listOf(Literal(2, 1), Literal(4, 2), Literal(1, 3))),
      decodeNextPacket(input).get()
    )
  }

  @Test
  fun `part 1 - sum versions`() {
    val testCases = listOf(
      hexToBits("8A004A801A8002F478") to 16L,
      hexToBits("620080001611562C8802118E34") to 12L,
      hexToBits("C0015000016115A2E0802F182340") to 23L,
      hexToBits("A0016C880162017C3686B18A3D4780") to 31L,
    )

    for ((input, expected) in testCases) {
      assertEquals(expected, sumAllVersionsOfTheTransmission(input))
    }
  }

  @Test
  fun `part 2 - evaluate`() {
    val testCases = listOf(
      hexToBits("C200B40A82") to 3L,
      hexToBits("04005AC33890") to 54L,
      hexToBits("880086C3E88112") to 7L,
      hexToBits("CE00C43D881120") to 9L,
      hexToBits("D8005AC2A8F0") to 1L,
      hexToBits("F600BC2D8F") to 0L,
      hexToBits("9C005AC2F8F0") to 0L,
      hexToBits("9C0141080250320F1802104A08") to 1L,
    )

    for ((input, expected) in testCases) {
      assertEquals(expected, evaluateTransmission(input))
    }
  }
}
