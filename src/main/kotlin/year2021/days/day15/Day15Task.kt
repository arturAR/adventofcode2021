package year2021.days.day15

import year2021.days.getFile
import java.util.*

private const val FILENAME = "day15/input.txt"
private const val TEST_FILENAME = "day15/test_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

fun solve1Task(lines: List<String>): Int {
    val map = buildMap(lines)
    return findLowestCost(map)
}

fun solve2Task(lines: List<String>): Int {
    val map = buildMap(lines)
    val bigMap = extendMap(map)
    return findLowestCost(bigMap)
}

fun findLowestCost(map: Map<Coord, Int>):Int {
    val start = Coord(0,0)
    val end = Coord(map.keys.maxOf { it.x }, map.keys.maxOf { it.y })

    val queue = PriorityQueue<Path>()
    val seen = mutableSetOf<Coord>()
    queue.add(Path(start, 0))
    while (queue.isNotEmpty() && queue.peek().coordAt != end) {
        val top = queue.poll()
        getAdjacents(top.coordAt)
            .filter { map.containsKey(it) }
            .filter { !seen.contains(it) }
            .map { Path(it, top.cost + map.getOrDefault(it, Int.MAX_VALUE)) }
            .forEach {
                seen.add(it.coordAt)
                queue.add(it)
            }
    }
    return queue.peek().cost
}


fun extendMap(map: Map<Coord, Int>): Map<Coord, Int> {
    var maxX = map.keys.maxOf { it.x }
    val maxY = map.keys.maxOf { it.y }

    return buildMap {
        putAll(map)
        for (x in maxX + 1 until 5 * (maxX + 1)) {
            for (y in 0..maxY) {
                put(Coord(x, y), fixValue(this[Coord(x - maxX - 1, y)]!! + 1))
            }
        }
        maxX = this.keys.maxOf { it.x }
        for (y in maxY + 1 until 5 * (maxY + 1)) {
            for (x in 0..maxX) {
                put(Coord(x, y), fixValue(this[Coord(x, y - maxY - 1)]!! + 1))
            }
        }
    }
}

fun fixValue(i: Int): Int {
    var n = i
    while (n > 9) {
        n -= 9
    }
    return n
}

data class Coord(val x: Int, val y: Int)
data class Path(val coordAt: Coord, val cost: Int) : Comparable<Path> {
    override fun compareTo(other: Path): Int {
        return Comparator.comparingInt<Path> { it.cost }.compare(this, other)
    }
}

fun buildMap(lines: List<String>): Map<Coord, Int> {
    return buildMap {
        lines.forEachIndexed { y, line ->
            line.toCharArray()
                .map { it.digitToInt() }
                .forEachIndexed { x, num ->
                    put(Coord(x, y), num)
                }
        }
    }
}

fun getAdjacents(coord: Coord): List<Coord> {
    val up = Coord(coord.x, coord.y - 1)
    val left = Coord(coord.x - 1, coord.y)
    val right = Coord(coord.x + 1, coord.y)
    val down = Coord(coord.x, coord.y + 1)
    return listOf(up, left, right, down)
}

fun printMap(map: Map<Coord, Int>) {
    val maxX = map.keys.maxOf { it.x }
    val maxY = map.keys.maxOf { it.y }
    for (y in 0..maxY) {
        for (x in 0..maxX) {
            print(map[Coord(x,y)])
        }
        println()
    }
}