import kotlin.math.floor

enum class Quadrants { I, II, III, IV; }

fun main()  {

  fun parseInput(input: List<String>): List<Pair<Pair<Long, Long>, Pair<Long, Long>>> {
    val pattern = """(-?\d+)""".toRegex()
    return input.map { line ->
      val digits = pattern.findAll(line).flatMap { it.destructured.toList() }.map(String::toLong).toList()
      (digits[0] to digits[1]) to (digits[2] to digits[3])
    }
  }

  fun computePosition(start: Pair<Long, Long>, velocity: Pair<Long, Long>, times: Int): Pair<Long, Long> =
    start + velocity * times.toLong()

  fun positionInGrid(position: Pair<Long, Long>, gridSize: Pair<Long, Long>): Pair<Long, Long> {
    var (x, y) = position % gridSize
    if (x < 0) x += gridSize.first
    if (y < 0) y += gridSize.second
    return x to y
  }

  fun quadrant(values: List<Pair<Long,Long>>, gridSize: Pair<Long,Long>, q: Quadrants): List<Pair<Long,Long>> {
    val midX = floor(gridSize.first / 2F).toLong()
    val midY = floor(gridSize.second / 2F).toLong()

    var temp = values
    if (gridSize.first % 2L != 0L) temp = temp.filterNot { it.first == midX }
    if (gridSize.second % 2L != 0L) temp = temp.filterNot { it.second == midY }

    return when(q) {
      Quadrants.I -> temp.filter { it.first in midX until gridSize.first && it.second in 0 until midY }
      Quadrants.II -> temp.filter { it.first in midX until gridSize.first && it.second in midY until gridSize.second }
      Quadrants.III -> temp.filter { it.first in 0 until midX && it.second in midY until gridSize.second }
      Quadrants.IV -> temp.filter { it.first in 0 until midX && it.second in 0 until midY }
    }
  }

  fun part1(input: List<String>, gridSize: Pair<Long, Long>): Long {
    val robots = parseInput(input)

    val positions = robots.map { positionInGrid(computePosition(it.first, it.second, 100), gridSize) }

    return Quadrants.entries.map { quadrant(positions, gridSize, it) }.fold(1L) { acc, it -> acc * it.size }
  }

  fun hasEasterEgg(positions: List<Pair<Long,Long>>): Boolean =
    positions.any { current ->
      Direction.entries
        .map { current + (it.dx.toLong() to it.dy.toLong()) }
        .all{it in positions}
    }

  fun part2(input: List<String>,gridSize: Pair<Long, Long>): Int {
    var robots = parseInput(input)
    var seconds = 0

    do {
      robots = robots.map { (p, v) ->
        positionInGrid(computePosition(p, v, 1), gridSize) to v
      }
      seconds++
    } while (!hasEasterEgg(robots.unzip().first))

    return seconds
  }

  val testInput = readInput("Day14_test")
  check(part1(testInput, 11L to 7L) == 12L)
  //check(part2(testInput) == 1206)

  val input = readInput("Day14")

  part1(input, 101L to 103L).println()
  part2(input,101L to 103L).println()
}