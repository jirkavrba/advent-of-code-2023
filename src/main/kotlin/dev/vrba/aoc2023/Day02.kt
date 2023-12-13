package dev.vrba.aoc2023


@Solved
object Day02 : Task<Int>(2, "Cube Conundrum") {

    override fun part1(lines: List<String>): Int {
        val bag = Dices(
            red = 12,
            green = 13,
            blue = 14
        )

        return parseInput(lines)
            .filter { it.isPossibleWithBag(bag) }
            .sumOf { it.number }
    }

    override fun part2(lines: List<String>): Int {
        return parseInput(lines).sumOf { game ->
            val red = game.reveals.maxOf { it.red }
            val green = game.reveals.maxOf { it.green }
            val blue = game.reveals.maxOf { it.blue }

            red * green * blue
        }
    }

    private data class Dices(
        val red: Int = 0,
        val green: Int = 0,
        val blue: Int = 0,
    )

    private data class Game(
        val number: Int,
        val reveals: List<Dices>
    )

    private fun Game.isPossibleWithBag(bag: Dices) =
        reveals.all {
            it.red <= bag.red &&
                    it.green <= bag.green &&
                    it.blue <= bag.blue
        }

    private fun parseInput(lines: List<String>): List<Game> =
        lines.map { line ->
            val (number, reveals) = line.removePrefix("Game ").split(": ")
            val parsedReveals = reveals.split("; ").map {
                it.split(", ").fold(Dices()) { dices, part ->
                    val (count, color) = part.split(" ")

                    when (color) {
                        "red" -> dices.copy(red = count.toInt())
                        "green" -> dices.copy(green = count.toInt())
                        "blue" -> dices.copy(blue = count.toInt())
                        else -> throw IllegalArgumentException("The dice color: $color is not recognized!")
                    }
                }
            }

            Game(
                number = number.toInt(),
                reveals = parsedReveals
            )
        }
}
