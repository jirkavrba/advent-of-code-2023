package dev.vrba.aoc

object Day05 : Task<Long>(5) {

    data class Almanac(
        val seeds: List<Long>,
        val mappings: List<Mapping>
    )

    data class Mapping(
        val ranges: List<MappingRange>
    )

    data class MappingRange(
        val targetStart: Long,
        val sourceStart: Long,
        val length: Long,
    ) {
        fun matches(input: Long): Boolean = input in sourceStart..<(sourceStart + length)
        fun convert(input: Long): Long = (input - sourceStart) + targetStart

        override fun toString(): String = "$sourceStart..${sourceStart + length} -> $targetStart..${targetStart + length}"
    }



    override fun part1(): Long {
        val almanac = parseInput()
        val locations = almanac.seeds.map { seed ->
            almanac.mappings.fold(seed) { value, mapping ->
                mapping.ranges.firstOrNull { it.matches(value) }
                    ?.convert(value)
                    ?: value
            }
        }

        return locations.min()
    }

    private fun parseInput(): Almanac {
        val lines = readInputLines()
        val seeds = lines.first().removePrefix("seeds: ").split(" ").map { it.toLong() }

        val initial = emptyList<Mapping>() to emptyList<MappingRange>()
        val (parsedMappings, lastMapping) = lines.drop(2).fold(initial) { reduction, line ->
            val (mappings, ranges) = reduction

            when {
                line.isBlank()  -> reduction
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
