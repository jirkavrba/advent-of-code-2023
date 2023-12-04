package dev.vrba.aoc

import kotlin.math.pow

object Day04 : Task(4) {

    data class ScratchCard(
        val number: Int,
        val winningNumbers: Set<Int>,
        val scratchedNumbers: List<Int>
    ) {
        val points: Int
            get() = 2.0.pow(scratchedNumbers.count { it in winningNumbers } - 1).toInt()
    }

    override fun part1(): Int =
        parseInput().sumOf { it.points }

    private fun parseInput(): List<ScratchCard> =
        readInputLines().map { line ->
            val (number, numbers) = line.removePrefix("Card ").split(": ")
            val (winningNumbers, scratchedNumbers) = numbers.split(" | ")

            ScratchCard(
                number = number.trim().toInt(),
                winningNumbers = winningNumbers.trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet(),
                scratchedNumbers = scratchedNumbers.trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            )
        }
}
