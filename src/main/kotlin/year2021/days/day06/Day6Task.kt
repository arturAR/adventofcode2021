package year2021.days.day06

import year2021.days.getFile

private const val FILENAME = "day06/input.txt"
private const val TEST_FILENAME = "day06/test_input.txt"

private const val AMOUNT_OF_DAYS_TASK_1 = 80
private const val AMOUNT_OF_DAYS_TASK_2 = 256

fun main() {
    val fishes = getFile(FILENAME).readLines()[0]
        .split(",")
        .map { it.toInt() }

    println(lanternfishAmount(fishes, AMOUNT_OF_DAYS_TASK_1))
    println(lanternfishAmount(fishes, AMOUNT_OF_DAYS_TASK_2))
}

fun lanternfishAmount(input: List<Int>, numDays: Int): Long {
    var fishGen = MutableList(9) { 0L }
        .apply { input.forEach {fish -> this[fish]++}}

    repeat(numDays) {
        val newFishGen = MutableList(9) { 0L }
        newFishGen[8] = fishGen[0]
        newFishGen[6] = fishGen[0]
        for (i in 1 until 9) {
            newFishGen[i - 1] += fishGen[i]
        }
        fishGen = newFishGen
    }
    return fishGen.sum()
}
