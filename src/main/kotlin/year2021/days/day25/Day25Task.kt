package year2021.days.day25

import year2021.days.getFile
import java.util.*

private const val FILENAME = "day25/input.txt"
private const val TEST_FILENAME = "day25/test_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines))
}

fun solve1Task(lines: List<String>): Int {
    val height = lines.size
    val width = lines.maxOfOrNull { it.length } ?: 0
    val initialState = CharArray(width * height) { lines[it / width].getOrElse(it % width) { '.' } }
    var n = 1
    val state = initialState.copyOf()

    while (true) {
        val a = (0 until height).fold(true) { acc, y ->
            val indices = (0 until width).filter { x ->
                state[y * width + x] == '>' && state[y * width + (x + 1) % width] == '.'
            }
            for (x in indices) {
                state[y * width + x] = '.'
                state[y * width + (x + 1) % width] = '>'
            }
            acc && indices.isEmpty()
        }
        val b = (0 until width).fold(true) { acc, x ->
            val indices = (0 until height).filter { y ->
                state[y * width + x] == 'v' && state[(y + 1) % height * width + x] == '.'
            }
            for (y in indices) {
                state[y * width + x] = '.'
                state[(y + 1) % height * width + x] = 'v'
            }
            acc && indices.isEmpty()
        }
        if (a && b) return n else n++
    }
}