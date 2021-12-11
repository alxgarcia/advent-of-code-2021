package day10

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day10Test {
  private val sampleData = listOf(
    "[({(<(())[]>[[{[]{<()<>>",
    "[(()[<>])]({[<{<<[]>>(",
    "{([(<{}[<>[]}>{[]{[(<()>",
    "(((({<>}<{<{<>}{[]{[]{}",
    "[[<[([]))<([[{}[[()]]]",
    "[{[{({}]{}}([{[{{{}}([]",
    "{<[[]]>}<{[{[{[]{()[[[]",
    "[<(<(<(<{}))><([]([]()",
    "<{([([[(<>()){}]>(<<{{",
    "<{([{{}}[<[[[<>{}]]]>[]]",
  )

  @Test
  fun `finds the offending char in corrupted lines`() {
    assertEquals(null, findMissingSequenceOrOffendingChar("").first)
    assertEquals(null, findMissingSequenceOrOffendingChar("()").first)
    assertEquals(null, findMissingSequenceOrOffendingChar("([])").first)

    assertEquals('}', findMissingSequenceOrOffendingChar("{([(<{}[<>[]}>{[]{[(<()>").first)
    assertEquals(']', findMissingSequenceOrOffendingChar("[{[{({}]{}}([{[{{{}}([]").first)
  }

  @Test
  fun `sum the score of corrupted lines`() {
    assertEquals(26397, sumScoreOfCorruptedLines(sampleData))
  }

  @Test
  fun `find missing sequence`() {
    assertEquals("])}>", findMissingSequenceOrOffendingChar("<{([{{}}[<[[[<>{}]]]>[]]").second)
    assertEquals("}}]])})]", findMissingSequenceOrOffendingChar("[({(<(())[]>[[{[]{<()<>>").second)
  }

  @Test
  fun `middle score in incomplete lines`() {
    assertEquals(288957, computeMiddleScoreOfIncompleteLinesMissingChars(sampleData))
  }
}
