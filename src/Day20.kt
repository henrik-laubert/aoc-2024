import kotlin.math.abs

fun main() {

  //TODO: extract
  //TODO: define against free tiles not obstacles?
  fun shortestPaths(
    start: Coordinates = Coordinates(0, 0),
    end: Coordinates,
    gridSize: Pair<Int,Int>,
    obstacles: List<Coordinates>
  ): List<List<Coordinates>> {
    val visited = mutableMapOf(start to 0)
    val queue = mutableListOf(listOf(start))
    val results = mutableListOf<List<Coordinates>>()

    while (queue.isNotEmpty()) {
      val current = queue.removeFirst()
      if (current.last() == end) {
        results.add(current)
        continue
      }

      Direction.cardinal.forEach {
        val next = current.last() + it
        val costs = current.size + 1

        if (next !in obstacles && next.inBounds(gridSize) && visited.getOrDefault(next, Int.MAX_VALUE) > costs) {
          queue.add(current + next)
          visited[next] = costs
        }
      }
    }

    return results
  }

  fun Coordinates.distance(other: Coordinates): Int {
    return abs(x - other.x) + abs(y - other.y)
  }

  fun part1(input: List<String>): Int {
    val walls = input.getCoordinatesOf { it == '#' }.map { it.toCoordinates() }
    val start = input.getCoordinatesOfFirst('S').toCoordinates()
    val end = input.getCoordinatesOfFirst('E').toCoordinates()
    val gridSize = input.gridSize()

    val default = shortestPaths(start, end, gridSize, walls).first()

    val shortcuts = default.flatMap { c ->
      default.mapNotNull { end ->
        if (c.distance(end) == 2)
          c to end
        else
          null
      }
    }

    return shortcuts.count { (start, end) ->
      val newCost = default.subList(0, default.indexOf(start) + 1).size + default.subList(default.indexOf(end), default.size).size
      (default.size - 1) - newCost >= 100
    }
  }

  fun part2(input: List<String>): Int {
    val walls = input.getCoordinatesOf { it == '#' }.map { it.toCoordinates() }
    val start = input.getCoordinatesOfFirst('S').toCoordinates()
    val end = input.getCoordinatesOfFirst('E').toCoordinates()
    val gridSize = input.gridSize()

    val default = shortestPaths(start, end, gridSize, walls).first()

    //TODO: extract
    val shortcuts = default.flatMap { c ->
      default.mapNotNull { end ->
        if (c.distance(end) in 2..20)
          Triple(c, end, c.distance(end))
        else
          null
      }
    }

    return shortcuts.count { (start, end, distance) ->
      val newCost = default.subList(0, default.indexOf(start) + 1).size - 1 + default.subList(default.indexOf(end), default.size).size - 1 + distance - 1
      (default.size - 1) - newCost >= 100
    }
  }

  val testInput = readInput("inputs/Day20_test")
  //check(part1(testInput).also { println(it) } == 0)
  //check(part2(testInput).also { println(it) } == 6)

  val input = readInput("inputs/Day20")
  part1(input).println()
  part2(input).println()
}
