
fun main()  {

  fun parseInput(input: List<String>): Triple<Coordinates, Coordinates, Grid<String>> {
    val start = input.getCoordinatesOfFirst('S').toCoordinates()
    val end = input.getCoordinatesOfFirst('E').toCoordinates()

    return Triple(start, end, input.toGrid())
  }

  fun calculateCosts(start: Coordinates, end: Coordinates, grid: Grid<String>): Pair<Int, Int> {
    val visited = mutableMapOf(start to 0)
    val queue = mutableListOf(Triple(listOf(start), 0 , Direction.RIGHT))
    val results = mutableListOf<Pair<Int, List<Coordinates>>>()

    while (queue.isNotEmpty()) {
      val current = queue.removeFirst()
      if (current.first.last() == end) {
        results.add(current.second to current.first)
        continue
      }

      Direction.cardinal.filterNot { it == current.third.opposite() }.forEach {
        val next = current.first.last() + it
        val costs = current.second + if (it != current.third) 1001 else 1
        if (grid.getOrNull(next) != "#" && next !in current.first && visited.getOrDefault(next, Int.MAX_VALUE) >= costs - 1000) {
          queue.add(Triple(current.first + next, costs, it))
          visited[next] = costs
        }
      }
    }

    val min = results.minOf {it.first}
    val tiles = results.filter { it.first == min }
      .fold(emptySet<Coordinates>()){ acc, it -> acc + it.second }.size

    return min to tiles
  }

  fun part1(input: List<String>): Int {
    val (start, end, grid) = parseInput(input)
    return calculateCosts(start, end, grid).first
  }

  fun part2(input: List<String>): Int {
    val (start, end, grid) = parseInput(input)
    return calculateCosts(start, end, grid).second
  }

  val testInput = readInput("inputs/Day16_test")
  check(part1(testInput) == 7036)
  check(part2(testInput) == 45)

  check(part2(readInput("inputs/Day16_test_2")) == 64)

  val input = readInput("inputs/Day16")
  part1(input).println()
  part2(input).println()
}
