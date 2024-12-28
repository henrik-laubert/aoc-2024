import utils.gridSize
import utils.readInput
import kotlin.math.abs

fun main() {

  fun parseInput(input: List<String>): Map<String, List<Coordinates>> {
    val antennas = mutableMapOf<String, MutableList<Coordinates>>()

    input.forEachIndexed { i, line ->
      line.forEachIndexed { j, char ->
        if (char != '.') {
          antennas.getOrPut(char.toString()) { mutableListOf() }
            .add(Coordinates(j, i))
        }
      }
    }

    return antennas
  }

  fun part1(input: List<String>): Int {
    val antennas = parseInput(input)
    val antiNodes = mutableSetOf<Coordinates>()

    antennas.keys.forEach { type ->
      antennas[type]!!.forEachIndexed { i, antenna ->
        for (j in i+1..antennas[type]!!.lastIndex) {
          val v = directionVector(antenna, antennas[type]!![j])
          antiNodes.addAll(listOf(antenna - v, antennas[type]!![j] + v))
        }
      }
    }

    return antiNodes.filter { it.inBounds(input.gridSize()) }.size
  }

  fun part2(input: List<String>): Int {
    val antennas = parseInput(input)
    val antiNodes = mutableSetOf<Coordinates>()

    val gridSize = input.gridSize()

    antennas.keys.forEach { type ->
      antennas[type]!!.forEachIndexed { i, antenna ->
        for (j in i+1 until antennas[type]!!.size) {
          val other = antennas[type]!![j]
          val v = directionVector(antenna, other)

          val back = -(antenna.x / abs(v.x))
          val forward = ((gridSize.first - antenna.x) / abs(v.x)) + 2
          val newNodes = (back..forward).map { k ->
            antenna + (v * k)
          }.filter { it.inBounds(gridSize) }

          antiNodes.addAll(newNodes)
        }
      }
    }

    return antiNodes.size.also { it.println() }
  }

  val testInput = readInput("inputs/Day08_test")
  check(part1(testInput).also { it.println() } == 14)
  check(part2(testInput) == 34)

  val input = readInput("inputs/Day08")
  part1(input).println()
  part2(input).println()
}
