package dev.vrba.aoc

import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.pow

object Day04 : Task<Int>(4) {

    data class ScratchCard(
        val number: Int,
        val winningNumbers: Set<Int>,
        val scratchedNumbers: List<Int>
    ) {
        val matchingNumbers: Int
            get() = scratchedNumbers.count { it in winningNumbers }

        val points: Int
            get() = 2.0.pow(matchingNumbers - 1).toInt()
    }

    override fun part1(): Int =
        parseInput().sumOf { it.points }

    // TODO: Candidate for optimization
    override fun part2(): Int {
        val initialScratchcards = parseInput()

        val scratchcards = initialScratchcards.toMutableList()
        val processedScratchcards = AtomicInteger()

        while (scratchcards.isNotEmpty()) {
            val current = scratchcards.first()
            val gainedScratchcards = initialScratchcards.drop(current.number).take(current.matchingNumbers)

            processedScratchcards.incrementAndGet()

            scratchcards.removeAt(0)
            scratchcards.addAll(0, gainedScratchcards)
        }

        return processedScratchcards.get()
    }

    private fun parseInput(): List<ScratchCard> {
        fun String.parseNumbers(): List<Int> = trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }

        return readInputLines().map { line ->
            val (number, numbers) = line.removePrefix("Card ").split(": ")
            val (winningNumbers, scratchedNumbers) = numbers.split(" | ")

            ScratchCard(
                number = number.trim().toInt(),
                winningNumbers = winningNumbers.parseNumbers().toSet(),
                scratchedNumbers = scratchedNumbers.parseNumbers()
            )
        }
    }
}
