package year2021.days.day19

import year2021.days.getFile
import java.util.*
import kotlin.math.abs


private const val FILENAME = "day19/input.txt"
private const val TEST_FILENAME = "day19/test_input.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

fun solve1Task(lines: List<String>): Int {
    val scanners = readScanners(lines).toMutableList()
    val base = createScannerNetwork(scanners).first
    return base.signals.size
}

fun solve2Task(lines: List<String>): Int {
    val scanners = readScanners(lines).toMutableList()
    val scannersCoords = createScannerNetwork(scanners).second

    var max = 0
    for (i in 0 until scannersCoords.size) {
        for (j in 0 until scannersCoords.size) {
            val md: Int = scannersCoords[i].getDistance(scannersCoords[j])
            if (md > max) max = md
        }
    }
    return max
}

fun readScanners(lines: List<String>): List<Scanner> {
    return buildList {
        var scannerId = 0
        var scanner = Scanner(scannerId.toString(), mutableListOf())
        for (line in lines) {
            if (line.isEmpty()) continue
            if (line.startsWith("---")) {
                scanner = Scanner(scannerId.toString(), mutableListOf())
                this.add(scanner)
                scannerId++
                continue
            }
            val (x, y, z) = line.split(",").map { it.toInt() }
            scanner.addSignal(x, y, z)
        }
    }
}

fun createScannerNetwork(scanners: MutableList<Scanner>): Pair<Scanner, MutableList<Signal>> {
    val base = scanners.removeAt(0)

    val scannersCoords = mutableListOf(Signal(0, 0, 0))
    while (scanners.size > 0) {
        outer@ for (sc in scanners) {
            for (t in sc.getAllRotations()) {
                val trans: Signal? = base.findTranslation(t)
                if (trans != null) {
                    scannersCoords.add(trans)
                    base.add(t, trans)
                    scanners.remove(sc)
                    break@outer
                }
            }
        }
    }

    return Pair(base, scannersCoords)
}

data class Signal(val x: Int, val y: Int, val z: Int) : Comparable<Signal> {

    fun getDistance(other: Signal): Int {
        return abs(other.x - x) + abs(other.y - y) + abs(other.z - z);
    }

    fun sum(other: Signal) = Signal(x + other.x, y + other.y, z + other.z)
    fun subtract(other: Signal) = Signal(x - other.x, y - other.y, z - other.z)

    override fun compareTo(other: Signal): Int {
        return Arrays.compare(intArrayOf(x, y, z), intArrayOf(other.x, other.y, other.z))
    }

    override fun toString(): String {
        return "\nSignal(x=$x, y=$y, z=$z)"
    }

    companion object {
        // https://stackoverflow.com/questions/16452383/how-to-get-all-24-rotations-of-a-3-dimensional-array
        fun transformations(signal: Signal): List<Signal> {
            var newSignal: Signal = signal
            val transformations: MutableList<Signal> = ArrayList<Signal>()
            for (c in 0..1) {
                for (s in 0..2) {
                    newSignal = roll(newSignal)
                    transformations.add(newSignal)
                    for (i in 0..2) {
                        newSignal = turn(newSignal)
                        transformations.add(newSignal)
                    }
                }
                newSignal = roll(turn(roll(newSignal)))
            }
            return transformations
        }

        private fun roll(c: Signal): Signal {
            return Signal(c.x, c.z, -c.y)
        }

        private fun turn(c: Signal): Signal {
            return Signal(-c.y, c.x, c.z)
        }
    }
}

class Scanner(val scannerId: String, val signals: MutableList<Signal>) {

    fun addSignal(x: Int, y: Int, z: Int) {
        val newSignal = Signal(x, y, z)
        signals.add(newSignal)
    }

    fun findTranslation(peer: Scanner): Signal? {
        val map = mutableMapOf<Signal, Int>()
        for (c in signals) {
            for (d in peer.signals) {
                val diff: Signal = c.subtract(d)
                map.merge(diff, 1) { a: Int, b: Int -> a + b }
            }
        }
        val r = map.entries.filter { it.value >= 12 }.toList()
        if (r.isEmpty()) return null
        if (r.size > 1) {
            println(r)
            throw IllegalStateException("multiple matches")
        }
        return r[0].key
    }

    fun getAllRotations(): List<Scanner> {
        val rotations = mutableListOf(mutableListOf<Signal>())
        for (i in 0..23) rotations.add(mutableListOf())
        for (signal in signals) {
            val transformations: List<Signal> = Signal.transformations(signal)
            for (i in 0..23) rotations[i].add(transformations[i])
        }
        return rotations.map { Scanner(scannerId + "rot", it) }.toList()
    }

    fun add(t: Scanner) {
        for (c in t.signals) {
            if (!signals.contains(c)) {
                signals.add(c)
            }
        }
    }

    fun add(t: Scanner, trans: Signal) {
        for (c in t.signals) {
            val new = c.sum(trans)
            if (!signals.contains(new)) {
                signals.add(new)
            }
        }
    }

    override fun toString(): String {
        return "\n\nScanner $scannerId(\n${signals})"
    }
}