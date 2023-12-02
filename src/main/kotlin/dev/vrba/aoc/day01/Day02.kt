package dev.vrba.aoc.day01

import dev.vrba.aoc.Task

object Day02 : Task(2) {
    data class Dices(
        val red: Int = 0,
        val green: Int = 0,
        val blue: Int = 0,
    )

    data class Game(
        val number: Int,
        val reveals: List<Dices>
    )

    override fun part1(): Any {
        val bag = Dices(
            red = 12,
            green = 13,
            blue = 14
        )

        return parseInput()
            .filter { it.isPossibleWithBag(bag) }
            .sumOf { it.number }
    }

    private fun Game.isPossibleWithBag(bag: Dices) =
        reveals.all {
            it.red <= bag.red &&
            it.green <= bag.green &&
            it.blue <= bag.blue
        }

    private fun parseInput() =
        loadInputLines()
            .map { it.parseGame() }

    private fun String.parseGame(): Game {
        val (number, reveals) = removePrefix("Game ").split(": ")
        val parsedReveals = reveals.split("; ").map {
            it.split(", ").fold(Dices()) { dices, part ->
                println(part)
                val (count, color) = part.split(" ")

                when (color) {
                    "red" -> dices.copy(red = count.toInt())
                    "green" -> dices.copy(green = count.toInt())
                    "blue" -> dices.copy(blue = count.toInt())
                    else -> throw IllegalArgumentException("The dice color: $color is not recognized!")
                }
            }
        }

        return Game(
            number = number.toInt(),
            reveals = parsedReveals
        )
    }
}
