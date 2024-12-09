fun main() {

  fun parseInput(input: List<String>): Map<String, List<Coordinates>> {
    val antennas = mutableMapOf<String, MutableList<Coordinates>>()

    input.forEachIndexed { i, line ->
      line.forEachIndexed { j, char ->
        if (char != '.') {
          antennas.getOrPut(char.toString()) { mutableListOf() }
            .add(Coordinates(i, j))
        }
      }
    }

    return antennas
  }

  fun directionVector(a: Coordinates, b: Coordinates): Coordinates {
    return Coordinates(b.first - a.first, b.second - a.second)
  }

  fun coodinatesInGrid(input: List<String>, coordinates: Coordinates): Boolean {
    return coordinates.first in input.indices && coordinates.second in 0 until input.first().length
  }

  fun part1(input: List<String>): Int {
    val antennas = parseInput(input)
    val antinodes = mutableSetOf<Coordinates>()

    antennas.keys.forEach { type ->
      antennas[type]!!.forEachIndexed { i, antenna ->
        for (j in i+1 until antennas[type]!!.size) {
          val v = directionVector(antenna, antennas[type]!![j])
          antinodes.addAll(listOf(antenna - v, antennas[type]!![j] + v))
        }
      }
    }

    return antinodes.filter { coodinatesInGrid(input,it) }.size
  }

  fun part2(input: List<String>): Int {
    val antennas = parseInput(input)
    val antinodes = mutableSetOf<Coordinates>()

    antennas.keys.forEach { type ->
      antennas[type]!!.forEachIndexed { i, antenna ->
        for (j in i+1 until antennas[type]!!.size) {
          val other = antennas[type]!![j]
          val v = directionVector(antenna, other)
          antinodes.addAll(listOf(antenna, other))

          var nextBackwards = antenna - v
          while (coodinatesInGrid(input, nextBackwards)) {
            antinodes.add(nextBackwards)
            nextBackwards -= v
          }
          var nextForwards = other + v
          while (coodinatesInGrid(input,nextForwards)) {
            antinodes.add(nextForwards)
            nextForwards += v
          }
        }
      }
    }

    return antinodes.size
  }

  val testInput = readInput("Day08_test")
  check(part1(testInput) == 14)
  check(part2(testInput) == 34)

  val input = readInput("Day08")
  part1(input).println()
  part2(input).println()
}
