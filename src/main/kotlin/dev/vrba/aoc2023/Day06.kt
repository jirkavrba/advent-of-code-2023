package dev.vrba.aoc2023

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToLong
import kotlin.math.sqrt

@Solved
data object Day06 : Task<Long>(6, "Wait For It") {

    override fun part1(lines: List<String>): Long {
        return parseInput(lines).fold(1L) { product, record ->
            product * numberOfWinCombinations(record)
        }
    }

    override fun part2(lines: List<String>): Long {
        val records = parseInput(lines)
        val mergedTimes = records.map { it.time.toString() }.reduce { a, b -> a + b }.toLong()
        val mergedDistances = records.map { it.distance.toString() }.reduce { a, b -> a + b }.toLong()

        val mergedRecord = Record(
            time = mergedTimes,
            distance = mergedDistances
        )

        return numberOfWinCombinations(mergedRecord)
    }

    private data class Record(
        val time: Long,
        val distance: Long
    )

    private fun solveQuadraticEquation(a: Long, b: Long, c: Long): Pair<Double, Double> {
        val discriminant = sqrt(b * b - 4.0 * a * c)
        val results = listOf(-1, 1).map { (-b + it * discriminant) / (2 * a) }

        return results.min() to results.max()
    }

    // Find all integer solutions for: (time - x) * x - distance > 0
// ~= -1 * x^2 + time * x -1 * record = 0
    private fun numberOfWinCombinations(record: Record): Long {
        val (low, high) = solveQuadraticEquation(-1, record.time, -record.distance)

        // Number of integers in the given range
        return ceil(high - 1).roundToLong() - floor(low + 1).roundToLong() + 1
    }

    private fun parseInput(lines: List<String>): List<Record> {
        val (timesLine, distanceLine) = lines
        val times = "\\d+".toRegex().findAll(timesLine.removePrefix("Time:")).map { it.value.toLong() }
        val distances = "\\d+".toRegex().findAll(distanceLine.removePrefix("Distance:")).map { it.value.toLong() }

        return times.zip(distances).map { Record(it.first, it.second) }.toList()
    }

}
