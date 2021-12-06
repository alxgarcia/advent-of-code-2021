package day2

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day2Test {
  internal class Submarine1Test {
    @Test
    fun `it moves forward`() {
      val submarine = Submarine1.fromStart
      val updated = processInstruction(submarine, "forward 5")
      assertEquals(Submarine1(5, 0), updated)
    }

    @Test
    fun `it goes up`() {
      val submarine = Submarine1.fromStart
      val updated = processInstruction(submarine, "up 5")
      assertEquals(Submarine1(0, -5), updated)
    }

    @Test
    fun `it goes down`() {
      val submarine = Submarine1.fromStart
      val updated = processInstruction(submarine, "down 5")
      assertEquals(Submarine1(0, 5), updated)
    }
  }

  internal class Submarine2Test {
    @Test
    fun `it moves forward`() {
      val submarine = Submarine2.fromStart
      val updated = processInstruction(submarine, "forward 5")
      assertEquals(Submarine2(5, 0, 0), updated)
    }

    @Test
    fun `it moves aim up`() {
      val submarine = Submarine2.fromStart
      val updated = processInstruction(submarine, "up 5")
      assertEquals(Submarine2(0, 0, -5), updated)
    }

    @Test
    fun `it moves aim down`() {
      val submarine = Submarine2.fromStart
      val updated = processInstruction(submarine, "down 5")
      assertEquals(Submarine2(0, 0, 5), updated)
    }

    @Test
    fun `it updates depth with the aim as it moves forward`() {
      val updated1 = listOf("down 6", "forward 5").fold(Submarine2.fromStart, ::processInstruction)
      assertEquals(Submarine2(5, 30, 6), updated1)

      val updated2 = listOf("up 12", "forward 5").fold(updated1, ::processInstruction)
      assertEquals(Submarine2(10, 0, -6), updated2)
    }
  }

  private val instructionsSample: List<String> = listOf(
    "forward 5",
    "down 5",
    "forward 8",
    "up 3",
    "down 8",
    "forward 2"
  )

  @Test
  fun `computes final horizontal position and depth`() {
    assertEquals(150, computeFinalHorizontalPositionAndDepthProduct(instructionsSample))
  }

  @Test
  fun `computes final horizontal position and depth - take 2`() {
    assertEquals(900, computeFinalHorizontalPositionAndDepthProduct2(instructionsSample))
  }
}