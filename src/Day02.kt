import kotlin.math.abs

fun main() {
  fun castInput(input: List<String>): List<List<Int>> {
    val temp = if (input.last().isBlank()) input.dropLast(1) else input

    return temp.map {
      it.split(" ").map(String::toInt)
    }
  }

  fun checkReport(report: List<Int>): Boolean {
    val asc = report[0] < report[1]

    for (i in 1..report.lastIndex) {
      if (abs(report[i-1] - report[i]) !in 1..3 || (asc != report[i-1] < report[i])) {
        return false
      }
    }
    return true
  }

  fun part1(input: List<String>): Int {
    val reports = castInput(input)

    return reports.count { checkReport(it) }
  }

  fun part2(input: List<String>): Int {
    val reports = castInput(input)
    var saveCount = 0

    for (report in reports) {
      if (checkReport(report)) saveCount++
      else {
        for (i in report.indices) {
          val temp = report.toMutableList().also { it.removeAt(i) }
          if (checkReport(temp)) {
            saveCount++
            break
          }
        }
      }
    }

    return saveCount
  }

  val testInput = readInput("Day02_test")
  check(part1(testInput) == 2)
  check(part2(testInput) == 4)

  val input = readInput("Day02")
  part1(input).println()
  part2(input).println()
}
