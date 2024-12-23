import kotlin.math.abs

fun main() {

  val numericPad = listOf("789", "456", "123", " 0A").toGrid()
  val steeringPad = listOf(" ^A", "<v>").toGrid()

  val cache = HashMap<Pair<String, Int>, Long>()

  fun moveOnPad(start: Coordinates, end: Coordinates, numeric: Boolean = false): Set<String> {
    val pointer = end - start

    val xDirection = if (pointer.x < 0) Direction.LEFT else Direction.RIGHT
    val yDirection = if (pointer.y < 0) Direction.UP else Direction.DOWN

    val xMoves = xDirection.toString().repeat(abs(pointer.x))
    val yMoves = yDirection.toString().repeat(abs(pointer.y))

    val options = listOf(xMoves + yMoves + "A", yMoves + xMoves + "A")

    return if(numeric) {
      if (start.y == 3 && end.x == 0)
        setOf(options[1])
      else if (start.x == 0 && end.y == 3)
        setOf(options[0])
      else setOf(options[0], options[1])
    } else {
      if (start.y == 0 && end == Coordinates(0, 1))
        setOf(options[1])
      else if (start == Coordinates(0, 1))
        setOf(options[0])
      else
        setOf(options[0], options[1])
    }
  }

  fun moveSequencesOnPad(sequences: Set<String>, pad: Grid<String> = steeringPad): Set<String> {
    return sequences.flatMap { sequence ->
      var current = pad.coordinatesOf("A")
      sequence.fold(setOf("")) { results, nextChar ->
        val next = pad.coordinatesOf(nextChar.toString())
        val options = moveOnPad(current, next, pad == numericPad)
        results.flatMap { result -> options.map { result + it } }.toSet()
          .also { current = next }
      }
    }.toSet()
  }

  fun costsOnLevel(sequence: String, level: Int): Long {
    val translated = moveSequencesOnPad(setOf(sequence))
    return cache.getOrPut(sequence to level) {
      if (level == 1)
        translated.minOf { it.length }.toLong()
    else
      translated.minOf { option ->
        option.split("A").dropLast(1).map { it+"A" }.sumOf { chunk ->
          costsOnLevel(chunk, level - 1)
        }
      }
    }
  }

  fun calculateCosts(levels: Int): Map<Pair<String, String>,Long> {
    val costs = mutableMapOf<Pair<String, String>, Long>()
    val buttons = listOf("A", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    val combinations = buttons.flatMap { button -> buttons.map { button to it } }

    combinations.forEach { (start, end) ->
      val initial = moveOnPad(numericPad.coordinatesOf(start), numericPad.coordinatesOf(end), true)
      costs[start to end] = initial.minOf { costsOnLevel(it, levels - 1) }
    }

    return costs
  }

  fun part1(input: List<String>): Long {
    val costs = calculateCosts(3)

    return input.sumOf { code ->
      val cost = ("A$code").windowed(2).sumOf {
        costs[it.first().toString() to it.last().toString()]!!
      }

      cost * code.dropLast(1).toInt()
    }
  }

  fun part2(input: List<String>): Long {
    val costs = calculateCosts(26)

    return input.sumOf { code ->
      val cost = ("A$code").windowed(2).sumOf {
        costs[it.first().toString() to it.last().toString()]!!
      }

      cost * code.dropLast(1).toInt()
    }
  }

  val testInput = readInput("inputs/Day21_test")
  check(part1(testInput).also { println(it) } == 126384L)

  val input = readInput("inputs/Day21")
  part1(input).println()
  part2(input).println()
}
