package dev.vrba.aoc2023

data object Day14 : Task<Int>(14, "Parabolic Reflector Dish") {

    data class ColumnPart(
        val rocks: Int,
        val offset: Int,
    )

    override fun part1(lines: List<String>): Int {
        val columns = lines.flip()
        val partialResults = columns.map { column ->
            val parts = column.split("#")
            val result = parts.fold(emptyList<ColumnPart>() to 0) { (parts, offset), part ->
                val rocks = part.count { it == 'O' }
                val newOffset = offset + part.length + 1

                parts + ColumnPart(rocks, offset) to newOffset
            }

            result.first.filter { it.rocks > 0 }
        }

        val weights = lines.size downTo 0

        return partialResults
            .flatten()
            .flatMap { weights.drop(it.offset).take(it.rocks) }
            .sum()
    }


    private fun List<String>.flip(): List<String> =
        first().indices.map { index ->
            map { row -> row[index] }.joinToString("")
        }
}
