package day18

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day18Test {
  @Test
  fun `parse the tree structure of a SnailFish number`() {
    val testCases = listOf(
      "[1,2]",
      "[[1,2],3]",
      "[9,[8,7]]",
      "[[1,9],[8,5]]",
      "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]",
      "[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]",
      "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
      "[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]",
    )

    for (input in testCases) {
      assertEquals(input, parseSnailFishNumber(input).toString())
    }
  }

  @Test
  fun `exploit a SnailFish number`() {
    val testCases = listOf(
      "[[[[[9,8],1],2],3],4]" to "[[[[0,9],2],3],4]",
      "[7,[6,[5,[4,[3,2]]]]]" to "[7,[6,[5,[7,0]]]]",
      "[[6,[5,[4,[3,2]]]],1]" to "[[6,[5,[7,0]]],3]",
      "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]" to "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
      "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]" to "[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
    )

    for ((input, expected) in testCases) {
      val n = parseSnailFishNumber(input)
      n.reduceOnce()
      assertEquals(expected, n.toString())
    }
  }

  @Test
  fun `sum two SnailFish numbers`() {
    val n1 = parseSnailFishNumber("[[[[4,3],4],4],[7,[[8,4],9]]]")
    val n2 = parseSnailFishNumber("[1,1]")

    val expected = "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"

    assertEquals(expected, sum(n1, n2).toString())
  }

  @Test
  fun `SnailFish number's magnitude`() {
    val input = "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"
    assertEquals(1384, parseSnailFishNumber(input).magnitude)
  }
}