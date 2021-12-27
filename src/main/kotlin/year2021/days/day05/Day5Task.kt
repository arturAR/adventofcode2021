package year2021.days.day05

import year2021.days.getFile
import kotlin.math.abs
import kotlin.math.sign

private const val FILENAME = "day05/input.txt"
private const val TEST_FILENAME = "day05/test_input.txt"

fun main() {
    val segments = getFile(FILENAME).readLines().map { line ->
        val (start, end) = line.split(" -> ", limit = 2)
        val (x0, y0) = start.split(',', limit = 2)
        val (x1, y1) = end.split(',', limit = 2)
        Pair(x0.toInt() to y0.toInt(), x1.toInt() to y1.toInt())
    }

    println(firstTaskMap(segments).values.count { it > 1 })
    println(secondTaskMap(segments).values.count { it > 1 })
}

fun firstTaskMap(segments: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Map<Pair<Int, Int>, Int> = buildMap {
    for ((start, end) in segments) {
        val (x0, y0) = start
        val (x1, y1) = end
        when {
            x0 == x1 -> for (y in minOf(y0, y1)..maxOf(y0, y1)) put(x0 to y, getOrElse(x0 to y) { 0 } + 1)
            y0 == y1 -> for (x in minOf(x0, x1)..maxOf(x0, x1)) put(x to y0, getOrElse(x to y0) { 0 } + 1)
        }
    }
}

fun secondTaskMap(segments: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Map<Pair<Int, Int>, Int> = buildMap {
    for ((start, end) in segments) {
        val (x0, y0) = start
        val (x1, y1) = end
        when {
            x0 == x1 -> for (y in minOf(y0, y1)..maxOf(y0, y1)) put(x0 to y, getOrElse(x0 to y) { 0 } + 1)
            y0 == y1 -> for (x in minOf(x0, x1)..maxOf(x0, x1)) put(x to y0, getOrElse(x to y0) { 0 } + 1)
            abs(x0 - x1) == abs(y0 - y1) -> {
                val dx = (x1 - x0).sign
                val dy = (y1 - y0).sign
                for (i in 0..maxOf(abs(y0 - y1))) {
                    val xy = x0 + i * dx to y0 + i * dy
                    put(xy, getOrElse(xy) { 0 } + 1)
                }
            }
        }
    }
}
