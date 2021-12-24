package day21

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day21Test {
  @Test
  fun `plays game according to the rules`() {
    assertEquals(739785, playDeterministicGame(4, 8))
  }

  @Test
  fun `multiverse dice`() {
    assertEquals(444356092776315 to 341960390180808, countUniversesInWhichPlayerWins(4, 8))
  }
}
