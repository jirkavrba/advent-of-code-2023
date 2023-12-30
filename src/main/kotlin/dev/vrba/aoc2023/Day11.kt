package dev.vrba.aoc2023

import kotlin.math.max
import kotlin.math.min

@Solved
data object Day11 : Task<Long>(11, "Cosmic Expansion") {

    override fun part1(lines: List<String>): Long {
        return solve(parseInput(lines), 2)
    }

    override fun part2(lines: List<String>): Long {
        return solve(parseInput(lines), 1_000_000)
    }

    private fun solve(galaxies: List<Galaxy>, distance: Long): Long {
        val rows = galaxies.minOf { it.x }..galaxies.maxOf { it.x }
        val columns = galaxies.minOf { it.y }..galaxies.maxOf { it.y }
        val emptyRows = rows.filter { row -> galaxies.none { it.x == row } }
        val emptyColumns = columns.filter { column -> galaxies.none { it.y == column } }
        val pairs = galaxies.flatMapIndexed { index, first ->
            galaxies.drop(index + 1).map { second ->
                first to second
            }
        }

        return pairs.sumOf {
            it.first.distanceTo(it.second, emptyRows, emptyColumns, distance)
        }
    }

    private data class Galaxy(
        val x: Int,
        val y: Int
    )

    private fun Galaxy.distanceTo(other: Galaxy, emptyRows: List<Int>, emptyColumns: List<Int>, expandedSize: Long): Long {
        val vertical = (min(y, other.y)..max(y, other.y)).sumOf { if (it in emptyColumns) expandedSize else 1L }
        val horizontal = (min(x, other.x)..max(x, other.x)).sumOf { if (it in emptyRows) expandedSize else 1L }

        return vertical + horizontal - 2
    }

    private fun parseInput(lines: List<String>): List<Galaxy> {
        return lines.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, char ->
                if (char == '#') Galaxy(x, y)
                else null
            }
        }
    }
}
