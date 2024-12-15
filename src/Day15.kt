
fun main()  {

  class Box(var left: Coordinates, var right: Coordinates = left + Direction.RIGHT) {
    fun couldMove(direction: Direction, walls: Set<Coordinates>, boxes: Set<Box>) : Boolean {
      val nextLeft = left + direction
      val nextRight = right + direction
      if (nextLeft in walls || nextRight in walls) return false
      val neighbours = when(direction) {
        Direction.UP, Direction.DOWN ->
          boxes.filter { nextLeft == it.left || nextLeft == it.right || nextRight == it.left || nextRight == it.right }
        Direction.LEFT -> boxes.filter { nextLeft == it.right }
        Direction.RIGHT -> boxes.filter { nextRight == it.left }
        else -> return false
      }
      return neighbours.isEmpty() || neighbours.all { it.couldMove(direction, walls, boxes) }
    }

    fun move(direction: Direction, walls: Set<Coordinates>, boxes: Set<Box>) {
      val nextLeft = left + direction
      val nextRight = right + direction
      val neighbours = when(direction) {
        Direction.UP, Direction.DOWN ->
          boxes.filter { nextLeft == it.left || nextLeft == it.right || nextRight == it.left || nextRight == it.right }
        Direction.LEFT -> boxes.filter { nextLeft == it.right }
        Direction.RIGHT -> boxes.filter { nextRight == it.left }
        else -> return
      }

      neighbours.forEach {
        it.move(direction, walls, boxes)
      }
      this.left = nextLeft
      this.right = nextRight
      return
    }
  }

  fun parseInput(input: List<String>): Triple<Set<Coordinates>, MutableSet<Coordinates>, Coordinates> {

    val walls = mutableSetOf<Coordinates>()
    val boxes = mutableSetOf<Coordinates>()
    var startPosition = Coordinates(0, 0)

    input.forEachIndexed { y, row ->
      row.forEachIndexed { x, it ->
        when (it) {
          '#' -> walls.add(Coordinates(x, y))
          'O' -> boxes.add(Coordinates(x, y))
          '@' -> startPosition = Coordinates(x, y)
        }
      }
    }

    return Triple(walls, boxes, startPosition)
  }

  fun parseInput2(input: List<String>): Triple<Set<Coordinates>, Set<Box>, Coordinates> {

    val walls = mutableSetOf<Coordinates>()
    val boxes = mutableSetOf<Box>()
    var startPosition = Coordinates(0, 0)

    input.forEachIndexed { y, row ->
      row.forEachIndexed { x, it ->
        when (it) {
          '#' -> walls.addAll(setOf(Coordinates(2*x, y), Coordinates(2*x+1,y)))
          'O' -> boxes.add(Box(Coordinates(2*x, y), Coordinates(2*x+1, y)))
          '@' -> startPosition = Coordinates(2*x, y)
        }
      }
    }

    return Triple(walls, boxes, startPosition)
  }

  fun parseMoves(input: List<String>): List<Direction> {
    return input.joinToString("").map {
      when (it) {
        '^' -> Direction.UP
        'v' -> Direction.DOWN
        '>' -> Direction.RIGHT
        '<' -> Direction.LEFT
        else -> null
      }
    }.filterNotNull()
  }

  fun moveBox(box: Coordinates, direction: Direction, walls: Set<Coordinates>, boxes: MutableSet<Coordinates>): Boolean {
    when (val next = box + direction) {
      in boxes -> if (moveBox(next, direction, walls, boxes)) {
        with(boxes) {
          remove(box)
          add(next)
        }
        return true
      } else return false
      in walls -> return false
      else -> {
        with(boxes) {
          remove(box)
          add(next)
        }
        return true
      }
    }
  }

  fun part1(input: List<String>): Long {
    val mid = input.indexOfFirst { it.isBlank() }
    val (walls, boxes, start) = parseInput(input.subList(0, mid))
    val moves = parseMoves(input.subList(mid + 1, input.size))

    var current = start
    for (move in moves) {
      val next = current + move
      current = when (next) {
        in boxes -> {
          if (moveBox(next, move, walls, boxes)) next
          else continue
        }
        in walls -> continue
        else -> next
      }
    }

    return boxes.sumOf { it.x + (it.y * 100L) }
  }

  fun part2(input: List<String>): Long {
    val mid = input.indexOfFirst { it.isBlank() }
    val (walls, boxes, start) = parseInput2(input.subList(0, mid))
    val moves = parseMoves(input.subList(mid + 1, input.size))

    var current = start
    for (move in moves) {

      val next = current + move
      if (next in walls) continue
      if (boxes.firstOrNull { it.left == next || it.right == next }?.couldMove(move, walls, boxes) != false) {
        boxes.firstOrNull { it.left == next || it.right == next }?.move(move, walls, boxes)
        current = next
      }
    }

    return boxes.sumOf { it.left.x + (it.left.y * 100L) }
  }

  val testInput = readInput("Day15_test")
  check(part1(testInput) == 10092L)
  check(part2(testInput) == 9021L)

  val input = readInput("Day15")

  part1(input).println()
  part2(input).println()
}