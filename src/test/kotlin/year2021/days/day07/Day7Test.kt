package year2021.days.day07

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day7Test {

    private val testInput = listOf(16,1,2,0,4,2,7,1,2,14)

    @Test
    fun testMedian() {
        assertEquals(2, median(testInput))
    }

    @Test
    fun test1TaskSolution() {
        assertEquals(37, solve1Task(testInput))
    }


    @Test
    fun test2TaskSolution() {
        assertEquals(168, solve2Task(testInput))
    }
}