package year2021.days.day24

import year2021.days.day04.FirstTask
import java.io.File
import java.lang.IllegalStateException
import java.util.*


private const val FILENAME = "day24/input.txt"
private const val TEST_FILENAME = "day24/test_input.txt"

fun main() {
    val path = Objects.requireNonNull(FirstTask::class.java.classLoader.getResource(FILENAME)).path
    val lines = File(path).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

fun solve1Task(input: List<String>): Long? {
    return solve(input, 9 downTo 1)
}

fun solve2Task(input: List<String>): Long? {
    return solve(input, 1..9)
}

fun solve(
    lines: List<String>,
    range: IntProgression,
    alu: Alu = Alu(),
    index: Int = 0,
    prefix: Long = 0L,
    visited: MutableSet<IndexedValue<Alu>> = mutableSetOf(),
): Long? {
    var i = index
    while (i < lines.size) {
        val line = lines[i++]
        if (line.startsWith("inp ")) {
            val lhs = line.substring(4, 5)
            for (value in range) {
                val sub = alu.copy(lhs, value)
                if (!visited.add(IndexedValue(i, sub.copy()))) return null
                solve(lines, range, sub.copy(lhs, value), i, 10 * prefix + value, visited)?.let { return it }
            }
            return null
        } else {
            val op = getOperations().getValue(line.substring(0, 3))
            val lhs = line.substring(4, 5)
            val rhs = line.substring(6)
            alu.set(lhs, op(alu.get(lhs), rhs.toIntOrNull() ?: alu.get(rhs)))
        }
    }

    return if (alu.z == 0) prefix else null
}

fun getOperations(): Map<String, (Int, Int) -> Int> {
    return mapOf(
        "add" to Int::plus,
        "mul" to Int::times,
        "div" to Int::div,
        "mod" to Int::rem,
        "eql" to { x, y -> if (x == y) 1 else 0 },
    )
}

data class Alu(var w: Int = 0, var x: Int = 0, var y: Int = 0, var z: Int = 0) {
    fun copy(key: String, value: Int) = when (key) {
        "w" -> copy(w = value)
        "x" -> copy(x = value)
        "y" -> copy(y = value)
        "z" -> copy(z = value)
        else -> throw IllegalStateException()
    }

    fun get(key: String) = when (key) {
        "w" -> w
        "x" -> x
        "y" -> y
        "z" -> z
        else -> throw IllegalStateException()
    }

    fun set(key: String, value: Int) {
        when (key) {
            "w" -> w = value
            "x" -> x = value
            "y" -> y = value
            "z" -> z = value
            else -> throw IllegalStateException()
        }
    }
}