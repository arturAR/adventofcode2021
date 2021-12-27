package year2021.days.day04

import year2021.days.getFile

private const val FILENAME = "day04/input.txt"
private const val TEST_FILENAME = "day04/test_input.txt"

fun main() {
    val input = getFile(FILENAME).readLines().filter { it.trimIndent().isNotBlank() }
    val numbers = input[0].split(",").map { it.toInt() }
    val boards = input
        .asSequence()
        .drop(1)
        .map { line -> line.split(" ").filter { it.isNotBlank() }.map { it.toInt() } }
        .map { line -> Row(line.map { Tile(it) }) }
        .chunked(5)
        .map { Board(it) }
        .toList()
    val result1 = FirstTask(boards, numbers).play()
    val result2 = SecondTask(boards, numbers).play()
    println(result1)
    println(result2)
}

class FirstTask(private val boards: List<Board>, private val numbers: List<Int>) {
    fun play(): Int {
        for (number in numbers) {
            boards.forEach { it.mark(number) }
            boards.map { it.score(number) }
                .firstOrNull { it > 0 }
                ?.let { return it }
        }
        return -1
    }
}

class SecondTask(private val boards: List<Board>, private val numbers: List<Int>) {
    fun play(): Int {
        for (number in numbers) {
            boards.forEach { it.mark(number) }
            boards
                .filter { !it.winner }
                .forEach {
                    val score = it.score(number)
                    if (score > 0) {
                        it.winner = true
                        if (boards.all { board -> board.winner }) {
                            return score
                        }
                    }
                }
        }
        return -1
    }
}

class Board(private val rows: List<Row>, var winner: Boolean = false) {
    fun mark(number: Int) {
        rows.forEach { it.mark(number) }
    }

    fun score(number: Int): Int {
        rows.firstOrNull { it.isFilled() }
            ?.let { return (sumUnmarked()) * number }
        toColumns(rows).firstOrNull { it.isFilled() }
            ?.let { return (sumUnmarked()) * number }
        return 0
    }

    fun sumUnmarked(): Int {
        return rows.sumOf { it.sumUnmarked() }
    }

    fun toColumns(rows: List<Row>): List<Row> {
        val columns = mutableListOf<Row>()
        if (rows.isEmpty()) return columns
        for (i in 0 until 5) {
            val tiles = mutableListOf<Tile>()
            for (j in 0 until 5) {
                tiles.add(rows[j].tiles[i])
            }
            columns.add(Row(tiles))
        }
        return columns
    }
}

class Row(val tiles: List<Tile>) {
    fun mark(number: Int) {
        tiles.filter { it.value == number }.forEach { it.mark() }
    }

    fun isFilled() = tiles.all { it.marked }

    fun sumUnmarked(): Int {
        return tiles.filter { !it.marked }
            .sumOf { it.value }
    }
}

class Tile(val value: Int, var marked: Boolean = false) {
    fun mark() {
        marked = true
    }
}

