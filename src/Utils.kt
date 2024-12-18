import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

val digitRegex = """(\d+)""".toRegex()

fun List<String>.parseDigitPairs(): List<Pair<Int,Int>> {
  return this.map { line ->
    val digits = digitRegex.findAll(line)
      .flatMap { it.destructured.toList() }.map(String::toInt).toList()
    digits[0] to digits[1]
  }
}

fun List<String>.parseDigits(): List<List<Int>> {
  return this.map { line ->
    digitRegex.findAll(line).flatMap { it.destructured.toList() }
      .map(String::toInt).toList()
  }
}

fun <A, B> List<String>.divideInput(
  predicate: (String) -> Boolean = { it.isBlank() },
  parse: (Pair<List<String>, List<String>>) -> Pair<A, B>
): Pair<A, B> {
  val mid = this.indexOfFirst(predicate)
  return parse(this.subList(0, mid) to this.subList(mid + 1, this.size))
}

fun List<String>.getCoordinatesOf(predicate: (Char) -> Boolean): Set<Pair<Int,Int>> {
  val results = mutableSetOf<Pair<Int,Int>>()

  this.forEachIndexed { y, line ->
    line.forEachIndexed { x, char ->
      if (predicate(char)) {results.add(x to y) }
    }
  }

  return results
}

fun List<String>.getCoordinatesOfFirst(char: Char): Pair<Int,Int> {
  return this.indexOfFirst { char in it }.let {
    this[it].indexOf(char) to it
  }
}

fun List<String>.gridSize(): Pair<Int,Int> =
 this.first().lastIndex to lastIndex

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
  .toString(16)
  .padStart(32, '0')

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

  fun getNeighbors(pos: Coordinates, directions: List<Direction>): List<T?> =
    directions.map { dir -> getOrNull(pos + dir) }

  fun getNeighbors(index: Int, directions: List<Direction>): List<T?> =
    getNeighbors(indexToCoordinates(index), directions)

  fun isInBounds(pos: Coordinates): Boolean =
    pos.x in 0 until rowLength && pos.y in 0 until values.size/rowLength

  operator fun set(index: Int, value: T) = values.set(index, value)
  operator fun set(pos: Coordinates, value: T) = values.set(pos.toIndex(), value)
}

fun List<String>.flatten(): List<String> =
  this.joinToString("").split("").filter { it.isNotBlank() }

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