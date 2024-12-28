import utils.readInput

fun main() {

  fun parseInput(input: List<String>): List<List<Int>> {
    return input.map { it.split("").filter(String::isNotBlank).map(String::toInt) }
  }

  fun getStartingPoints(input: List<List<Int>>): List<Coordinates> {
    val startingPoints = mutableListOf<Coordinates>()
    input.forEachIndexed { i, row ->
      row.forEachIndexed { j, it ->
        if (it == 0) startingPoints.add(Coordinates(i, j))
      }
    }
    return startingPoints
  }

  fun getElementAt(input: List<List<Int>>, pos: Coordinates): Int? {
    return input.getOrNull(pos.x)?.getOrNull(pos.y)
  }

  fun traverse(input: List<List<Int>>, thisRef: Coordinates): List<Coordinates> {
    val current = getElementAt(input, thisRef)
    if (current == 9) return listOf(thisRef)

    val result = mutableListOf<Coordinates>()

    val neighbors = listOf(
      thisRef.copy(x = thisRef.x - 1), // up
      thisRef.copy(x = thisRef.x + 1), // down
      thisRef.copy(y = thisRef.y - 1), // left
      thisRef.copy(y = thisRef.y + 1)  // right
    )

    neighbors.forEach { neighbor ->
      getElementAt(input, neighbor)?.let {
        if (it == current!! + 1) {
          result += traverse(input, neighbor)
        }
      }
    }

    return result
  }

  fun part1(input: List<String>): Int {
    val temp = parseInput(input)
    val start = getStartingPoints(temp)

    return start.sumOf {
      traverse(temp, it).toSet().size
    }
  }

  fun part2(input: List<String>): Int {
    val temp = parseInput(input)
    val start = getStartingPoints(temp)

    return start.sumOf {
      traverse(temp, it).size
    }
  }

  val testInput = readInput("Day10_test")
  check(part1(testInput) == 36)
  check(part2(testInput) == 81)

  val input = readInput("Day10")
  part1(input).println()
  part2(input).println()
}
