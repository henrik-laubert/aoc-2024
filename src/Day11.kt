
fun main()  {

  fun parseInput(input: List<String>): Map<Long, Long> {
    return """(\d+)""".toRegex().findAll(input.first())
      .map { it.value.toLong() }
      .groupingBy { it }.eachCount()
      .mapValues { it.value.toLong() }
  }

  fun split(number: String): Pair<Long, Long> {
    return Pair(
      number.substring(0, number.length/2).toLong(),
      number.substring(number.length/2, number.length).toLong()
    )
  }

  fun blink(current: Map<Long, Long>): Map<Long, Long> {
    val next = mutableMapOf<Long, Long>()
    current.forEach { (key, count) ->
      if (key == 0L) {
       next[1] = next.getOrDefault(1, 0) + count
      } else if (key.toString().length % 2 == 0 && key.toString().length > 1) {
        val temp = split(key.toString())
        next[temp.first] = next.getOrDefault(temp.first, 0) + count
        next[temp.second] = next.getOrDefault(temp.second, 0) + count
      } else {
       next[key * 2024] = next.getOrDefault(key * 2024, 0) + count
      }
    }
    return next
  }

  fun part1(input: List<String>): Long {
    var stones = parseInput(input)
    repeat(25) {
      stones = blink(stones)
    }
    return stones.values.sum()
  }

  fun part2(input: List<String>): Long {
    var stones = parseInput(input)
    repeat(75) {
      stones = blink(stones)
    }
    return stones.values.sum()
  }

  val testInput = readInput("Day11_test")
  check(part1(testInput) == 55312L)
  //check(part2(testInput) == 81)

  val input = readInput("Day11")
  part1(input).println()
  part2(input).println()
}
