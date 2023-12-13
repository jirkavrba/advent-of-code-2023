package dev.vrba.aoc2023

@Solved
object Day01 : Task<Int>(1, "Trebuchet?!") {

    override fun part1(lines: List<String>): Int =
        lines.sumOf { it.firstDigit() * 10 + it.lastDigit() }

    override fun part2(lines: List<String>): Int =
        lines
            .map { it.translateNumbersToDigits() }
            .sumOf { it.firstDigit() * 10 + it.lastDigit() }

    private fun String.firstDigit(): Int =
        firstOrNull { it.isDigit() }?.digitToInt() ?: 0

    private fun String.lastDigit(): Int =
        lastOrNull { it.isDigit() }?.digitToInt() ?: 0


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
}
