import utils.readInput

fun main() {

  fun parseInput(input: List<String>): List<List<String>> {
    return input.map {
      """(\d+)""".toRegex().findAll(it)
        .flatMap { it.destructured.toList() }.toList()
    }
  }

  fun List<String>.partition(): List<List<List<String>>> {
    if (this.isEmpty()) return listOf(emptyList())

    val result = mutableListOf<List<List<String>>>()

    for (i in 1..this.size) {
      val head = this.subList(0, i)
      val tailPartitions = this.subList(i, this.size).partition()

      for (tail in tailPartitions) {
        result.add(listOf(head) + tail)
      }
    }

    return result
  }

  fun getConcatenatedPartitions(input: List<String>): List<List<String>> {
    return input.partition().map { it.map { it.joinToString(",|,") } }
  }

  fun termFromPartition(partition: List<List<String>>): List<String> {
    return partition.joinToString(",+,") { it.joinToString(",*,") }.split(",")
  }

  fun evalTerm(term: List<String>): Long {
    var temp = term[0].toLong()
    for (i in 1 until term.size step 2) {
      when (term[i]) {
        "+" -> temp += term[i + 1].toLong()
        "*" -> temp *= term[i + 1].toLong()
        "|" -> temp = (temp.toString() + term[i + 1]).toLong()
      }
    }
    return temp
  }

  fun part1(input: List<String>): Long {
    return parseInput(input).filter { equation ->
      equation.drop(1).partition().map(::termFromPartition).any { term ->
        evalTerm(term) == equation.first().toLong()
      }
    }.sumOf { it.first().toLong() }
  }

  fun part2(input: List<String>): Long {
    return parseInput(input).filter { eq ->
      getConcatenatedPartitions(eq.drop(1)).any {
        it.partition().map(::termFromPartition).any { term ->
          evalTerm(term) == eq.first().toLong()
        }}
    }.sumOf { it.first().toLong() }
  }

  val testInput = readInput("Day07_test")
  check(part1(testInput) == 3749L)
  check(part2(testInput) == 11387L)

  val input = readInput("Day07")
  part1(input).println()
  part2(input).println()
}
