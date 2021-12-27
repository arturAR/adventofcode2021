package year2021.days.day16

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day16Test {

    @Test
    fun testFirstTaskSolution() {
        assertEquals(16, solve1Task("8A004A801A8002F478"))
        assertEquals(12, solve1Task("620080001611562C8802118E34"))
        assertEquals(23, solve1Task("C0015000016115A2E0802F182340"))
        assertEquals(31, solve1Task("A0016C880162017C3686B18A3D4780"))
    }

    @Test
    fun testSecondTaskSolution() {
        assertEquals(3, solve2Task("C200B40A82"))
        assertEquals(54, solve2Task("04005AC33890"))
        assertEquals(7, solve2Task("880086C3E88112"))
        assertEquals(9, solve2Task("CE00C43D881120"))
        assertEquals(1, solve2Task("D8005AC2A8F0"))
        assertEquals(0, solve2Task("F600BC2D8F"))
        assertEquals(0, solve2Task("9C005AC2F8F0"))
        assertEquals(1, solve2Task("9C0141080250320F1802104A08"))
    }
}