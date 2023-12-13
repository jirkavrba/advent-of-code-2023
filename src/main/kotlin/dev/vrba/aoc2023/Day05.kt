package dev.vrba.aoc2023

@Solved
object Day05 : Task<Long>(5, "If You Give A Seed A Fertilizer") {

    override fun part1(lines: List<String>): Long {
        val almanac = parseInput(lines)
        val locations = almanac.seeds.map { seed ->
            almanac.mappings.fold(seed) { value, mapping ->
                mapping.ranges.firstOrNull { it.matches(value) }
                    ?.convert(value)
                    ?: value
            }
        }

        return locations.min()
    }

    private data class Almanac(
        val seeds: List<Long>,
        val mappings: List<Mapping>
    )

    private data class Mapping(
        val ranges: List<MappingRange>
    )

    private data class MappingRange(
        val targetStart: Long,
        val sourceStart: Long,
        val length: Long,
    ) {
        fun matches(input: Long): Boolean = input in sourceStart..<(sourceStart + length)
        fun convert(input: Long): Long = (input - sourceStart) + targetStart

        override fun toString(): String = "$sourceStart..${sourceStart + length} -> $targetStart..${targetStart + length}"
    }

    private fun parseInput(lines: List<String>): Almanac {
        val seeds = lines.first().removePrefix("seeds: ").split(" ").map { it.toLong() }

        val initial = emptyList<Mapping>() to emptyList<MappingRange>()
        val (parsedMappings, lastMapping) = lines.drop(2).fold(initial) { reduction, line ->
            val (mappings, ranges) = reduction

            when {
                line.isBlank() -> reduction
                line.matches("^[a-z].*".toRegex()) -> (mappings + Mapping(ranges)) to emptyList()
                else -> {
                    val (targetStart, sourceStart, length) = line.trim().split(" ").map { it.toLong() }
                    mappings to ranges + MappingRange(
                        targetStart = targetStart,
                        sourceStart = sourceStart,
                        length = length
                    )
                }
            }
        }

        return Almanac(
            seeds = seeds,
            mappings = parsedMappings + Mapping(lastMapping)
        )
    }

}
