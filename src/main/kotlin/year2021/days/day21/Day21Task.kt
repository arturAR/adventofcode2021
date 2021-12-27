package year2021.days.day21

import year2021.days.getFile
import kotlin.math.max
import kotlin.math.min

private const val FILENAME = "day21/input.txt"
private const val TEST_FILENAME = "day21/test_input.txt"

fun main() {
    val lines = getFile(TEST_FILENAME).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

fun solve1Task(lines: List<String>): Int {
    var player1Position = lines[0].substringAfter(':').trim().toInt()
    var player2Position = lines[1].substringAfter(':').trim().toInt()
    var resultPlayer1 = 0
    var resultPlayer2 = 0

    var round = 0
    var rolls = 0

    while (resultPlayer1 <= 1000 || resultPlayer2 <= 1000) {
        player1Position += ++round
        round %= 100

        player1Position += ++round
        round %= 100

        player1Position += ++round
        round %= 100

        player1Position--
        player1Position %= 10
        player1Position++

        rolls += 3
        resultPlayer1 += player1Position
        if (resultPlayer1 >= 1000) break


        player2Position += ++round
        round %= 100

        player2Position += ++round
        round %= 100

        player2Position += ++round
        round %= 100

        player2Position--
        player2Position %= 10
        player2Position++

        rolls += 3
        resultPlayer2 += player2Position
        if (resultPlayer2 >= 1000) break
    }
    return min(resultPlayer1, resultPlayer2) * rolls
}

fun solve2Task(lines: List<String>): Long {
    val MULTS = intArrayOf(1, 3, 6, 7, 6, 3, 1)
    val p1pos = lines[0].substringAfter(':').trim().toInt()
    val p2pos = lines[1].substringAfter(':').trim().toInt()

    // Part 2
    var qps = mutableMapOf<GameState, Long>()
    val start = GameState(p1pos, 0, p2pos, 0)
    qps[start] = 1L

    var p1Wins: Long = 0
    var p2Wins: Long = 0

    var pturn = 1

    while (qps.isNotEmpty()) {
        val nextQps = mutableMapOf<GameState, Long>()
        for ((key, value) in qps) {
            for (i in 0..6) {
                val dist = i + 3
                val mult = MULTS[i]
                val next = key.roll(pturn, dist)
                if (next.isDone()) {
                    if (pturn == 1) {
                        p1Wins += value * mult
                    } else {
                        p2Wins += value * mult
                    }
                } else {
                    nextQps.merge(next, value * mult) { o, n -> o + n }
                }
            }
        }
        pturn = 3 - pturn
        qps = nextQps
    }

    return max(p1Wins, p2Wins)
}

private class GameState(val p1Pos: Int, val p1Score: Int, val p2Pos: Int, val p2Score: Int) {
    fun roll(player: Int, dist: Int): GameState {
        return if (player == 1) {
            val newPos: Int = (p1Pos + dist - 1) % 10 + 1
            GameState(newPos, p1Score + newPos, p2Pos, p2Score)
        } else {
            val newPos: Int = (p2Pos + dist - 1) % 10 + 1
            GameState(p1Pos, p1Score, newPos, p2Score + newPos)
        }
    }

    fun isDone(): Boolean = p1Score >= 21 || p2Score >= 21
}