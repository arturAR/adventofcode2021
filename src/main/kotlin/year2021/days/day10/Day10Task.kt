package year2021.days.day10

import year2021.days.getFile

private const val FILENAME = "day10/input.txt"
private const val TEST_FILENAME = "day10/test_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

fun solve1Task(lines: List<String>): Int {
    val scoreMap = mapOf(")" to 3, "]" to 57, "}" to 1197, ">" to 25137)
    return lines.sumOf {
        val invalidChar = checkInvalid(it)
        (scoreMap[invalidChar] ?: 0)
    }
}

fun solve2Task(lines: List<String>): Long {
    val scoreMap = mapOf<String, Long>("(" to 1, "[" to 2, "{" to 3, "<" to 4)
    val listOfScores = lines.map {
        checkIncomplete(it).reversed()
            .fold(0L) { score, brace -> ((5 * score) + (scoreMap[brace] ?: 0)) }
    }.filterNot { it == 0L }
        .sorted()

    return listOfScores[listOfScores.size / 2]
}

fun checkInvalid(line: String): String {
    val stack = Stack()
    line.forEach {
        if (isOpeningBrace(it.toString())) {
            stack.push(it.toString())
        } else if (isClosingBrace(it.toString())) {
            val poppedOpener = stack.pop()
            if (isNotMatch(poppedOpener, it.toString())) {
                return it.toString()
            }
        }
    }
    return ""
}

fun checkIncomplete(line: String): List<String> {
    val stack = Stack()
    line.forEach {
        if (isOpeningBrace(it.toString())) {
            stack.push(it.toString())
        } else if (isClosingBrace(it.toString())) {
            val poppedOpener = stack.pop()
            if (isNotMatch(poppedOpener, it.toString())) {
                return listOf()
            }
        }
    }

    if (stack.read() != "") return stack.list
    return listOf()
}

fun isOpeningBrace(string: String): Boolean {
    return setOf("{", "[", "<", "(").contains(string)
}

fun isClosingBrace(string: String): Boolean {
    return setOf("}", "]", ">", ")").contains(string)
}

fun isNotMatch(openingBrace: String, closingBrace: String): Boolean {
    return closingBrace != (mapOf("(" to ")", "[" to "]", "{" to "}", "<" to ">")[openingBrace])
}

class Stack(val list: MutableList<String> = mutableListOf()) {
    fun read(): String {
        if (list.isEmpty()) return ""
        return list.last()
    }

    fun push(string: String) {
        list.add(string)
    }

    fun pop(): String {
        if (list.isEmpty()) return ""
        return list.removeLast()
    }
}