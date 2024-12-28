import utils.getCoordinatesOf
import utils.getCoordinatesOfFirst
import utils.gridSize
import utils.readInput

fun main() {

  fun computePath(
    start: Coordinates,
    obstacles: List<Coordinates>,
    gridSize: Pair<Int, Int>,
    direction: Direction = Direction.UP
  ): List<Pair<Coordinates,Direction>> {
    var current = start
    var currentDirection = direction
    val visitedWithDirection = mutableListOf(start to direction)

    while ((current + currentDirection).inBounds(gridSize)) {
      if((current + currentDirection) in obstacles) {
        currentDirection = currentDirection.rotate()
      } else {
        current += currentDirection
        if ((current to currentDirection) in visitedWithDirection) throw Exception("is Loop")
        visitedWithDirection.add(current to currentDirection)
      }
    }

    return visitedWithDirection
  }

  fun part1(input: List<String>): Int {
    val obstacles = input.getCoordinatesOf { it == '#' }.map{ it.toCoordinates() }
    val start = input.getCoordinatesOfFirst('^').toCoordinates()

    return computePath(start, obstacles, input.gridSize())
      .map { it.first }.toSet().size
  }

  fun part2(input: List<String>): Int {

    val obstacles = input.getCoordinatesOf { it == '#' }.map{ it.toCoordinates() }
    val start = input.getCoordinatesOfFirst('^').toCoordinates()

    val path = computePath(start, obstacles, input.gridSize())

    val newObstacles = mutableSetOf<Coordinates>()
    path.forEachIndexed { index, (pos, dir) ->
      if (index != 0) {
        try {
          computePath(
            path[index-1].first,
            obstacles + pos,
            input.size to input.first().length,
             path[index-1].second
          )
        } catch (e: Exception){
          newObstacles.add(pos)
        }
      }
    }

    return newObstacles.size
  }

  val testInput = readInput("inputs/Day06_test")
  check(part1(testInput) == 41)
  check(part2(testInput) == 6)

  val input = readInput("inputs/Day06")
  part1(input).println()
  part2(input).println()
}
