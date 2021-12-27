package year2021.days.day06

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day6Test {

    private val testInput = listOf(3,4,3,1,2)

    @Test
    fun testAmountAfterDays() {
        assertEquals(26, lanternfishAmount(testInput, 18))
        assertEquals(5934, lanternfishAmount(testInput, 80))
        assertEquals(26984457539, lanternfishAmount(testInput, 256))
    }
}