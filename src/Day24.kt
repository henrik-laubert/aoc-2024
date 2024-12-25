private enum class GateType {
  AND, OR, XOR;
}

fun main() {

  fun Int.toBoolean(): Boolean = this != 0

  fun Map<String, Boolean?>.longOfBinary(wireStart: Char) =
    this.keys.filter { it.first() == wireStart }.sortedDescending()
      .map { if (this[it]!!) 1 else 0 }.joinToString("")
      .toLong(2)

  data class Gate(val a: String, val operation: GateType, val b: String, val out: String) {
    fun compute(wires: Map<String, Boolean?>): Boolean? {
      return if (wires[a] == null || wires[b] == null) null
      else when(operation) {
        GateType.AND -> wires[a]!! && wires[b]!!
        GateType.OR -> wires[a]!! || wires[b]!!
        GateType.XOR -> wires[a]!! xor wires[b]!!
      }
    }
  }

  fun executeCircuit(gates: Set<Gate>, wires:  Map<String, Boolean?>):  Map<String, Boolean?> {
    val queue = gates.toMutableList()
    val result = wires.toMutableMap()
    while (queue.isNotEmpty()) {
      val currentGate = queue.removeFirst()

      result[currentGate.out] = currentGate.compute(result)
      if (result[currentGate.out] == null) {
        queue.add(currentGate)
      }
    }

    return result
  }

  fun part1(input: List<String>): Long {
    val (inputs, gates) = input.divideInput { (first, second) ->
      first.map { it.substringBefore(":") to it.substringAfterLast(" ").toInt().toBoolean() } to
          second.map { line -> """(\w{2,3})""".toRegex().findAll(line).map { it.groupValues[1] }.toList() }
            .map {Gate(it[0], GateType.valueOf(it[1]), it[2], it[3]) }.toSet()
    }

    val wires = mutableMapOf<String, Boolean?>(*inputs.toTypedArray())

    return executeCircuit(gates, wires).longOfBinary('z')
  }

  fun findBadWires(gates: Map<String, Gate>): List<String> {
    fun Gate.isWiredToInputs() = listOf(a.first(), b.first()).all { it in "xy" }

    val bad = mutableListOf<String>()

    for (gate in gates.values) {
      val lhsOp = gates[gate.a]?.operation
      val rhsOp = gates[gate.b]?.operation

      if (gate.out in listOf("z01", "z45"))
        continue

      if (gate.out.first() == 'z') {
        when {
          gate.operation != GateType.XOR -> bad.add(gate.out)
          lhsOp == GateType.AND -> bad.add(gate.a)
          rhsOp == GateType.AND -> bad.add(gate.b)
          lhsOp == GateType.XOR && rhsOp == GateType.XOR -> when {
            gates[gate.a]?.isWiredToInputs() == false -> bad.add(gate.a)
            gates[gate.b]?.isWiredToInputs() == false -> bad.add(gate.b)
            else -> error("bad output with unexpected form: ${gate.out}")
          }
        }

      } else if (gate.operation == GateType.OR) {
        if (lhsOp != GateType.AND) bad.add(gate.a)
        if (rhsOp != GateType.AND) bad.add(gate.b)
      }
    }

    return bad
  }

  fun part2(input: List<String>): String {
    val (inputs, gates) = input.divideInput { (first, second) ->
      first.map { it.substringBefore(":") to it.substringAfterLast(" ").toInt().toBoolean() } to
          second.map { line -> """(\w{2,3})""".toRegex().findAll(line).map { it.groupValues[1] }.toList() }
            .map {Gate(it[0], GateType.valueOf(it[1]), it[2], it[3]) }.toSet()
    }

    return findBadWires(gates.associateBy { it.out }).sorted().joinToString(",")
  }

  val testInput = readInput("inputs/Day24_test")
  check(part1(testInput) == 2024L)

  val input = readInput("inputs/Day24")
  part1(input).println()
  part2(input).println()
}
