package dev.vrba.aoc2023

@Solved
data object Day10 : Task<Long>(10, "Pipe Maze") {

    override fun part1(lines: List<String>): Long {
        val (start, map) = parseInput(lines)
        val loop = findLoop(start, map)

        return loop.size / 2L
    }

    private fun parseInput(lines: List<String>): Pair<Tile, TileMap> {
        val tiles = lines.flatMapIndexed { y, line ->
            line.mapIndexed { x, char ->
                char.toTile(x, y)
            }
        }

        val map: TileMap = tiles.associateBy { it.position }
        val start = tiles.first { it.type == TileType.StartingPosition }

        return start to map
    }
}

private typealias TileMap = Map<Position, Tile>

private enum class Direction(val shift: Position) {
    Up(Position(0, -1)),
    Left(Position(-1, 0)),
    Right(Position(1, 0)),
    Down(Position(0, 1))
}

private enum class TileType(val symbol: Char, val directions: Set<Direction>) {
    VerticalPipe('|', setOf(Direction.Up, Direction.Down)),
    HorizontalPipe('-', setOf(Direction.Left, Direction.Right)),
    NorthEastBendPipe('L', setOf(Direction.Up, Direction.Right)),
    NorthWestBendPipe('J', setOf(Direction.Up, Direction.Left)),
    SouthWestBendPipe('7', setOf(Direction.Left, Direction.Down)),
    SouthEastBestPipe('F', setOf(Direction.Right, Direction.Down)),
    StartingPosition('S', Direction.entries.toSet()),
    Ground('.', emptySet()),
}

private data class Position(
    val x: Int,
    val y: Int
) {
    operator fun plus(other: Position): Position =
        Position(x + other.x, y + other.y)
}

private data class Tile(
    val type: TileType,
    val position: Position,
    val marked: Boolean = false
)

private val tilesFromSymbols = TileType.entries.associateBy { it.symbol }

private fun Char.toTile(x: Int, y: Int): Tile =
    tilesFromSymbols[this]
        ?.let { type -> Tile(type = type, position = Position(x, y)) }
        ?: throw RuntimeException("No tile type associated with the symbol $this")


private fun Tile.connectedPipes(map: TileMap): Set<Tile> =
    type.directions
        .mapNotNull { map[position + it.shift] }
        .toSet()

private fun Tile.neighbours(map: TileMap): Set<Tile> =
    Direction.entries
        .mapNotNull { map[position + it.shift] }
        .toSet()

private tailrec fun findLoop(start: Tile, map: TileMap, visitedNodes: List<Tile> = emptyList()): List<Tile> {
    val connections = start.connectedPipes(map).filter { it.connectedPipes(map).contains(start) }
    val availableConnections = connections - visitedNodes.toSet()

    // Reached the full loop
    if (availableConnections.isEmpty() && connections.any { it.type == TileType.StartingPosition }) {
        return visitedNodes + start
    }

    return findLoop(availableConnections.random(), map, visitedNodes + start)
}


