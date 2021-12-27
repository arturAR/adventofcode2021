package year2021.days.day01

import year2021.days.getFile

private const val FILENAME = "day01/input.txt"
private const val TEST_FILENAME = "day01/test_input.txt"
private const val TEST_LONG_FILENAME = "day01/test_long_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

fun solve1Task(lines: List<String>): Int {
    var result = 0
    for (i in 1 until lines.size) {
        val prev = lines[i - 1].toInt()
        val curr = lines[i].toInt()
        if (curr > prev) {
            result++
        }
    }
    return result
}

fun solve2Task(lines: List<String>): Int {
    var result = 0
    var last = lines[0].toInt() + lines[1].toInt() + lines[2].toInt()
    for (i in 1 until lines.size - 1) {
        val next = lines[i - 1].toInt() + lines[i].toInt() + lines[i + 1].toInt()
        if (next > last) {
            result++
        }
        last = next
    }
    return result
}