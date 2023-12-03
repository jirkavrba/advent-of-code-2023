package dev.vrba.aoc

object AdventOfCode

fun main() {
    println("\uD83C\uDF84 Advent of code 2023 \uD83C\uDF84")

    val solvedDays = listOf(
        Day01,
        Day02,
        Day03
    )

    solvedDays.forEach {
        it.display()
    }
}
