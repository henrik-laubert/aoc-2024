import kotlin.math.abs

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

data class Coordinates(val x : Int, val y : Int) {

  constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)

  fun inBounds(end: Pair<Int, Int>): Boolean {
    return this.x in 0..end.first && this.y in 0..end.second
  }

  operator fun plus(other: Coordinates): Coordinates {
    return Coordinates(this.x + other.x, this.y + other.y)
  }

  operator fun minus(other: Coordinates): Coordinates {
    return Coordinates(this.x - other.x, this.y - other.y)
  }

  operator fun times(n: Int): Coordinates {
    return Coordinates(this.x * n, this.y * n)
  }

  operator fun plus(other: Direction): Coordinates {
    return Coordinates(this.x + other.dx, this.y + other.dy)
  }

  operator fun minus(other: Direction): Coordinates {
    return Coordinates(this.x - other.dx, this.y - other.dy)
  }

  fun distance(other: Coordinates): Int {
    return abs(x - other.x) + abs(y - other.y)
  }
}

fun Pair<Int, Int>.toCoordinates() = Coordinates(this)

fun directionVector(a: Coordinates, b: Coordinates): Coordinates {
  return b - a
}
class Grid<T>(val values: MutableList<T>, val rowLength: Int) : Iterable<T> by values {

  private fun Coordinates.toIndex(): Int = this.y * rowLength + this.x
  fun indexToCoordinates(index: Int): Coordinates = Coordinates(index % rowLength, index/rowLength)

  fun getOrNull(pos: Coordinates): T? =
    if (isInBounds(pos)) values[pos.toIndex()] else null

  fun coordinatesOf(element: T): Coordinates =
    indexToCoordinates(values.indexOf(element))

  fun getNeighbors(pos: Coordinates, directions: List<Direction>): List<T?> =
    directions.map { dir -> getOrNull(pos + dir) }

  fun getNeighbors(index: Int, directions: List<Direction>): List<T?> =
    getNeighbors(indexToCoordinates(index), directions)

  fun isInBounds(pos: Coordinates): Boolean =
    pos.x in 0 until rowLength && pos.y in 0 until values.size/rowLength

  operator fun set(index: Int, value: T) = values.set(index, value)
  operator fun set(pos: Coordinates, value: T) = values.set(pos.toIndex(), value)
}

fun List<String>.flatten(): List<String> {
  return this.joinToString("").split("").filter { it.isNotEmpty() }
}

fun List<String>.toGrid(): Grid<String> =
  Grid(this.flatten().toMutableList(), this.first().length)

enum class Direction(val dx: Int, val dy: Int) {
  UP(0,-1),
  UPRIGHT(1,-1),
  RIGHT(1,0),
  DOWNRIGHT(1,1),
  DOWN(0,1),
  DOWNLEFT(-1,1),
  LEFT(-1, 0),
  UPLEFT(-1,-1);

  override fun toString(): String {
    return when(this) {
      UP -> "^"
      RIGHT -> ">"
      DOWN -> "v"
      LEFT -> "<"
      else -> TODO()
    }
  }

  operator fun times(other: Int): Coordinates =
    Coordinates(dx * other, dy * other)

  companion object {
    val cardinal = listOf(UP, RIGHT, DOWN, LEFT)
    val diagonal = listOf(UPRIGHT, DOWNRIGHT, DOWNLEFT, UPLEFT)
  }
}

fun Direction.opposite(): Direction =
  Direction.entries.first { it.dx == -this.dx && it.dy == -this.dy }

fun Direction.rotate(clockwise: Boolean = true): Direction {
  return if (clockwise) Direction.entries.first { this.dx == it.dy && this.dy == -it.dx }
  else Direction.entries.first { it.dx == this.dy && it.dy == this.dx }
}

fun Direction.toPair(): Pair<Int, Int> = this.dx to this.dy

@Suppress("UNCHECKED_CAST")
operator fun <T: Number, U: Number> Pair<T, T>.plus(n: U): Pair<T, T> =
  (first.toLong() + n.toLong()) as T to (second.toLong() + n.toLong()) as T

@Suppress("UNCHECKED_CAST")
operator fun <T: Number, U: Number> Pair<T, T>.plus(other: Pair<U, U>): Pair<T, T> =
  (first.toLong() + other.first.toLong()) as T to (second.toLong() + other.second.toLong()) as T

@Suppress("UNCHECKED_CAST")
operator fun <T: Number, U: Number> Pair<T, T>.times(n: U): Pair<T, T> =
  (first.toLong() * n.toLong()) as T to (second.toLong() * n.toLong()) as T

@Suppress("UNCHECKED_CAST")
operator fun <T: Number, U: Number> Pair<T, T>.rem(other: Pair<U, U>): Pair<T, T> =
  (first.toLong() % other.first.toLong()) as T to (second.toLong() % other.second.toLong()) as T

operator fun<T: Number> Pair<T, T>.plus(direction: Direction): Pair<T, T> = this + direction.toPair()

inline fun <T> withMeasuredTime(name: String = "measured code block", block: () -> T): T {
  val start = System.currentTimeMillis()
  val result = block()
  val end = System.currentTimeMillis()
  println("""Execution time of "$name": ${end-start} ms """)
  return result
}
