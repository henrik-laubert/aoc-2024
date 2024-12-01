import kotlin.math.abs

fun main() {
  fun castInput(input: List<String>): Pair<List<Int>, List<Int>> {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()

    for (line in input) {
      if (line.isBlank()) continue
      val values = line.split("   ").map(String::toInt)
      list1.add(values[0])
      list2.add(values[1])
    }

    return Pair(list1, list2)
  }

  fun part1(input: List<String>): Int {
    val numbers = castInput(input)
    val temp = numbers.second.sorted()

    return numbers.first.sorted().foldIndexed(0) { index, acc, i ->
      acc + abs(i - temp[index])
    }
  }

  fun part2(input: List<String>): Int {
    val numbers = castInput(input)
    val count = numbers.second.groupingBy { it }.eachCount()

    return numbers.first.fold(0) { acc, i ->
      acc + i * count.getOrDefault(i, 0)
    }
  }

  val testInput = readInput("Day01_test")
  part1(testInput).println()
  part2(testInput).println()
  check(part1(testInput) == 11)
  check(part2(testInput) == 31)

  val input = readInput("Day01").dropLast(1)
  part1(input).println()
  part2(input).println()
}
