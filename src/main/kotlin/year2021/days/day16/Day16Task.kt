package year2021.days.day16

import year2021.days.getFile
import java.math.BigInteger

private val HET_TO_BINARY = mapOf(
    "0" to "0000",
    "1" to "0001",
    "2" to "0010",
    "3" to "0011",
    "4" to "0100",
    "5" to "0101",
    "6" to "0110",
    "7" to "0111",
    "8" to "1000",
    "9" to "1001",
    "A" to "1010",
    "B" to "1011",
    "C" to "1100",
    "D" to "1101",
    "E" to "1110",
    "F" to "1111"
)

private const val FILENAME = "day16/input.txt"
private const val TEST_FILENAME = "day16/test_input.txt"
private const val TEST_FILENAME_1 = "day16/test_input_1.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines[0]))
    println(solve2Task(lines[0]))
}

fun solve1Task(input: String): Long {
    val bin = hexToBin(input)
    return findPackets(bin)
}

fun solve2Task(input: String): Long {
    val bin = hexToBin(input)
    val res = findPackets2(bin, Int.MAX_VALUE)
    println(res)
    return res[0]
}

fun hexToBin(input: String): String {
    return input.map { HET_TO_BINARY[it.toString()] }.joinToString("")
}

fun findPackets(binaryInput: String): Long {
    var sum = 0L
    var i = 0
    while (i < binaryInput.length) {
        if (binaryInput.substring(i).all { e -> e == '0' }) {
            break
        }
        val version: Int = binToDec(binaryInput.substring(i, i + 3))
        sum += version.toLong()
        val id: Int = binToDec(binaryInput.substring(i + 3, i + 6))
        i += 6
        if (id == 4) {
            while (true) {
                if (binaryInput[i] == '0') {
                    i += 5
                    break
                }
                i += 5
            }
        } else {
            var lengthLength = 15
            val b = binaryInput[i] == '1'
            if (b) {
                lengthLength = 11
            }
            i++
            val length: Int = binToDec(binaryInput.substring(i, i + lengthLength))
            i += lengthLength
            if (!b) {
                sum += findPackets(binaryInput.substring(i, i + length))
                i += length
            }
        }
    }
    return sum
}

fun binToDec(s: String): Int {
    return BigInteger(s, 2).toString(10).toInt()
}

fun binToLongDec(s: String?): Long {
    return BigInteger(s, 2).toString(10).toLong()
}

var prevI = 0
fun findPackets2(binaryInput: String, toParse: Int): List<Long> {
    val res: MutableList<Long> = ArrayList()
    var i = 0
    var parsed = 0
    while (i < binaryInput.length) {
        if (parsed >= toParse) {
            break
        }
        if (binaryInput.substring(i).all { e -> e == '0' }) {
            break
        }
        val id = binToDec(binaryInput.substring(i + 3, i + 6))
        i += 6
        if (id == 4) {
            var s = ""
            while (true) {
                s += binaryInput.substring(i + 1, i + 5)
                if (binaryInput[i] == '0') {
                    i += 5
                    break
                }
                i += 5
            }
            res.add(binToLongDec(s))
        } else {
            var lengthLength = 15
            val b = binaryInput[i] == '1'
            if (b) {
                lengthLength = 11
            }
            i++
            val length = binToDec(binaryInput.substring(i, i + lengthLength))
            i += lengthLength
            val op = findPackets2(binaryInput.substring(i, if (b) binaryInput.length else i + length), if (b) length else Int.MAX_VALUE)
            res.add(performOp(op, id))
            i += if (b) prevI else length
        }
        prevI = i
        parsed++
    }
    return res
}

fun performOp(op: List<Long>, id: Int): Long {
    return when(id) {
        0 -> op.sum()
        1 -> op.reduce { a, b -> a * b }
        2 -> op.minOrNull()!!
        3 -> op.maxOrNull()!!
        5 -> if(op[0] > op[1]) 1L else 0L;
        6 -> if(op[0] < op[1]) 1L else 0L;
        7 -> if(op[0] == op[1]) 1L else 0L;
        else -> throw IllegalStateException("Not known id: "+id);
    };
}