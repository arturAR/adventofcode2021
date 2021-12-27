package year2021.days.day11

import year2021.days.getFile

private const val FILENAME = "day11/input.txt"
private const val TEST_FILENAME = "day11/test_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines, 100))
    println(solve2Task(lines))
}

fun solve1Task(lines: List<String>, steps: Int): Int {
    val map = buildMap(lines)

    for (currStep in 1..steps) {
        calculateStep(currStep, map)
    }

    return map.values.sumOf { it.flashCounter }
}

fun solve2Task(lines: List<String>): Int {
    val map = buildMap(lines)
    var step = -1

    for (currStep in 1..Int.MAX_VALUE) {
        calculateStep(currStep, map)
        if (map.values.all { currStep == it.lastFlashedStep }) {
            step = currStep
            break
        }
    }

    return step;
}

fun calculateStep(currStep: Int, map: Map<Coord, Octopus>) {
    map.values.forEach { it.increaseEnergy(currStep) }

    while (map.values.any { it.isReadyToFlash(currStep) }) {
        map.values.filter { it.isReadyToFlash(currStep) }
            .forEach {
                if (it.flash(currStep)) {
                    getOctopusAdjacents(it.coord, map).forEach { it1 -> it1.increaseEnergy(currStep) }
                }
            }
    }
}

data class Coord(val x: Int, val y: Int)

class Octopus(val coord: Coord, var energy: Int) {
    var flashCounter = 0
    var lastFlashedStep = -1

    fun increaseEnergy(step: Int) {
        if (step > lastFlashedStep) {
            energy++
        }
    }

    fun isReadyToFlash(step: Int): Boolean {
        return step > lastFlashedStep && energy > 9;
    }

    fun flash(step: Int): Boolean {
        if (isReadyToFlash(step)) {
            energy = 0
            lastFlashedStep = step;
            flashCounter++
            return true
        }

        return false
    }
}

fun buildMap(lines: List<String>): Map<Coord, Octopus> {
    return buildMap {
        lines.forEachIndexed { y, line ->
            line.toCharArray()
                .map { it.digitToInt() }
                .forEachIndexed { x, num ->
                    put(Coord(x, y), Octopus(Coord(x, y), num))
                }
        }
    }
}

fun getOctopusAdjacents(coord: Coord, map: Map<Coord, Octopus>): List<Octopus> {
    return getAdjacents(coord).mapNotNull { map.get(it) }
}

fun getAdjacents(coord: Coord): List<Coord> {
    val upLeft = Coord(coord.x - 1, coord.y - 1)
    val up = Coord(coord.x, coord.y - 1)
    val upRight = Coord(coord.x + 1, coord.y - 1)
    val left = Coord(coord.x - 1, coord.y)
    val right = Coord(coord.x + 1, coord.y)
    val downLeft = Coord(coord.x - 1, coord.y + 1)
    val down = Coord(coord.x, coord.y + 1)
    val downRight = Coord(coord.x + 1, coord.y + 1)
    return listOf(upLeft, up, upRight, left, right, downLeft, down, downRight)
}