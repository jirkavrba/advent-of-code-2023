package dev.vrba.aoc2023

object AdventOfCode2023

fun main() {
    Task::class.sealedSubclasses
        .mapNotNull { it.objectInstance }
        .sortedBy { it.day }
        .forEach { it.run() }
}

@Target(AnnotationTarget.CLASS)
annotation class Solved

@Target(AnnotationTarget.CLASS)
annotation class UseExampleInput(val suffix: String = "_example")

@Target(AnnotationTarget.CLASS)
annotation class UseWholeFileAsInput

sealed class Task<T : Number>(
    val day: Int,
    private val title: String? = null
) {
    protected open fun part1(lines: List<String>): T =
        TODO("Part 1 is not implemented yet!")

    protected open fun part2(lines: List<String>): T =
        TODO("Part 2 is not implemented yet!")

    fun run() {
        val annotations = this::class.annotations
        val solved = annotations.any { it is Solved }

        println("${if (solved) "✅" else "\uD83C\uDF81"} Day $day" + (title?.let { ": $it" } ?: ""))

        if (solved) {
            return
        }

        val suffix = annotations
            .filterIsInstance<UseExampleInput>()
            .firstOrNull()
            ?.suffix
            ?: ""

        val file = "./input_${day}${suffix}"
        val input = AdventOfCode2023::class.java
            .classLoader
            .getResource(file)
            ?.readText()
            ?.let { content ->
                if (annotations.any { it is UseWholeFileAsInput }) listOf(content)
                else content.lines().filter { it.isNotBlank() }
            }
            ?: throw IllegalArgumentException("Input file $file not found!")


        println("Part 1: ${try { part1(input) } catch (_: NotImplementedError) { "❄\uFE0F Not implemented yet" } }")
        println("Part 2: ${try { part2(input) } catch (_: NotImplementedError) { "❄\uFE0F Not implemented yet" } }")
    }
}

