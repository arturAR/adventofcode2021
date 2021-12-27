package year2021.days.day14

import year2021.days.getFile

private const val FILENAME = "day14/input.txt"
private const val TEST_FILENAME = "day14/test_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    val polymer = lines[0]
    val templates = lines.drop(2)
        .map { it.split(" -> ") }
        .associate { it[0] to it[1] }

    println(solveTask(polymer, templates, 10))
//    println(solveTask(polymer, templates, 40)) //
    println(solveTaskFaster(lines, 10))
    println(solveTaskFaster(lines, 40))
}

fun solveTask(polymer: String, templates: Map<String, String>, steps: Int): Long {
    var nextPolymer = polymer
    repeat(steps) {
        nextPolymer = nextPolymer.windowed(2)
            .joinToString("") {
                it[0] + templates.getOrDefault(it, "")
            } + nextPolymer.last()
    }

    val map = buildMap<Char, Long> {
        nextPolymer.forEach { put(it, getOrElse(it) { 0 } + 1) }
    }
    return (map.values.maxOrNull()!! - map.values.minOrNull()!!)
}

fun solveTaskFaster(lines: List<String>, steps: Int): Long {
    val initial = lines[0]
    val templates = lines.drop(2).associate {
        val (left, right) = it.split(" -> ")
        left to listOf("${left.first()}$right", "$right${left.last()}")
    }
    var state = initial.zip(initial.drop(1)) { a, b -> "$a$b" }
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }

    repeat(steps) {
        state = buildMap {
            for ((src, n) in state) {
                for (dst in templates.getValue(src)) {
                    put(dst, getOrElse(dst) { 0 } + n)
                }
            }
        }
    }
    val counts = buildMap<Char, Long> {
        put(initial.first(), 1)
        put(initial.last(), getOrElse(initial.last()) { 0 } + 1)
        for ((pair, n) in state) {
            for (c in pair) {
                put(c, getOrElse(c) { 0 } + n)
            }
        }
    }
    return (counts.values.maxOrNull()!! - counts.values.minOrNull()!!) / 2
}
