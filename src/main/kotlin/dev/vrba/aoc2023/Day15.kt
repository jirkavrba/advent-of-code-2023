package dev.vrba.aoc2023

@Solved
@UseWholeFileAsInput
data object Day15 : Task<Int>(15, "Lens Library") {

    data class Lens(
        val label: String,
        val focalLength: Int,
    ) {
        override fun toString() = "[$label $focalLength]"
    }

    override fun part1(lines: List<String>): Int {
        val words = lines.first().replace("\n", "").split(',')
        val hashes = words.map { it.holidayHash() }

        return hashes.sum()
    }

    override fun part2(lines: List<String>): Int {
        val instructions = lines.first().replace("\n", "").split(',')
        val initial = emptyMap<Int, List<Lens>>()
        val updated = instructions.fold(initial) { slots, instruction ->
            val label = instruction.takeWhile { it != '-' && it != '=' }
            val slot = label.holidayHash()

            when {
                instruction.endsWith('-') -> slots.modify(slot) { lenses ->
                    lenses?.filter { it.label != label } ?: emptyList()
                }

                else -> {
                    val lens = Lens(label, instruction.split("=").last().toInt())
                    slots.modify(slot) { value ->
                        val lenses = value ?: emptyList()

                        if (lenses.any { it.label == label }) lenses.map { if (it.label == label) lens else it }
                        else lenses + lens
                    }
                }
            }
        }

        return updated.entries.sumOf { (hash, lenses) ->
            lenses.withIndex().sumOf {
                (hash + 1) * (it.index + 1) * it.value.focalLength
            }
        }
    }

    private fun String.holidayHash(): Int = fold(0) { previous, current ->
        (previous + current.code) * 17 % 256
    }

    private inline fun <K, V> Map<K, V>.modify(key: K, transform: (V?) -> V): Map<K, V> = this + mapOf(key to transform(this[key]))

}
