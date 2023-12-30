package dev.vrba.aoc2023

import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.pow

@Solved
data object Day04 : Task<Int>(4, "Scratchcards") {

    override fun part1(lines: List<String>): Int {
        return parseInput(lines).sumOf { it.points }
    }

    override fun part2(lines: List<String>): Int {
        val initialScratchcards = parseInput(lines)
        val processedScratchcards = AtomicInteger()

        val scratchcards = initialScratchcards.toMutableList()

        while (scratchcards.isNotEmpty()) {
            val current = scratchcards.first()
            val gainedScratchcards = initialScratchcards.drop(current.number).take(current.matchingNumbers)

            processedScratchcards.incrementAndGet()

            scratchcards.removeAt(0)
            scratchcards.addAll(0, gainedScratchcards)
        }

        return processedScratchcards.get()
    }

    private data class Scratchcard(
        val number: Int,
        val winningNumbers: Set<Int>,
        val scratchedNumbers: List<Int>
    ) {
        val matchingNumbers: Int
            get() = scratchedNumbers.count { it in winningNumbers }

        val points: Int
            get() = 2.0.pow(matchingNumbers - 1).toInt()
    }

    private fun parseInput(lines: List<String>): List<Scratchcard> {
        fun String.parseNumbers(): List<Int> = trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }

        return lines.map { line ->
            val (number, numbers) = line.removePrefix("Card ").split(": ")
            val (winningNumbers, scratchedNumbers) = numbers.split(" | ")

            Scratchcard(
                number = number.trim().toInt(),
                winningNumbers = winningNumbers.parseNumbers().toSet(),
                scratchedNumbers = scratchedNumbers.parseNumbers()
            )
        }
    }
}
