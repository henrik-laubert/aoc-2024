import kotlin.math.abs

fun main() {
  fun castInput(input: List<String>): Pair<List<Int>, List<Int>> {
    return (if (input.last().isBlank()) input.dropLast(1) else input )
      .map {
        val values = it.split("   ").map(String::toInt)
        values[0] to values[1]
      }.unzip()
  }

  fun part1(input: List<String>): Int {
    val numbers = castInput(input)

    return numbers.first.sorted()
      .zip(numbers.second.sorted())
      .sumOf { abs(it.first - it.second) }
  }

  fun part2(input: List<String>): Int {
    val numbers = castInput(input)
    val count = numbers.second.groupingBy { it }.eachCount()

    return numbers.first.sumOf { it * (count[it] ?: 0) }
  }

  val testInput = readInput("Day01_test")
  check(part1(testInput) == 11)
  check(part2(testInput) == 31)

  val input = readInput("Day01")
  part1(input).println()
  part2(input).println()
}
