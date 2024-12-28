import utils.readInput

fun main() {

  class Graph<T>(val edges: List<Pair<T,T>>)  {
    val vertexSet by lazy {
      edges.flatMap { listOf(it.first, it.second) }.toSet()
    }

    private val _cache = HashMap<T, Set<T>>()

    fun getNeighbours(vertex: T): Set<T> {
      return _cache.getOrPut(vertex) {
        edges.filter { it.first == vertex || it.second == vertex }
          .map { if(it.first == vertex) it.second else it.first }
          .toSet()
      }
    }
  }

  fun part1(input: List<String>): Int {
    val edges = input.map { line -> line.split("-")}.map { it[0] to it[1] }

    val neighbours = mutableMapOf<String, Set<String>>()

    edges.forEach { edge ->
      neighbours[edge.first] = neighbours.getOrPut(edge.first) { emptySet() } + edge.second
      neighbours[edge.second] = neighbours.getOrPut(edge.second) { emptySet() } + edge.first
    }

    val triples = mutableSetOf<Set<String>>()

    neighbours.keys.forEach { pc ->
      neighbours[pc]!!.forEach { neighbour ->
        neighbours[neighbour]!!.forEach {
          if (pc in neighbours[it]!!)
            triples.add(setOf(pc, neighbour, it))
        }
      }
    }

    return triples.count { group -> group.any {'t' == it.first() } }
  }

  fun <T> findMaximalCliques(g: Graph<T>): List<Set<T>> {
    val cliques = mutableListOf<Set<T>>()

    fun recurse(r: Set<T>, p: Set<T>, x: Set<T>) {
      if (p.isEmpty() && x.isEmpty()) {
        cliques.add(r)
        return
      }

      val pivot = p.firstOrNull() ?: x.firstOrNull() ?: return
      val pivotNeighbors = g.getNeighbours(pivot)

      val newP = p.toMutableSet()
      val newX = x.toMutableSet()

      for (v in p) {
        if (v in pivotNeighbors) continue

        val ns = g.getNeighbours(v)
        recurse(r + v, newP.intersect(ns), newX.intersect(ns))
        newP.remove(v)
        newX.add(v)
      }
    }

    recurse(emptySet(), g.vertexSet, emptySet())
    return cliques
  }

  fun part2BronKerbosch(input: List<String>): String {
    val edges = input.map { line -> line.split("-")}.map { it[0] to it[1] }
    val graph = Graph(edges)

    val startMax = System.currentTimeMillis()

    val connectedGroups = findMaximalCliques(graph)

    val endMax = System.currentTimeMillis()

    println("Bron Kerbosch version: ${endMax - startMax} ms")

    return connectedGroups.first { group -> group.size == connectedGroups.maxOf { it.size } }
      .sorted().joinToString(",")
  }

  fun part2Old(input: List<String>): String {
    val edges = input.map { line -> line.split("-")}.map { it[0] to it[1] }
    val graph = Graph(edges)

    val startMax = System.currentTimeMillis()

    var connectedGroups = mutableSetOf<Set<String>>()

    graph.vertexSet.forEach { current ->
      val neighbours = graph.getNeighbours(current)

      neighbours.forEach { neighbour ->
          connectedGroups.add( neighbours intersect graph.getNeighbours(neighbour) + neighbour + current)
      }
    }

    connectedGroups = connectedGroups.filter { group ->
      val edgesInGroup = graph.edges.count { it.first in group && it.second in group }
      edgesInGroup == ((group.size*(group.size -1)) /2)
    }.toMutableSet()

    val endMax = System.currentTimeMillis()

    println("Old version: ${endMax - startMax} ms")

    return connectedGroups.first { group -> group.size == connectedGroups.maxOf { it.size } }
      .sorted().joinToString(",")
  }

  fun part2(input: List<String>): String {
    val edges = input.map { line -> line.split("-")}.map { it[0] to it[1] }
    val graph = Graph(edges)

    val startMax = System.currentTimeMillis()

    val connectedGroups = mutableSetOf<Set<String>>()

    graph.vertexSet.forEach { current ->
      val neighbours = graph.getNeighbours(current)

      var candidates = mutableSetOf(neighbours + current)

      neighbours.forEach { neighbour ->
        for (i in candidates.indices) {
          val newCandidate = candidates.elementAt(i) intersect graph.getNeighbours(neighbour) + neighbour
          if (newCandidate.size >= 3) { candidates.add(newCandidate)}
        }
      }

      candidates = candidates.filter { group ->
        val edgesInGroup = graph.edges.count { it.first in group && it.second in group }
        edgesInGroup == ((group.size*(group.size -1)) /2)
      }.toMutableSet()

      candidates = candidates.filter { candidate ->
        candidates.none { it intersect candidate == candidate && it != candidate}
      }.toMutableSet()

      connectedGroups.addAll(candidates)
    }

    //connectedGroups = connectedGroups.filter { group ->
    //  val edgesInGroup = graph.edges.count { it.first in group && it.second in group }
    //  edgesInGroup == ((group.size*(group.size -1)) /2)
    //}.toMutableSet()

    return connectedGroups.first { group -> group.size == connectedGroups.maxOf { it.size } }
      .sorted().joinToString(",")
  }

  val testInput = readInput("inputs/Day23_test")
  //check(part1(testInput) == 7)
  //check(part2(listOf("ka-co","ta-co", "de-co", "ta-ka", "de-ta", "ka-de", "ta-c", "de-a", "de-b", "a-b")) == "co,de,ka,ta")
  //check(part2(testInput.take(30)) == "co,de,ka,ta")
  //check(part2(
  //  listOf(
  //    "a-b", "a-c", "a-d", "a-e", "a-f", "a-g",
  //    "b-c", "b-d", "b-e", "b-f", "b-h",
  //    "c-d", "c-e", "c-g", "c-h",
  //    "d-g", "d-h", "d-f"
  //  )
  //) == "a,b,c,d")
  //check(part2(
  //  listOf(
  //    "a-b", "a-c", "a-d", "a-e", "a-f", "a-g",
  //    "b-c", "b-d", "b-e", "b-h", "b-i",
  //    "c-d", "c-f", "c-i", "c-j",
  //    "d-g", "d-h", "d-j"
  //  )
  //) == "a,b,c,d")

  val input = readInput("inputs/Day23")
  //part1(input).println()
  part2(input).println()
  //part2Old(input).println()
  //part2BronKerbosch(input).println()
}
