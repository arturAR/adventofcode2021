package year2021.days.day13

import year2021.days.getFile

private const val FILENAME = "day13/input.txt"
private const val TEST_FILENAME = "day13/test_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    val map = Array(1400) { BooleanArray(1400) }
    val instructions = mutableListOf<Pair<String, Int>>()
    lines.forEach {
        if(it.startsWith("fold along")) {
            val pair = it.replace("fold along ", "").split("=")
            instructions.add(Pair(pair[0], pair[1].toInt()))
        } else if (it.isNotEmpty()) {
            val coords = it.split(",")
            map[coords[1].toInt()][coords[0].toInt()] = true
        }
    }

    println(solve1Task(map, instructions))
    solve2Task(map, instructions)
}

fun solve1Task(map: Array<BooleanArray>, instructions: List<Pair<String, Int>>): Int {
    val newMap = foldAlong(map, instructions.take(1))
    return newMap.fold(0) { acc, li -> acc + li.filter { it }.size }
}

fun solve2Task(map: Array<BooleanArray>, instructions: List<Pair<String, Int>>) {
    foldAlong(map, instructions).forEachIndexed { down, li ->
        if (down < 6) {
            li.forEachIndexed { across, it ->
                if (across < 40) {
                    if (it) print("#") else print(".")
                }
            }
            println()
        }
    }
}

fun foldAlong(map: Array<BooleanArray>, instructions: List<Pair<String, Int>>): Array<BooleanArray> {
    val newMap = Array(1400) { BooleanArray(1400) }
        .also { map.forEachIndexed { index, line -> it[index] = line.clone() } }

    instructions.forEach { instruction ->
        if(instruction.first == "x") {
            val maxX = instruction.second
            for (y in newMap.indices) {
                for (x in newMap[y].indices) {
                    if (x <= maxX) continue

                    if (newMap[y][x]) {
                        newMap[y][maxX - (x - maxX)] = true
                        newMap[y][x] = false
                    }
                }
            }
        }

        if(instruction.first == "y") {
            val maxY = instruction.second
            for (y in newMap.indices) {
                for (x in newMap[y].indices) {
                    if (y <= maxY) continue

                    if (newMap[y][x]) {
                        newMap[maxY - (y - maxY)][x] = true
                        newMap[y][x] = false
                    }
                }
            }
        }
    }

    return newMap
}