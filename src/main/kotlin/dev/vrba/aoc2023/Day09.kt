package dev.vrba.aoc2023

private typealias History = List<Long>

@Solved
data object Day09 : Task<Long>(9, "Mirage Maintenance") {

    override fun part1(lines: List<String>): Long {
        return parseInput(lines).sumOf { it.predictNext() }
    }

    override fun part2(lines: List<String>): Long {
        return parseInput(lines).sumOf { it.predictPrevious() }
    }

    private fun parseInput(lines: List<String>): List<History> =
        lines.map { line ->
            line.split("\\s+".toRegex()).map { it.toLong() }
        }

    private fun History.differences(): History =
        zipWithNext().map { it.second - it.first }

    private fun History.predictNext(): Long =
        if (isEmpty()) 0 else differences().predictNext() + last()

    private fun History.predictPrevious(): Long =
        if (isEmpty()) 0 else first() - differences().predictPrevious()
}
