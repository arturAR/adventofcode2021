package year2021.days.day20

import year2021.days.getFile

private const val FILENAME = "day20/input.txt"
private const val TEST_FILENAME = "day20/test_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

fun solve1Task(lines: List<String>): Int {
    return sequence(lines).drop(2).first().sumOf {
        it.count { it == '#' }
    }
}

fun solve2Task(lines: List<String>): Int {
    return sequence(lines).drop(50).first().sumOf {
        it.count { it == '#' }
    }
}

fun sequence(lines: List<String>): Sequence<List<String>> {
    val alg = lines.first()
    return generateSequence(lines.drop(2) to '.') { (image, fill) ->
        List(image.size + 2) { y ->
            val line1 = image.getOrNull(y - 2)
            val line2 = image.getOrNull(y - 1)
            val line3 = image.getOrNull(y)
            CharArray(image[y.coerceIn(image.indices)].length + 2) { x ->
                lookup(
                    alg,
                    getValue(line1, x - 2, fill),
                    getValue(line1, x - 1, fill),
                    getValue(line1, x, fill),
                    getValue(line2, x - 2, fill),
                    getValue(line2, x - 1, fill),
                    getValue(line2, x, fill),
                    getValue(line3, x - 2, fill),
                    getValue(line3, x - 1, fill),
                    getValue(line3, x, fill)
                )
            }.concatToString()
        } to lookup(alg, fill, fill, fill, fill, fill, fill, fill, fill, fill)
    }.map { it.first }
}

fun lookup(alg: String, vararg key: Char): Char {
    return alg[key.fold(0) { acc, c -> 2 * acc + c.code.and(1) }]
}

fun getValue(line: String?, x: Int, fill: Char): Char {
    return line?.getOrNull(x) ?: fill
}