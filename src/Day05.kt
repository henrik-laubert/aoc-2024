import kotlin.math.floor

fun main() {

  fun parseInput(input: List<String>): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
    val rules = input.subList(0, input.indexOf(""))
      .map { it.split("|").let { (first, second) -> first.toInt() to second.toInt() } }
    val updates = input.subList(input.indexOf("") + 1, input.size)
      .map { it.split(",").map(String::toInt) }

    return Pair(rules, updates)
  }

  fun updateIsValid(update: List<Int>, rules: List<Pair<Int, Int>>): Boolean {
    return update.all { page ->
      rules.filter { it.first == page && it.second in update }
        .all { rule ->
          update.indexOf(page) < update.indexOf(rule.second)
        }
    }
  }

  fun fixUpdate(update: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
    var temp = update

    temp.forEachIndexed { index, page ->
      val minIndex = rules
        .filter { (first, second) -> first == page && second in temp && temp.indexOf(page) > temp.indexOf(second) }
        .ifEmpty { return@forEachIndexed }
        .minOf { temp.indexOf(it.second) }

      val p1 = temp.subList(0, minIndex)
      val p2 = temp.subList(minIndex, index)
      val p3 = temp.subList(index + 1, temp.size)

      temp = p1 + page + p2 + p3
    }

    return temp
  }

  fun part1(input: List<String>): Int {
    val (rules, updates) = parseInput(input)

    return updates.filter { updateIsValid(it, rules) }
      .sumOf { it[floor(it.size / 2F).toInt()] }
  }

  fun part2(input: List<String>): Int {
    val (rules, updates) = parseInput(input)

    return updates.filterNot { updateIsValid(it, rules) }
      .map { fixUpdate(it, rules) }
      .sumOf { it[floor(it.size / 2F).toInt()] }
  }

  val testInput = readInput("Day05_test")
  check(part1(testInput) == 143)
  check(part2(testInput) == 123)

  val input = readInput("Day05")
  part1(input).println()
  part2(input).println()
}
