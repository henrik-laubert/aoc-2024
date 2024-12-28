import utils.readInput
import kotlin.math.roundToLong

fun main()  {

  operator fun Pair<Long,Long>.times(n: Long): Pair<Long,Long> = first * n to second * n
  operator fun Pair<Long,Long>.plus(n: Pair<Long,Long>): Pair<Long,Long> = first + n.first to second + n.second
  operator fun Pair<Long,Long>.plus(n: Long): Pair<Long,Long> = first + n to second + n

  fun parseInput(input: List<String>): List<List<Pair<Long, Long>>> {
    val pattern = """=?([-+]?\d+), Y=?([-+]?\d+)""".toRegex()

    return pattern.findAll(input.joinToString("\n"))
      .windowed(3, 3)
      .map { machine ->
        machine.map { it.groupValues[1].toLong() to it.groupValues[2].toLong() }
      }.toList()
  }

  fun computeB(a: Pair<Long, Long>, b: Pair<Long, Long>, x: Pair<Long, Long>): Long =
    ((x.second * a.first - x.first * a.second).toDouble() / (b.second * a.first - b.first * a.second)).roundToLong()

  fun computeA(a: Pair<Long, Long>, b: Pair<Long, Long>, x: Pair<Long, Long>): Long =
    ((x.first - computeB(a, b, x) * b.first).toDouble() / a.first).roundToLong()

  fun part1(input: List<String>): Long {
    val machines = parseInput(input)

    return machines.sumOf {
      val a = computeA(it[0], it[1], it[2])
      val b = computeB(it[0], it[1], it[2])

      if (it[0] * a + it[1] * b == it[2] && a in 0..100 && b in 0.. 100) {
        a * 3 + b
      } else {
        0
      }
    }
  }

  fun part2(input: List<String>): Long {
    val machines = parseInput(input).map {
      listOf(it[0], it[1], it[2] + 10000000000000)
    }

    return machines.sumOf {
      val a = computeA(it[0], it[1], it[2])
      val b = computeB(it[0], it[1], it[2])

      if (it[0] * a + it[1] * b == it[2]) {
        a * 3 + b
      } else {
        0
      }
    }
  }

  val testInput = readInput("Day13_test")
  check(part1(testInput) == 480L)
  //check(part2(testInput) == 1206)

  val input = readInput("Day13")
  part1(input).println()
  part2(input).println()
}

