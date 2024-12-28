import utils.divideInput
import utils.readInput

fun main() {
  val cache = mutableMapOf<String, Long>()

  fun possibleCombinations(pattern: String, towels: List<String>): Long {
    return cache.getOrPut(pattern) {
      if (pattern.isBlank()) 1
      else towels.filter { pattern.startsWith(it) }.sumOf {
        possibleCombinations(pattern.removePrefix(it), towels)
      }
    }
  }

  fun part1(input: List<String>): Int {
    val (towels, pattern) = input.divideInput { (first, second) ->
     first.first().split(", ") to second
    }

    cache.clear()
    return pattern.count { possibleCombinations(it, towels) > 0 }
  }

  fun part2(input: List<String>): Long {
    val (towels, pattern) = input.divideInput { (first, second) ->
      first.first().split(", ") to second
    }

    cache.clear()
    return pattern.sumOf { possibleCombinations(it, towels) }
  }

  val testInput = readInput("inputs/Day19_test")
  check(part1(testInput) == 6)
  check(part2(testInput) == 16L)

  val input = readInput("inputs/Day19")
  part1(input).println()
  part2(input).println()
}
