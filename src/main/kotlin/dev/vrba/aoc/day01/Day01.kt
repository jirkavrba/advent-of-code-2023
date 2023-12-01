package dev.vrba.aoc.day01

import dev.vrba.aoc.Task

object Day01 : Task(1) {

    override fun part1(): Any {
        val lines = loadInputLines()
        val parsed = lines.map { line -> line.firstDigit() * 10 + line.lastDigit() }

        return parsed.sum()
    }

    private fun CharSequence.firstDigit(): Int? =
        firstOrNull { it.isDigit() }?.digitToInt()

    private fun CharSequence.lastDigit(): Int? =
        lastOrNull { it.isDigit() }?.digitToInt()

    private operator fun Int?.times(other: Int?) = (this ?: 0) * (other ?: 0)

    private operator fun Int?.plus(other: Int?) = (this ?: 0) + (other ?: 0)
}
