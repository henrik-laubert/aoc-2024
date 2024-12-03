fun main() {

  fun computeMul(string: String): Int {
    val mulRegex = """mul\((\d+),(\d+)\)""".toRegex()

    return mulRegex.findAll(string).sumOf {
      it.groupValues[1].toInt() * it.groupValues[2].toInt()
    }
  }

  fun part1(input: List<String>): Int {
    val memory = input.joinToString(" ")

    return computeMul(memory)
  }

  fun part2(input: List<String>): Int {
    val memory = input.joinToString(" ")
    val executeRegex = """(?>^|do\(\))((?:(?!don't\(\)).)*)(?>don't\(\)|$)""".toRegex()

    return executeRegex.findAll(memory).sumOf {
      computeMul(it.groupValues[1])
    }
  }

  val testInput = readInput("Day03_test")
  check(part1(testInput) == 161)
  check(part2(readInput("Day03_test_pt2")) == 48)

  val input = readInput("Day03")
  part1(input).println()
  part2(input).println()
}
