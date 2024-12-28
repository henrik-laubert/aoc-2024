package utils

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
): Pair<A, B>
{
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