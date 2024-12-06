fun main() {

  operator fun Pair<Int,Int>.plus(other: Pair<Int, Int>): Pair<Int,Int> {
    return first + other.first to second + other.second
  }

  fun parseInput(input: List<String>): List<List<String>> {
    return input.map { it.split("").filterNot { char -> char.isBlank() } }
  }

  fun getObstacles(input: List<List<String>>): List<Pair<Int,Int>>{
    val obstacles = mutableListOf<Pair<Int,Int>>()

    input.forEachIndexed { row, str ->
      str.forEachIndexed { col, value ->
        if (value == "#") {
          obstacles.add(col to row)
        }
      }
    }

    return obstacles
  }

  fun getStartPos(input: List<List<String>>): Pair<Int,Int> {
    val row = input.indexOfFirst { "^" in it }
    return input[row].indexOf("^") to row
  }

  fun computePath(input: List<List<String>>): List<Pair<Pair<Int,Int>,Pair<Int,Int>>> {
    val obstacles = getObstacles(input)

    var current = getStartPos(input)
    var direction = 0 to -1

    val visitedWithDirection = mutableListOf(current to direction)

    while ((current + direction).second in input.indices && (current + direction).first in 0 .. input.first().lastIndex) {
      if((current + direction) in obstacles) {
        direction = direction.copy(first = -direction.second, second = direction.first)
      } else {
        current += direction
        if ((current to direction) in visitedWithDirection) throw Exception("is Loop")
        visitedWithDirection.add(current to direction)
      }
    }

    return visitedWithDirection
  }

  fun part1(input: List<List<String>>): Int {
    return computePath(input).map { it.first }.toSet().size
  }

  fun part2(input: List<List<String>>): Int {
    val path = computePath(input)
    val newObstacles = mutableSetOf<Pair<Int,Int>>()
    path.drop(1).map { it.first }.toSet().forEachIndexed { index, (col, row) ->
      try {
        val newInput = input.map { it.toMutableList() }
        if(newInput[row][col] != "^") {
          newInput[row][col] = "#"
          computePath(newInput)
        }
      } catch (e: Exception){
        newObstacles.add(col to row)
      }
    }

    return newObstacles.size
  }

  val testInput = parseInput(readInput("Day06_test"))
  check(part1(testInput) == 41)
  check(part2(testInput) == 6)

  val input = parseInput(readInput("Day06"))
  part1(input).println()
  part2(input).println()
}
