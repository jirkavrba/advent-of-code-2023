package dev.vrba.aoc

import java.lang.RuntimeException

abstract class Task(private val day: Int) {

    open fun part1(): Any {
        TODO("Part 1 is not implemented yet!")
    }

    open fun part2(): Any {
        TODO("Part 2 is not implemented yet!")
    }

    fun display() {
        println("Day $day")
        println("Task 1 output: ${part1()}")
        println("Task 2 output: ${part2()}")
    }

    protected fun loadInputLines(suffix: String = ""): List<String> =
        with("input_${day}${suffix}.txt") {
            AdventOfCode::class.java.getResource("/$this")
                ?.readText()
                ?.lines()
                ?: throw RuntimeException("Resource file $this not found.")
        }

}


