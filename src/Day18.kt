
fun main()  {

  fun parseInput(input: List<String>): List<Coordinates> =
    input.map { it.split(",").map(String::toInt) }
      .map { Coordinates(it[0], it[1]) }

  fun Coordinates.inBounds(dim: Coordinates): Boolean {
    return this.x in 0..dim.x && this.y in 0..dim.y
  }

  fun shortestPaths(
    start: Coordinates = Coordinates(0,0),
    end: Coordinates,
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
        if (next !in obstacles && next.inBounds(end) && visited.getOrDefault(next, Int.MAX_VALUE) > costs) {
          queue.add(current + next)
          visited[next] = costs
        }
      }
    }

    return results
  }

  fun part1(input: List<String>, bytes: Int, memorySize: Pair<Int,Int>): Int {
    val corrupted = parseInput(input).take(bytes)

    return shortestPaths(end = Coordinates(memorySize), obstacles = corrupted).first().size - 1
  }

  fun part2(input: List<String>, bytes: Int, memorySize: Pair<Int,Int>): Pair<Int,Int> {
    val corrupted = parseInput(input)
    var counter = bytes

    var paths = shortestPaths(end = Coordinates(memorySize), obstacles = corrupted.take(counter))

    while (paths.isNotEmpty()) {
      while (paths.none { corrupted[counter - 1] in it })  {
        counter++
      }
      paths = shortestPaths(end = Coordinates(memorySize), obstacles = corrupted.take(counter))
    }

    return corrupted[counter - 1].x to corrupted[counter - 1].y
  }

  val testInput = readInput("inputs/Day18_test")
  check(part1(testInput, 12, 6 to 6).also { println(it) } == 22)
  check(part2(testInput, 12, 6 to 6).also { println(it) } == 6 to 1)

  val input = readInput("inputs/Day18")
  part1(input, 1024, 70 to 70).println()
  part2(input, 1024, 70 to 70).println()
}
