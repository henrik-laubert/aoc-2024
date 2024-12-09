
fun main() {

  class File(var id: Int, var size: Int, var space: Int)

  fun parseInput(input: List<String>): List<String> {
    val disk = mutableListOf<String>()
    input.first().split("").filter { it.isNotBlank() }.forEachIndexed { i, c ->
      if (i % 2 == 0) {
        repeat(c.toInt()) { disk.add("${i / 2}") }
      } else {
        repeat(c.toInt()) { disk.add(".") }
      }
    }
    return disk
  }

  fun compress(string: List<String>): List<String> {
    val temp = string.toMutableList()
    var current = 0

    while (current < temp.lastIndex) {
      if (temp[current] == ".") {
        val c = temp.removeLast()
        if (c != ".") temp[current] = c
        else current--
      }
      current++
    }
    return temp
  }

  fun checksum(string: List<String>): Long {
    return string.foldIndexed(0) {index, acc, s -> if(s != ".") acc + (index * s.toInt()) else acc }
  }

  fun parseInputToFiles(input: List<String>): List<File> {
    val disk = mutableListOf<File>()
    val temp = input.first().toMutableList()
    if (temp.size % 2 == 1) temp.add('0')
    for (it in temp.indices step 2) {
      disk.add(File(it/2, temp[it].digitToInt(), temp[it+1].digitToInt()))
    }
    return disk
  }

  fun moveFile(file: File, disk: List<File>): List<File> {
    val gap = disk.firstOrNull { it.space >= file.size && disk.indexOf(file) > disk.indexOf(it) }
    return if (gap != null) {
      disk[disk.indexOf(file) - 1].space += file.size + file.space
      file.space = gap.space - file.size
      gap.space = 0
      val temp = disk.toMutableList().also { it.remove(file) }
      temp.subList(0, temp.indexOf(gap) + 1) + file + temp.subList(temp.indexOf(gap) + 1, temp.size)
    } else disk
  }

  fun fileDiskToListOfString(disk: List<File>): List<String> {
    val temp = mutableListOf<String>()
    disk.forEach { file ->
      repeat(file.size) { temp.add(file.id.toString()) }
      repeat(file.space) { temp.add(".")}
    }
    return temp
  }

  fun part1(input: List<String>): Long {
    return checksum(compress(parseInput(input)))
  }

  fun part2(input: List<String>): Long {
    var disk = parseInputToFiles(input)

    for (i in disk.last().id downTo 1) {
      disk = moveFile(disk.first { it.id == i }, disk)
    }
    return checksum(fileDiskToListOfString(disk))
  }

  val testInput = readInput("Day09_test")
  check(part1(testInput) == 1928L)
  check(part2(testInput) == 2858L)

  val input = readInput("Day09")
  part1(input).println()
  part2(input).println()
}
