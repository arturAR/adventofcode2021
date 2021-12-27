package year2021.days.day03

import year2021.days.getFile

private const val FILENAME = "day03/input.txt"
private const val TEST_FILENAME = "day03/test_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

private fun solve1Task(lines: List<String>): Int {
    val ones = IntArray(lines[0].length)
    val zeros = IntArray(lines[0].length)

    lines.forEach { line ->
        run {
            val lineTab = line.toCharArray()
            for (index in lineTab.indices) {
                if(lineTab[index] == '1') {
                    ones[index]++
                } else {
                    zeros[index]++
                }
            }
        }
    }
    val gamma = IntArray(lines[0].length)
    val epsilon = IntArray(lines[0].length)

    for (index in ones.indices) {
        if(ones[index] > zeros[index]) {
            gamma[index] = 1
        } else {
            epsilon[index] = 1
        }
    }

    val gammaDec = Integer.parseInt(gamma.joinToString(""), 2)
    val epsilonDec = Integer.parseInt(epsilon.joinToString(""), 2)
    println(gammaDec)
    println(epsilonDec)
    return gammaDec * epsilonDec
}

private fun solve2Task(lines: List<String>): Int {
    val oxygenNum = calculate(lines, '0' , '1')
    val co2Num = calculate(lines, '1' , '0')

    println(oxygenNum)
    println(co2Num)
    return (oxygenNum * co2Num)
}

private fun calculate(lines: List<String>, char1: Char, char2: Char) : Int {
    val list = lines.toMutableList()
    var index = 0
    while (list.size > 1) {
        val (ones, zeros) = oneZeros(list);
        list.removeAll { line ->
            ones[index] >= zeros[index] && line.toCharArray()[index] == char1 ||
                    ones[index] < zeros[index] && line.toCharArray()[index] == char2 }
        index++
    }

    return list[0].toInt(2)
}

private fun oneZeros(lines: List<String>): Pair<IntArray, IntArray> {
    val ones = IntArray(lines[0].length)
    val zeros = IntArray(lines[0].length)
    lines.forEach { line ->
        run {
            val lineTab = line.toCharArray()
            for (index in lineTab.indices) {
                if (lineTab[index] == '1') {
                    ones[index]++
                } else {
                    zeros[index]++
                }
            }
        }
    }
    return Pair(ones, zeros)
}

