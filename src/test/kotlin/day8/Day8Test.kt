package day8

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class Day8Test {
  private val sampleData = listOf(
    "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe",
    "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc",
    "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg",
    "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb",
    "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea",
    "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb",
    "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe",
    "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef",
    "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb",
    "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"
  )

  @Test
  fun `count occurrences of 1, 4, 7 and 8 in the output`() {
    val listOfDisplayDigitRows = parseOnlyDigitsOutput(sampleData)

    assertEquals(26, countOneFourSevenAndEights(listOfDisplayDigitRows))
  }

  @Test
  fun `decipher panel digits`() {
    val listOfExpected = listOf(8394, 9781, 1197, 9361, 4873, 8418, 4548, 1625, 8717, 4315)

    sampleData.zip(listOfExpected).forEach { (entry, expected) ->
      assertEquals(expected, decipherSignal(entry))
    }
  }
}
