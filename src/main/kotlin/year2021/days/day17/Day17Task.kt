package year2021.days.day17

import year2021.days.getFile
import java.util.regex.Pattern


private const val FILENAME = "day17/input.txt"
private const val TEST_FILENAME = "day17/test_input.txt"
private val AREA_PATTERN: Pattern = Pattern
    .compile("^target area: x=(-?\\d+)..(-?\\d+), y=(-?\\d+)..(-?\\d+)$")

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines[0]))
    println(solve2Task(lines[0]))
}

fun solve1Task(input: String): Int {
    val area = fromString(input)
    return area.velocityCandidates().maxOf { testTrajectory(it.x, it.y, area) }
}

fun solve2Task(input: String): Int {
    val area = fromString(input)
    return area.velocityCandidates().count { testTrajectory(it.x, it.y, area) > Int.MIN_VALUE }
}

fun testTrajectory(x: Int, y: Int, targetArea: Area): Int {
    var velocity = Coord(x, y)
    var position = Coord(0, 0)
    var maxY = Int.MIN_VALUE

    while (targetArea.before(position)) {
        if (maxY < position.y) {
            maxY = position.y
        }
        position = position.plus(velocity)
        velocity = velocity.slowDown()
    }
    if (targetArea.contains(position)) {
        return maxY;
    }
    return Integer.MIN_VALUE;
}

data class Coord(val x: Int, val y: Int) {
    fun slowDown(): Coord {
        val nX = if (x > 0) x - 1 else if (x < 0) x + 1 else 0
        val nY = y - 1
        return Coord(nX, nY)
    }

    fun plus(velocity: Coord): Coord {
        return Coord(x + velocity.x, y + velocity.y)
    }
}

data class Area(val topLeft: Coord, val bottomRight: Coord) {
    fun contains(coord: Coord): Boolean {
        return topLeft.x <= coord.x && coord.x <= bottomRight.x &&
                coord.y <= topLeft.y && bottomRight.y <= coord.y
    }

    fun before(coord: Coord): Boolean {
        return (coord.x < topLeft.x && bottomRight.y < coord.y) ||
                (coord.x < bottomRight.x && topLeft.y < coord.y);
    }

    fun velocityCandidates(): List<Coord> {
        val result: MutableList<Coord> = ArrayList(10000)
        for (x in 0 until bottomRight.x * 2) {
            for (y in bottomRight.y * 2 until -bottomRight.y * 2) {
                result.add(Coord(x, y))
            }
        }
        return result
    }
}

fun fromString(input: String): Area {
    val m = AREA_PATTERN.matcher(input)
    require(m.matches()) { "does not match: $input" }
    val minX = m.group(1).toInt()
    val maxX = m.group(2).toInt()
    val minY = m.group(3).toInt()
    val maxY = m.group(4).toInt()
    return Area(Coord(minX, maxY), Coord(maxX, minY))
}