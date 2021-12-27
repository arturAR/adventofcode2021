package year2021.days.day08

import year2021.days.getFile

private const val FILENAME = "day08/input.txt"
private const val TEST_FILENAME = "day08/test_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

fun solve1Task(lines: List<String>): Int {
    return lines.map { it.split("|")[1].trim() }
        .flatMap { it.split(" ") }
        .filter { it.length in intArrayOf(2, 3, 4, 7) }
        .count()
}

fun solve2Task(lines: List<String>): Int {
    return lines.sumOf {
        val split = it.split(" | ")
        val inputDigits = split[0].split(" ")
        val outputDigits = split[1].split(" ")
        val mappings = createMappings(inputDigits)
        outputDigits.map { out -> mappings.indexOf(out.toCharArray().sorted().joinToString("")) }
            .joinToString("")
            .toInt()
    }
}

private fun createMappings(list: List<String>): List<String> {
    val mixMapping = mutableListOf<String>()
    repeat(10) { mixMapping.add("") }

    list.map { it.toCharArray().sorted().joinToString("") }
        .sortedBy { it.length }.forEach {
            when (it.length) {
                2 -> mixMapping[1] = it
                3 -> mixMapping[7] = it
                4 -> mixMapping[4] = it
                7 -> mixMapping[8] = it
                5 -> {
                    when {
                        it.toList().intersect(mixMapping[1].toList()).size == 2 -> mixMapping[3] = it
                        it.toList().intersect(mixMapping[4].toList()).size == 3 -> mixMapping[5] = it
                        else -> mixMapping[2] = it
                    }
                }
                6 -> {
                    when {
                        it.toList().intersect(mixMapping[1].toList()).size < 2 -> mixMapping[6] = it
                        it.toList().intersect(mixMapping[4].toList()).size == 4 -> mixMapping[9] = it
                        else -> mixMapping[0] = it
                    }
                }
            }
        }
    return mixMapping
}