package year2021.days.day07

import year2021.days.getFile
import kotlin.math.abs

private const val FILENAME = "day07/input.txt"
private const val TEST_FILENAME = "day07/test_input.txt"

fun main() {
    val crabs = getFile(FILENAME).readLines()[0]
        .split(",")
        .map { it.toInt() }

    println(solve1Task(crabs))
    println(solve2Task(crabs))
}

fun solve1Task(input: List<Int>): Int {
    val median = median(input)

    return input.sumOf { abs(it - median) }
}

fun median(input: List<Int>): Int {
    return input.sorted()[input.size / 2]
}

fun solve2Task(input: List<Int>): Int = calculateTotalFuelCosts(
    input,
    fuelCostCalculation = { crabPosition, target ->
        (0..abs(target - crabPosition)).fold(0) { acc, step -> acc + step }
    }
)

fun calculateTotalFuelCosts(input: List<Int>, fuelCostCalculation: (Int, Int) -> Int): Int =
    (0..input.maxOrNull()!!)
        .minOf { candidate -> input.sumOf { crab -> fuelCostCalculation(crab, candidate) } }
