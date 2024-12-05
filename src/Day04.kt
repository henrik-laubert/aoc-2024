fun main() {

  fun part1(input: List<String>): Int {
    var count = 0

    for (row in input.indices) {
      val up = row >= 3
      val down = row + 3 <= input.lastIndex

      for (char in input[row].indices) {

        if (input[row][char] == 'X') {
          val right = char + 3 <= input[row].lastIndex
          val left = char >= 3

          // right-down
          if (right && down && input[row + 1][char + 1] == 'M' && input[row + 2][char + 2] == 'A' && input[row + 3][char + 3] == 'S') count++
          // down
          if (down && input[row + 1][char] == 'M' && input[row + 2][char] == 'A' && input[row + 3][char] == 'S') count++
          // left-down
          if (left && down && input[row + 1][char - 1] == 'M' && input[row + 2][char - 2] == 'A' && input[row + 3][char - 3] == 'S') count++
          // left-up
          if (left && up && input[row - 1][char - 1] == 'M' && input[row - 2][char - 2] == 'A' && input[row - 3][char - 3] == 'S') count++
          // up
          if (up && input[row - 1][char] == 'M' && input[row - 2][char] == 'A' && input[row - 3][char] == 'S') count++
          // right-up
          if (right && up && input[row - 1][char + 1] == 'M' && input[row - 2][char + 2] == 'A' && input[row - 3][char + 3] == 'S') count++
          // right
          if (right && input[row][char + 1] == 'M' && input[row][char + 2] == 'A' && input[row][char + 3] == 'S') count++
          // left
          if (left &&input[row][char - 1] == 'M' && input[row][char - 2] == 'A' && input[row][char - 3] == 'S') count++
        }
      }
    }

    return count
  }

  fun part2(input: List<String>): Int {
    var count = 0

    for (row in 1 until  input.lastIndex) {
      for (char in 1 until input[row].lastIndex) {
        if (input[row][char] == 'A') {
          // left
          if (input[row - 1][char - 1] == 'M' && input[row + 1][char - 1] == 'M' && input[row - 1][char + 1] == 'S' && input[row + 1][char + 1] == 'S') count++
          // right
          if (input[row - 1][char - 1] == 'S' && input[row + 1][char - 1] == 'S' && input[row - 1][char + 1] == 'M' && input[row + 1][char + 1] == 'M') count++
          // up
          if (input[row - 1][char - 1] == 'M' && input[row + 1][char - 1] == 'S' && input[row - 1][char + 1] == 'M' && input[row + 1][char + 1] == 'S') count++
          // down
          if (input[row - 1][char - 1] == 'S' && input[row + 1][char - 1] == 'M' && input[row - 1][char + 1] == 'S' && input[row + 1][char + 1] == 'M') count++
        }
      }
    }

    return count
  }

  val testInput = readInput("Day04_test")
  check(part1(testInput) == 18)
  check(part2(testInput) == 9)

  val input = readInput("Day04")
  part1(input).println()
  part2(input).println()
}
