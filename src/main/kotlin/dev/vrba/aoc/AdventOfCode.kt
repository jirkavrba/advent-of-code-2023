package dev.vrba.aoc

import dev.vrba.aoc.day01.Day01
import dev.vrba.aoc.day01.Day02

object AdventOfCode

fun main() {
    println("\uD83C\uDF84 Advent of code 2023 \uD83C\uDF84")

    val solvedDays = listOf(
        Day01,
        Day02
    )

    solvedDays.forEach {
        it.display()
    }
}
