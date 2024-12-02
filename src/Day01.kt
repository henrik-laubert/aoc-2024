import kotlin.math.abs

fun main() {
  fun castInput(input: List<String>): Pair<List<Int>, List<Int>> {
    val temp = if (input.last().isBlank()) input.dropLast(1) else input

    return temp.map {
      it.split("   ").map(String::toInt)
        .let { (first, second) ->
          first to second
        }
    }.unzip()
  }

  fun part1(input: List<String>): Int {
    val (first, second) = castInput(input)

    return first.sorted()
      .zip(second.sorted())
      .sumOf { abs(it.first - it.second) }
  }

  fun part2(input: List<String>): Int {
    val (first, second) = castInput(input)
    val count = second.groupingBy { it }.eachCount()

    return first.sumOf { it * count.getOrDefault(it, 0) }
  }

  val testInput = readInput("Day01_test")
  check(part1(testInput) == 11)
  check(part2(testInput) == 31)

  val input = readInput("Day01")
  part1(input).println()
  part2(input).println()
}
