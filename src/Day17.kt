import kotlin.math.pow

enum class Opcode {
  ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV;
}

fun main()  {

  class Computer(var a: Long, programm: List<Int>, ) {
    var b = 0L
    var c = 0L
    var programmPointer = 0L

    val instructions = programm.windowed(2,2).map {
      Opcode.entries[it.first()] to it.last().toLong()
    }

    val out = mutableListOf<Long>()

    private fun combo(operant: Long): Long =
      when (operant) {
        0L, 1L, 2L, 3L -> operant
        4L -> a
        5L -> b
        6L -> c
        else -> throw Exception("Reserved")
      }

    fun printState() {
      println("A: $a, B: $b, C: $c, out: $out")
      //instructions.forEachIndexed { index, it ->
      //  if (index.toLong() == programmPointer) print("p -> ") else print("     ")
      //  println(it)
      //}
    }

    fun step(): Boolean  {
      instructions.getOrNull(programmPointer.toInt())
        ?.let{ (op, value) ->
          when (op) {
            Opcode.ADV -> a = (a / 2.0.pow(combo(value).toDouble()).toLong())
            Opcode.BXL -> b = b xor value
            Opcode.BST -> b = combo(value) % 8
            Opcode.JNZ -> if (a != 0L) programmPointer = value else programmPointer += 2
            Opcode.BXC -> b = b xor c
            Opcode.OUT -> out.add(combo(value) % 8)
            Opcode.BDV -> b = (a / 2.0.pow(combo(value).toDouble()).toInt())
            Opcode.CDV -> c = (a / 2.0.pow(combo(value).toDouble()).toInt())
          }

          if (op != Opcode.JNZ) programmPointer++

          return true
        } ?: return false
    }

    fun execute(breakPoint: Long? = null, verbose: Boolean = false) {
      while (step()) {
        if (verbose) printState()
        if (programmPointer == breakPoint) break
      }
    }

    fun flush(): String =
      out.joinToString(",")
  }

  fun part1(input: Computer): String {
    input.execute()
    return input.flush()
  }

  fun part2(input: List<Int>): Long {
    var a = mutableSetOf(0L)

    for(i in input.indices) {
      a = a.map{ (it * 8..it * 8 + 7 ).toList() }.flatten().filter {
        val c = Computer(it, input)
        c.execute(7)
        c.out.first() == input[input.lastIndex - i].toLong() && c.a in a
      }.toSet().toMutableSet()
    }

    return a.min()
  }

  val testInput = Computer(729, listOf(0, 1, 5, 4, 3, 0))
  check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
  //check(part2(listOf(0,3,5,4,3,0)) == 117440L)

  val input = Computer(47006051, listOf(2,4,1,3,7,5,1,5,0,3,4,3,5,5,3,0))
  part1(input).println()
  part2(listOf(2,4,1,3,7,5,1,5,0,3,4,3,5,5,3,0)).println()
}
