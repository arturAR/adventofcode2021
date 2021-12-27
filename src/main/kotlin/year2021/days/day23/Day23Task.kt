package year2021.days.day23

import year2021.days.getFile
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


private const val FILENAME = "day23/input.txt"
private const val FILENAME_2 = "day23/input_2.txt"
private const val TEST_FILENAME = "day23/test_input.txt"
private const val TEST_FILENAME_2 = "day23/test_input_2.txt"
val MOVE_COSTS = intArrayOf(1, 10, 100, 1000)
var DEPTH = 0

fun main() {
    val lines = getFile(FILENAME).readLines()
    println(solvePosition(lines))

    val lines2 = getFile(FILENAME_2).readLines()
    println(solvePosition(lines2))
}

fun solvePosition(lines: List<String>): Long {
    val emptySpaces = 7
    DEPTH = lines.size - 3
    val startingPositions = IntArray(totalUnits())
    for (lineIndex in 0 until DEPTH) {
        val line: String = lines[lineIndex + 2]
        for (index in 0..3) {
            val c = line[2 * index + 3]
            var unit = (c - 'A') * DEPTH
            while (startingPositions[unit] != 0) {
                unit++
            }
            startingPositions[unit] = 4 * lineIndex + index + emptySpaces
        }
    }

    val queue = PriorityQueue<GameState>(Comparator.comparingLong { it.cost })
    queue.add(GameState(startingPositions, 0))
    var best = Long.MAX_VALUE
    val alreadyProcessed: MutableMap<String, Long> = mutableMapOf()
    while (!queue.isEmpty()) {
        val toProcess: GameState = queue.poll()
        if (toProcess.cost >= best) {
            break
        }

        for (unit in 0 until totalUnits()) {
            val validPos = findValidPositions(toProcess.positions, unit)
            for (i in validPos.indices) {
                if (!validPos[i]) {
                    continue
                }
                val price = calcPrice(unit, toProcess.positions[unit], i)
                val next = toProcess.moveUnit(unit, i, price)
                if (next.isComplete()) {
                    best = min(best, next.cost)
                } else {
                    val repr = next.getStringRepresentation()
                    if (next.cost < alreadyProcessed.getOrDefault(repr, Long.MAX_VALUE)) {
                        alreadyProcessed[repr] = next.cost
                        queue.add(next)
                    }
                }
            }
        }
    }
    return best
}

private fun findValidPositions(positions: IntArray, unit: Int): BooleanArray {
    return if (positions[unit] < 7) {
        findValidRoomPositions(positions, unit)
    } else {
        findValidHallPositions(positions, unit)
    }
}

private fun findValidRoomPositions(positions: IntArray, unit: Int): BooleanArray {
    val occupied = IntArray(totalUnits() + 7)
    for (i in 0 until totalUnits() + 7) {
        occupied[i] = -1
    }
    for (i in 0 until totalUnits()) {
        occupied[positions[i]] = i
    }
    val rv = BooleanArray(totalUnits() + 7)
    val cPos = positions[unit]
    val type = getType(unit)
    val room1 = type + 7
    if (!checkHallwayClear(cPos, room1, occupied)) {
        return rv
    }
    var tgt = room1
    for (i in 0 until DEPTH) {
        if (occupied[room1 + 4 * i] == -1) {
            tgt = room1 + 4 * i
        } else if (getType(occupied[room1 + 4 * i]) != type) {
            return rv
        }
    }
    rv[tgt] = true
    return rv
}

private fun findValidHallPositions(positions: IntArray, unit: Int): BooleanArray {
    val occupied = IntArray(totalUnits() + 7)
    for (i in 0 until totalUnits() + 7) {
        occupied[i] = -1
    }
    for (i in 0 until totalUnits()) {
        occupied[positions[i]] = i
    }
    val rv = BooleanArray(7)
    val cPos = positions[unit]
    val type = getType(unit)

    var x = cPos - 4

    while (x > 6) {
        if (occupied[x] > -1) {
            return rv
        }
        x -= 4
    }

    if ((cPos + 1) % 4 == type) {
        var gottaMove = false
        var i = cPos + 4
        while (i < totalUnits() + 7) {
            if (getType(occupied[i]) != type) {
                gottaMove = true
                break
            }
            i += 4
        }
        if (!gottaMove) {
            return rv
        }
    }

    var effPos = cPos
    while (effPos > 10) {
        effPos -= 4
    }
    for (i in 0..6) {
        if (occupied[i] == -1 && checkHallwayClear(i, effPos, occupied)) {
            rv[i] = true
        }
    }
    return rv
}

private fun checkHallwayClear(hallPos: Int, roomPos: Int, occupied: IntArray): Boolean {
    val min = min(hallPos + 1, roomPos - 5)
    val max = max(hallPos - 1, roomPos - 6)
    for (i in min..max) {
        if (occupied[i] != -1) {
            return false
        }
    }
    return true
}

private fun totalUnits(): Int {
    return 4 * DEPTH
}

private fun getType(unit: Int): Int {
    return if (unit == -1) -1 else unit / DEPTH
}

private fun calcPrice(unit: Int, from: Int, to: Int): Int {
    val machniom = machniom(from, to)
    val depth = (machniom.second - 3) / 4
    val tgtHall = (machniom.second + 1) % 4 * 2 + 3
    val discount = if (machniom.first == 0 || machniom.first == 6) 1 else 0
    val dist = abs(2 * machniom.first - tgtHall) + depth - discount
    val type = getType(unit)
    return MOVE_COSTS[type] * dist
}

private fun machniom(from: Int, to: Int): Pair<Int, Int> {
    return if(from > to) Pair(to, from) else Pair(from, to)
}

class GameState(val positions: IntArray, val cost: Long) {
    fun moveUnit(unit: Int, position: Int, price: Int): GameState {
        val newPositions = positions.copyOf(positions.size)
        newPositions[unit] = position
        return GameState(newPositions, cost + price)
    }

    fun isComplete(): Boolean {
        for (i in positions.indices) {
            val type = getType(i)
            if (positions[i] < 7 || (positions[i] + 1) % 4 != type) {
                return false
            }
        }
        return true
    }

    fun getStringRepresentation(): String {
        val occupied = IntArray(totalUnits() + 7)
        for (i in 0 until totalUnits() + 7) {
            occupied[i] = -1
        }
        for (i in 0 until totalUnits()) {
            occupied[positions[i]] = i
        }
        var rv = ""
        for (i in 0 until totalUnits() + 7) {
            val type = getType(occupied[i])
            if (type == -1) {
                rv += "x"
            } else {
                rv += type
            }
        }
        return rv
    }

    override fun toString(): String {
        return "GameState(positions=${positions.contentToString()}, cost=$cost)"
    }
}
