package dev.vrba.aoc2023

@Solve
object Day08 : Task<Long>(8, "Haunted Wasteland") {

    override fun part1(lines: List<String>): Long {
        val (directions, nodes) = parseInput(lines)

        return directions.runningFold(nodes["AAA"]) { node, direction -> nodes[node.navigate(direction)] }
            .withIndex()
            .dropWhile { it.value.name != "ZZZ" }
            .first()
            .index
            .toLong()
    }

    override fun part2(lines: List<String>): Long {
        val (directions, nodes) = parseInput(lines)

        val startNodes = nodes.filterKeys { it.endsWith("A") }.values
        val trajectories = startNodes.map { start ->
            directions.runningFold(start) { node, direction -> nodes[node.navigate(direction)] }
                .withIndex()
                .dropWhile { !it.value.name.endsWith("Z") }
                .first()
                .index
                .toLong()
        }

        return trajectories.reduce(::lcm)
    }

    private enum class Direction {
        Left,
        Right
    }

    private data class Node(
        val name: String,
        val left: String,
        val right: String,
    )

    private fun Node.navigate(direction: Direction) =
        when (direction) {
            Direction.Left -> left
            Direction.Right -> right
        }

    private fun <T> Sequence<T>.repeated() =
        generateSequence(this) { it }.flatten()


    // Map that replaces the default get operator by an asserted call
    private class TrustMeBroMap<K, V>(val source: Map<K, V>) : Map<K, V> by source {
        override operator fun get(key: K): V = source[key] ?: throw NullPointerException(":)")
    }

    private fun <K, V> Map<K, V>.legit(): TrustMeBroMap<K, V> = TrustMeBroMap(this)

    private fun parseInput(lines: List<String>): Pair<Sequence<Direction>, TrustMeBroMap<String, Node>> {
        val directions = lines.first()
            .map { if (it == 'L') Direction.Left else Direction.Right }
            .asSequence()
            .repeated()

        val nodes = lines
            .drop(1)
            .dropWhile { it.isBlank() }
            .associate { line ->
                val (name, branches) = line.split(" = ")
                val (left, right) = branches.removeSurrounding("(", ")").split(", ")

                name to Node(name, left, right)
            }.legit()

        return directions to nodes
    }

    private fun lcm(x: Long, y: Long) =
        x * y / gcd(x, y)

    private tailrec fun gcd(x: Long, y: Long): Long =
        if (y == 0L) x else gcd(y, x % y)

}
