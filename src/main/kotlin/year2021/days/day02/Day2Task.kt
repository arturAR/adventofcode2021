package year2021.days.day02

import year2021.days.getFile

private const val FILENAME = "day02/input.txt"
private const val TEST_FILENAME = "day02/test_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

fun solve1Task(lines: List<String>): Int {
    val result = lines.fold(Pair(0, 0)) { acc, line -> parseLine(line, acc) }

    println("coord: " + result.first + " | " + result.second)
    return (result.first * result.second)
}

private fun parseLine(line: String, coord: Pair<Int, Int>): Pair<Int, Int> {
    val split = line.split(" ")
    return when (split[0]) {
        "forward" -> Pair(coord.first + Integer.parseInt(split[1]), coord.second)
        "down" -> Pair(coord.first, coord.second + Integer.parseInt(split[1]))
        "up" -> Pair(coord.first, coord.second - Integer.parseInt(split[1]))
        else -> coord
    }
}

fun solve2Task(lines: List<String>): Int {
    val result = lines.fold(Triple(0,0,0)) { acc, line -> parseLine(line, acc) }

    println("coord: " + result.first + " | " + result.second)
    return (result.first * result.second)
}

private fun parseLine(line: String, coord: Triple<Int, Int, Int>): Triple<Int, Int, Int> {
    val split = line.split(" ")
    return when (split[0]) {
        "forward" -> Triple(
            coord.first + Integer.parseInt(split[1]),
            coord.second + (coord.third * Integer.parseInt(split[1])),
            coord.third
        )
        "down" -> Triple(coord.first, coord.second, coord.third + Integer.parseInt(split[1]))
        "up" -> Triple(coord.first, coord.second, coord.third - Integer.parseInt(split[1]))
        else -> coord
    }
}