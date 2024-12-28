import utils.parseDigitPairs
import utils.readInput
import kotlin.math.abs

fun main() {
  fun part1(input: List<String>): Int {
    val (first, second) = input.parseDigitPairs().unzip()

    return first.sorted()
      .zip(second.sorted())
      .sumOf { abs(it.first - it.second) }
  }

  fun part2(input: List<String>): Int {
    val (first, second) = input.parseDigitPairs().unzip()
    val count = second.groupingBy { it }.eachCount()

    return first.sumOf { it * count.getOrDefault(it, 0) }
  }

  val testInput = readInput("inputs/Day01_test")
  check(part1(testInput) == 11)
  check(part2(testInput) == 31)

  val input = readInput("inputs/Day01")
  part1(input).println()
  part2(input).println()
}
