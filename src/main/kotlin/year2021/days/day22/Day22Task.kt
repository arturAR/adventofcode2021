package year2021.days.day22

import year2021.days.getFile
import java.util.regex.Pattern
import kotlin.math.max
import kotlin.math.min

private const val FILENAME = "day22/input.txt"
private const val TEST_FILENAME = "day22/test_input.txt"
private const val TEST_FILENAME_2 = "day22/test_input_2.txt"
private val PROCEDURE_PATTERN: Pattern = Pattern.compile("x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)$")

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

fun solve1Task(lines: List<String>): Int {
    val map: MutableMap<Coord, Boolean> = mutableMapOf()
    lines.map { Procedure.fromString(it) }
        .filter { it.xRange.first >= -50 && it.xRange.last <= 50 }
        .filter { it.yRange.first >= -50 && it.yRange.last <= 50 }
        .filter { it.zRange.first >= -50 && it.zRange.last <= 50 }
        .forEach {
            for (x in it.xRange) {
                for (y in it.yRange) {
                    for (z in it.zRange) {
                        if (it.isOn) {
                            map[Coord(x, y, z)] = true
                        } else {
                            map.remove(Coord(x, y, z))
                        }
                    }
                }
            }
        }
    return map.size
}

fun solve2Task(lines: List<String>): Long {
    val cubeSpace = mutableListOf<Cube>()
    val removedCubeSpace = mutableListOf<Cube>()

    val intersectRange = { a: Range, b: Range -> a.start <= b.end && a.end >= b.start }
    val isOverlapCube = { a: Cube, b: Cube -> intersectRange(a.x, b.x) && intersectRange(a.y, b.y) && intersectRange(a.z, b.z) }
    val interceptRange = { a: Range, b: Range -> Range(max(b.start, a.start), min(b.end, a.end)) }
    val getOverlapCube = { a: Cube, b: Cube -> Cube(interceptRange(a.x, b.x), interceptRange(a.y, b.y), interceptRange(a.z, b.z)) }

    for (instruction in lines.map { Instruction.fromString(it) }) {
        val (on, xRange, yRange, zRange) = instruction
        val current = Cube(xRange, yRange, zRange)
        if (on) {
            val currentOverlap = mutableListOf<Cube>()
            //find overlap with existing cubespace and remove the overlapping area
            for (cube in cubeSpace) {
                if (isOverlapCube(current, cube)) {
                    currentOverlap.add(getOverlapCube(current, cube))
                }
            }
            //find overlap with deleted elements and read them to the cubespace
            for (removed in removedCubeSpace) {
                if (isOverlapCube(current, removed)) {
                    cubeSpace.add(getOverlapCube(current, removed))
                }
            }
            removedCubeSpace.addAll(currentOverlap)
            cubeSpace.add(current)
        } else {
            //remove elements you are currently overlapping with in the cubespace
            val currentRemoveOverlap = mutableListOf<Cube>()
            for (cube in cubeSpace) {
                if (isOverlapCube(current, cube)) {
                    currentRemoveOverlap.add(getOverlapCube(current, cube))
                }
            }
            //if you overlap with an element in the removed space, read that space to prevent double removal
            for (removed in removedCubeSpace) {
                if (isOverlapCube(current, removed)) {
                    cubeSpace.add(getOverlapCube(current, removed))
                }
            }
            //remove overlapping elements from the cubespace
            removedCubeSpace.addAll(currentRemoveOverlap)
        }
    }

    val area = { cube: Cube -> (cube.x.end - cube.x.start + 1).toLong() * (cube.y.end - cube.y.start + 1).toLong() * (cube.z.end - cube.z.start + 1).toLong() }
    var result = 0L
    for (space in cubeSpace) {
        result += area(space)
    }
    for (removed in removedCubeSpace) {
        result -= area(removed)
    }
    return result
}


data class Coord(val x: Int, val y: Int, val z: Int)
data class Procedure(val isOn: Boolean, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {

    companion object {
        fun fromString(input: String): Procedure {
            val isOn = input.substringBefore(" ") == "on"
            val newInput = input.substringAfter(" ")
            val m = PROCEDURE_PATTERN.matcher(newInput)
            require(m.matches()) { "does not match: $newInput" }
            val minX = m.group(1).toInt()
            val maxX = m.group(2).toInt()
            val minY = m.group(3).toInt()
            val maxY = m.group(4).toInt()
            val minZ = m.group(5).toInt()
            val maxZ = m.group(6).toInt()
            return Procedure(isOn, minX..maxX, minY..maxY, minZ..maxZ)
        }
    }
}

data class Range(val start: Int, val end: Int)
data class Cube(val x: Range, val y: Range, val z: Range)
data class Instruction(val isOn: Boolean, val xRange: Range, val yRange: Range, val zRange: Range) {
    companion object {
        fun fromString(input: String): Instruction {
            val isOn = input.substringBefore(" ") == "on"
            val newInput = input.substringAfter(" ")
            val m = PROCEDURE_PATTERN.matcher(newInput)
            require(m.matches()) { "does not match: $newInput" }
            val minX = m.group(1).toInt()
            val maxX = m.group(2).toInt()
            val minY = m.group(3).toInt()
            val maxY = m.group(4).toInt()
            val minZ = m.group(5).toInt()
            val maxZ = m.group(6).toInt()
            return Instruction(isOn, Range(minX, maxX), Range(minY, maxY), Range(minZ, maxZ))
        }
    }
}
