import utils.parseDigits
import utils.readInput
import kotlin.math.abs

fun main() {

  fun checkReport(report: List<Int>): Boolean {
    val asc = report[0] < report[1]

    return report.zipWithNext().all {
      abs(it.first - it.second) in 1..3 && asc == it.first < it.second
    }
  }

  fun part1(input: List<String>): Int {
    val reports = input.parseDigits()

    return reports.count { checkReport(it) }
  }

  fun part2(input: List<String>): Int {
    val reports = input.parseDigits()

    return reports.count {
      checkReport(it) || it.indices.any { i ->
        checkReport(it.toMutableList().apply { removeAt(i) })
      }
    }
  }

  val testInput = readInput("inputs/Day02_test")
  check(part1(testInput) == 2)
  check(part2(testInput) == 4)

  val input = readInput("inputs/Day02")
  part1(input).println()
  part2(input).println()
}
