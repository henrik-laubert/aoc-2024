
fun main() {

  fun next(secret: Long): Long {
    var temp = (secret xor (secret * 64)) % 16777216
    temp = (temp xor (temp / 32)) % 16777216
    return (temp xor (temp * 2048)) % 16777216
  }

  fun part1(input: List<String>): Long {
    return input.map(String::toLong).sumOf {
      var temp = it
      repeat(2000) {
        temp = next(temp)
      }
      temp
    }
  }

  fun part2(input: List<String>): Long {
    val prices = input.map(String::toLong).map { start ->
      val result = mutableListOf(start)
      repeat(2000) {
        result.add(next(result.last()))
      }
      result.map { it % 10 }
    }

    val changes = prices.map { buyer ->
      buyer.mapIndexed { index, price ->
        if (index == 0) null
        else price - buyer[index-1]
      }
    }

    val traitors = prices.mapIndexed { index, price ->
      price.zip(changes[index])
    }

    val sequences = mutableMapOf<List<Long?>, Long>()
    traitors.forEach { traitor ->
      val seen = mutableSetOf<List<Long?>>()
      val (currentPrices, currentChanges) = traitor.unzip()
      for (index in 4..currentChanges.lastIndex) {
        val sequence = currentChanges.subList(index-3, index+1)
        if (sequence in seen) continue
        sequences[sequence] = sequences.getOrPut(sequence) { 0L } + currentPrices[index]
        seen.add(sequence)
      }
    }

    return sequences.maxOf { it.value }
  }

  val testInput = readInput("inputs/Day22_test")
  check(part1(testInput) == 37327623L)
  check(part2(listOf("1","2","3","2024")) == 23L)

  val input = readInput("inputs/Day22")
  part1(input).println()
  part2(input).println()
}
