import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()


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

    operator fun plus(other: Coordinates): Coordinates {
        return Coordinates(this.x + other.x, this.y + other.y)
    }

    operator fun minus(other: Coordinates): Coordinates {
        return Coordinates(this.x - other.x, this.y - other.y)
    }

    operator fun plus(other: Direction): Coordinates {
        return Coordinates(this.x + other.dx, this.y + other.dy)
    }

    operator fun minus(other: Direction): Coordinates {
        return Coordinates(this.x - other.dx, this.y - other.dy)
    }
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
