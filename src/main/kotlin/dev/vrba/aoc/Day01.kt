package dev.vrba.aoc

object Day01 : Task<Int>(1) {

    override fun part1() =
        readInputLines()
            .sumOf { it.firstDigit() * 10 + it.lastDigit() }

    override fun part2() =
        readInputLines()
            .map { it.translateNumbersToDigits() }
            .sumOf { it.firstDigit() * 10 + it.lastDigit() }

    private fun String.translateNumbersToDigits(): String {
        val translations = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )

        return translations.entries.fold(this) { string, (word, value) ->
            // The translated number is duplicated to prevent consuming an overlapping character from another digit
            // E.g. "eightwothree" would be interpreted as "eigh23", which is incorrect
            string.replace(word, "${word.last()}${value}${word.first()}")
        }
    }

    private fun String.firstDigit(): Int =
        firstOrNull { it.isDigit() }?.digitToInt() ?: 0

    private fun String.lastDigit(): Int =
        lastOrNull { it.isDigit() }?.digitToInt() ?: 0
}
