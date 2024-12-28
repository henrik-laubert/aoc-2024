import utils.getCoordinatesOf
import utils.readInput

fun main() {

  fun part1(input: List<String>): Int {
    val grid = input.toGrid()
    val startPositions = input.getCoordinatesOf { it == 'X' }.map { it.toCoordinates() }

    return startPositions.sumOf { pos ->
      Direction.entries.count { dir ->
        List (4) { index -> grid.getOrNull(pos + (dir * index)) }
          .joinToString("") == "XMAS"
      }
    }
  }

  fun part2(input: List<String>): Int {
    val grid = input.toGrid()
    val startPositions = input.getCoordinatesOf { it == 'A' }.map { it.toCoordinates() }

    return startPositions.count { pos ->
      grid.getNeighbors(pos, Direction.diagonal)
        .joinToString("") in listOf("MMSS", "SMMS", "SSMM", "MSSM")
    }
  }

  val testInput = readInput("inputs/Day04_test")
  check(part1(testInput) == 18)
  check(part2(testInput) == 9)

  val input = readInput("inputs/Day04")
  part1(input).println()
  part2(input).println()
}
