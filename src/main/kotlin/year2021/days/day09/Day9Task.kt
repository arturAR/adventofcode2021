package year2021.days.day09

import year2021.days.getFile

private const val FILENAME = "day09/input.txt"
private const val TEST_FILENAME = "day09/test_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

data class Coord(val x: Int, val y: Int)

fun solve1Task(lines: List<String>): Int {
    val (_, mapFiltered) = buildMaps(lines)

    return mapFiltered.values.sumOf { it + 1 }
}

fun solve2Task(lines: List<String>): Int {
    val (map, mapFiltered) = buildMaps(lines)

    return mapFiltered.map { (key, _) ->
        getBasin(key, map).size
    }.sortedDescending()
        .take(3)
        .reduce { x, y -> x * y }
}

fun getBasin(key: Coord, map: Map<Coord, Int>): Set<Coord> {
    val basin = mutableSetOf(key)
    val visitedCoords = mutableSetOf(key)
    val toVisitCoords = getAdjacents(key).toMutableList()

    while (toVisitCoords.size > 1) {
        val next = toVisitCoords.removeFirst()
        visitedCoords.add(next)
        if(map.getOrDefault(next, 9) != 9) {
            basin.add(next)
            toVisitCoords.addAll(getAdjacents(next).filterNot { visitedCoords.contains(it) })
        }
    }

    return basin;
}

fun buildMaps(lines: List<String>): Pair<Map<Coord, Int>, Map<Coord, Int>> {
    val map = buildMap {
        lines.forEachIndexed { y, line ->
            line.toCharArray()
                .map { it.digitToInt() }
                .forEachIndexed { x, num ->
                    put(Coord(x, y), num)
                }
        }
    }

    val filteredMap = map.filter { (key, value) ->
        getAdjacents(key)
            .map { map.getOrDefault(it, 100) }
            .all { value < it }
    }

    return Pair(map, filteredMap)
}

fun getAdjacents(coord: Coord): List<Coord> {
    val up = Coord(coord.x, coord.y - 1)
    val down = Coord(coord.x, coord.y + 1)
    val left = Coord(coord.x - 1, coord.y)
    val right = Coord(coord.x + 1, coord.y)
    return listOf(left, up, down, right)
}
