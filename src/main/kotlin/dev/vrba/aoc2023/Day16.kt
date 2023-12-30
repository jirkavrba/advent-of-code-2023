package dev.vrba.aoc2023

import dev.vrba.aoc2023.Day16.Direction.Down
import dev.vrba.aoc2023.Day16.Direction.Left
import dev.vrba.aoc2023.Day16.Direction.Right
import dev.vrba.aoc2023.Day16.Direction.Up
import dev.vrba.aoc2023.Day16.Mirror.HorizontalSplit
import dev.vrba.aoc2023.Day16.Mirror.TopLeftMirror
import dev.vrba.aoc2023.Day16.Mirror.TopRightMirror
import dev.vrba.aoc2023.Day16.Mirror.VerticalSplit

@Solved
data object Day16 : Task<Int>(16, "The Floor Will Be Lava") {

    data class Position(val x: Int, val y: Int) {
        operator fun plus(other: Position) = Position(x + other.x, y + other.y)
        override fun toString() = "($x, $y)"
    }

    enum class Direction(private val arrow: String, val vector: Position) {
        Up("↑", Position(0, -1)),
        Down("↓", Position(0, 1)),
        Left("←", Position(-1, 0)),
        Right("→", Position(1, 0));

        override fun toString() = arrow
    }

    enum class Mirror(val reflections: (Direction) -> List<Direction>) {
        VerticalSplit({ listOf(Up, Down) }),
        HorizontalSplit({ listOf(Left, Right) }),

        // \
        TopRightMirror({
            when (it) {
                Left -> listOf(Up)
                Down -> listOf(Right)
                Up -> listOf(Left)
                Right -> listOf(Down)
            }
        }),

        // /
        TopLeftMirror({
            when (it) {
                Left -> listOf(Down)
                Down -> listOf(Left)
                Up -> listOf(Right)
                Right -> listOf(Up)
            }
        }),
    }

    data class Tile(
        val position: Position,
        val mirror: Mirror? = null,
    )

    data class Bounds(
        val width: Int,
        val height: Int,
    ) {
        operator fun contains(position: Position) =
            position.x in 0..<width && position.y in 0..<height
    }

    data class Beam(
        val position: Position,
        val direction: Direction,
    ) {
        override fun toString() = "($position $direction)"
    }

    data class ParsedInput(
        val bounds: Bounds,
        val map: Map<Position, Tile>,
    )

    override fun part1(lines: List<String>): Int {
        val (bounds, map) = parseInput(lines)
        val start = Beam(Position(-1, 0), Right)

        return numberOfEnergizedPositions(bounds, map, start)
    }

    override fun part2(lines: List<String>): Int {
        val (bounds, map) = parseInput(lines)

        val horizontalStartPositions = (0..<bounds.height).flatMap {
            listOf(
                Beam(Position(-1, it), Right),
                Beam(Position(bounds.width, it), Left)
            )
        }

        val verticalStartPositions = (0..<bounds.width).flatMap {
            listOf(
                Beam(Position(it, -1), Down),
                Beam(Position(it, bounds.height), Up)
            )
        }

        return (verticalStartPositions + horizontalStartPositions).maxOf {
            numberOfEnergizedPositions(bounds, map, it)
        }
    }

    private fun parseInput(lines: List<String>): ParsedInput {
        val bounds = Bounds(
            width = lines[0].length,
            height = lines.size
        )

        val tiles = lines.flatMapIndexed { y, line ->
            line.mapIndexed { x, char ->
                val position = Position(x, y)
                val mirror = when (char) {
                    '|' -> VerticalSplit
                    '-' -> HorizontalSplit
                    '\\' -> TopRightMirror
                    '/' -> TopLeftMirror
                    else -> null
                }

                Tile(position, mirror)
            }
        }

        val map = tiles.associateBy { it.position }

        return ParsedInput(bounds, map)
    }

    private fun numberOfEnergizedPositions(bounds: Bounds, map: Map<Position, Tile>, start: Beam): Int {
        val initial = listOf(start) to emptySet<Beam>()
        val iterations = generateSequence(initial) { (beams, visited) ->
            beams.flatMap { beam ->
                val next = beam.position + beam.direction.vector
                val mirror = map[next]?.mirror

                when {
                    beam in visited -> emptyList()
                    next !in bounds -> emptyList()
                    mirror == null -> listOf(beam.copy(position = next))
                    else -> mirror.reflections(beam.direction).map {
                        Beam(next, it)
                    }
                }
            } to visited + beams.filter { it.position in bounds }
        }

        val (_, settled) = iterations.first { it.first.isEmpty() }
        val positions = settled.map { it.position }.toSet()

        return positions.size
    }
}
