import utils.readInput

fun main()  {

  data class Plot(val plant: String, val region: Int? = null)

  fun Grid<Plot>.computeRegions(): Grid<Plot> {
    var nextRegion = 0
    val regionAlias = mutableListOf<MutableSet<Int>>()

    this.forEachIndexed { index, current ->
      val n = this.getNeighbors(index, listOf(Direction.LEFT, Direction.UP)).filterNotNull().filter {it.plant == current.plant}
      when (n.size) {
        1 -> this[index] = current.copy(region = n.first().region)
        2 -> {
          val leftRegion = n.first().region!!
          val upRegion = n.last().region!!
          if (leftRegion != upRegion)
            regionAlias.firstOrNull { leftRegion in it || upRegion in it }
              ?.addAll(listOf(leftRegion, upRegion))
              ?: regionAlias.add(mutableSetOf(leftRegion, upRegion))
          this[index] = current.copy(region = upRegion)
        }
        else -> this[index] = current.copy(region = nextRegion++)
      }
    }

    regionAlias.forEach { alias ->
      val combined = regionAlias.filter { alias.intersect(it).isNotEmpty() }.fold(mutableSetOf<Int>()) { acc, it -> (acc + it).toMutableSet() }
      this.forEachIndexed { j, current ->
        if (current.region in combined)
          this[j] = current.copy(region = combined.min())
      }
    }
    return this
  }

  fun Grid<Plot>.computePerimeters(): Map<Int, Int> {
    val perimeters = mutableMapOf<Int, Int>()

    this.forEachIndexed { index, current ->
      val n = this.getNeighbors(index, Direction.cardinal).filter { it == null || it.plant != current.plant }.size
      perimeters[current.region!!] = perimeters[current.region]?.plus(n) ?: n
    }
    return perimeters
  }

  fun part1(input: List<String>): Int {
    val garden = Grid(
      input.flatten().map { Plot(it) }.toMutableList(),
      input.first().length
    ).computeRegions()

    val perimeters = garden.computePerimeters()
    val regionSizes = garden.groupingBy { it.region }.eachCount()

    return regionSizes.map { (region, size) -> size * perimeters[region]!! }
      .sum()
  }

  fun part2(input: List<String>): Int {
    val garden = Grid(
      input.flatten().map { Plot(it) }.toMutableList(),
      input.first().length
    ).computeRegions()


    val corners = mutableMapOf<Int, Int>()
    garden.forEachIndexed { index, current ->

      val neighbors = garden.getNeighbors(index, Direction.entries) //clockwise
      val candidates = neighbors.windowed(3, step = 2).toMutableList()
      candidates.add(listOf(neighbors[6], neighbors[7], neighbors[0]))

      val n = candidates.count { candidate ->
        candidate.all { it == null || it.region != current.region }
            || (candidate.none { it == null } && candidate[1]!!.region != current.region && candidate.first()!!.region == current.region && candidate.last()!!.region == current.region)
            || (candidate.none { it == null } && candidate[1]!!.region == current.region && candidate.first()!!.region != current.region && candidate.last()!!.region != current.region)
      }

      corners[current.region!!] = corners[current.region]?.plus(n) ?: n
    }


    val regionSizes = garden.groupingBy { it.region }.eachCount()

    return regionSizes.map { (region, size) -> size * corners[region]!! }
      .sum()
  }

  val testInput = readInput("Day12_test")
  check(part1(testInput) == 1930)
  check(part2(testInput) == 1206)

  val input = readInput("Day12")
  part1(input).println()
  part2(input).println()
}