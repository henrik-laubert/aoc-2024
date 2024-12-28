import utils.readInput

fun main() {

  fun parseInput(input: List<String>): Pair<List<List<Int>>, List<List<Int>>> {
    val temp = input.toMutableList()

    val keys = mutableListOf<List<Int>>()
    val locks = mutableListOf<List<Int>>()

    val current = mutableListOf<String>()

    while (temp.isNotEmpty()) {
      val line = temp.removeFirst()
      if (line.isBlank()) {
        val parsed = mutableListOf(-1, -1, -1, -1, -1)
        current.forEach {
          it.forEachIndexed { index, c ->
            if (c == '#') parsed[index]++
          }
        }

        if (current.first().all { it == '.' }) keys.add(parsed)
        else locks.add(parsed)

        current.clear()
      }
      else {
        current.add(line)
      }
    }

    val parsed = mutableListOf(-1, -1, -1, -1, -1)
    current.forEach {
      it.forEachIndexed { index, c ->
        if (c == '#') parsed[index]++
      }
    }

    if (current.first().all { it == '.' }) keys.add(parsed)
    else locks.add(parsed)

    return keys to locks
  }

  fun part1(input: List<String>): Int {
    val (keys, locks) = parseInput(input)

    return keys.sumOf { key ->
      locks.count { lock ->
        lock.forEachIndexed { i, it ->
          if(it + key[i] > 5) return@count false
        }
        true
      }
    }
  }

  fun part2(input: List<String>): Int {
    TODO()
  }

  val testInput = readInput("inputs/Day25_test")
  check(part1(testInput) == 3)
  //check(part2(testInput) == 2024L)

  val input = readInput("inputs/Day25")
  part1(input).println()
  part2(input).println()
}
