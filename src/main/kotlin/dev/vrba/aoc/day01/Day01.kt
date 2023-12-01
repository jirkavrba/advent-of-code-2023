package dev.vrba.aoc.day01

import dev.vrba.aoc.Task

object Day01 : Task(1) {

    override fun part1(): Any {
        val lines = loadInputLines()
        val parsed = lines.map { it.firstDigit() * 10 + it.lastDigit() }

        return parsed.sum()
    }

    override fun part2(): Any {
        val lines = loadInputLines()
        val parsed = lines
            .map { it.translateNumbersToDigits() }
            .map { it.firstDigit() * 10 + it.lastDigit() }

        return parsed.sum()
    }

    private fun CharSequence.translateNumbersToDigits(): CharSequence {
        // The translated number is duplicated to prevent consuming an overlapping character from another digit
        // E.g. "eightwothree" would be interpreted as "eigh23", which is incorrect
        val translations = mapOf(
            "one" to "on21one",
            "two" to "two2two",
            "three" to "three3three",
            "four" to "four4four",
            "five" to "five5five",
            "six" to "six6six",
            "seven" to "seven7seven",
            "eight" to "eight8eight",
            "nine" to "nine9nine"
        )

        return translations.entries.fold(this) { string, (search, replacement) ->
            string.replace(search.toRegex(), replacement)
        }
    }


    private fun CharSequence.firstDigit(): Int? =
        firstOrNull { it.isDigit() }?.digitToInt()

    private fun CharSequence.lastDigit(): Int? =
        lastOrNull { it.isDigit() }?.digitToInt()

    private operator fun Int?.times(other: Int?) = (this ?: 0) * (other ?: 0)

    private operator fun Int?.plus(other: Int?) = (this ?: 0) + (other ?: 0)
}
