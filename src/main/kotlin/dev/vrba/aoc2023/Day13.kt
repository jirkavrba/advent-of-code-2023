package dev.vrba.aoc2023

@Solved
@UseWholeFileAsInput
data object Day13 : Task<Int>(13, "Point of Incidence") {

    override fun part1(lines: List<String>): Int {
        return parseInput(lines).sumOf { pattern ->
            pattern.rows.indexesOfMirrors().sumOf { it * 100 } +
                    pattern.columns.indexesOfMirrors().sum()
        }
    }

    override fun part2(lines: List<String>): Int {
        return parseInput(lines).sumOf { pattern ->
            pattern.rows.indexesOfMirrors(1).sumOf { it * 100 } +
                    pattern.columns.indexesOfMirrors(1).sum()
        }
    }

    private data class Pattern(
        val rows: List<String>,
        val columns: List<String>,
    )

    private fun parseInput(lines: List<String>): List<Pattern> {
        return lines
            .first()
            .split("\n\n")
            .map { block ->
                val rows = block.trim().split("\n").filter { it.isNotBlank() }
                val columns = rows.flip()

                Pattern(
                    rows = rows,
                    columns = columns
                )
            }
    }

    private fun List<String>.flip(): List<String> =
        first().indices.map { index ->
            map { row ->
                row[index]
            }.joinToString("")
        }

    private fun List<String>.indexesOfMirrors(smudges: Int = 0): List<Int> {
        return indices.drop(1).filter { split ->
            val first = take(split).reversed()
            val second = drop(split)

            first.zip(second).sumOf { (a, b) ->
                a.zip(b).count {
                    it.first != it.second
                }
            } == smudges
        }
    }
}
