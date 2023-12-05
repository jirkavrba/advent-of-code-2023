package dev.vrba.aoc

object Day03 : Task<Int>(3) {
    data class Position(
        val x: Int,
        val y: Int
    )

    data class NumberToken(
        val value: Int,
        val length: Int,
        val origin: Position,
    )

    data class SymbolToken(
        val symbol: String,
        val position: Position
    )

    override fun part1(): Int {
        val (numbers, symbols) = parseInput()
        val symbolPositions = symbols.map { it.position }.toSet()

        return numbers
            .filter { number -> number.neighbourPositions.any { it in symbolPositions } }
            .sumOf { it.value }
    }

    override fun part2(): Int {
        val (numbers, symbols) = parseInput()
        val numbersMap = numbers
            .flatMap { token -> token.occupiedPositions.map { it to token.value } }
            .fold(emptyMap<Position, Int>()) { map, positions -> map + positions }

        return symbols
            .filter { it.symbol == "*" }
            .sumOf { symbol ->
                val neighbourNumbers = symbol.neighbourPositions.mapNotNull { numbersMap[it] }.toSet()
                if (neighbourNumbers.size == 2) neighbourNumbers.reduce { a, b -> a * b } else 0
            }
    }

    private val SymbolToken.neighbourPositions: Set<Position>
        get() =
            (-1..1).flatMap { x ->
                (-1..1).map { y ->
                    Position(position.x + x, position.y + y)
                }
            }.toSet()

    private val NumberToken.neighbourPositions: Set<Position>
        get() =
            (-1..length).flatMap { x ->
                (-1..1).map { y ->
                    Position(origin.x + x, origin.y + y)
                }
            }.toSet()

    private val NumberToken.occupiedPositions: Set<Position>
        get() =
            (0..<length).map {
                Position(origin.x + it, origin.y)
            }.toSet()

    private fun parseInput(): Pair<Set<NumberToken>, Set<SymbolToken>> {
        data class ReductionState(
            val numbers: Set<NumberToken> = emptySet(),
            val symbols: Set<SymbolToken> = emptySet(),
            val currentNumberToken: NumberToken? = null,
        )

        return readInputLines().mapIndexed { y, line ->
            val result = line.foldIndexed(ReductionState()) { x, state, character ->
                when {
                    character.isDigit() -> state.copy(
                        currentNumberToken = state.currentNumberToken
                            ?.let {
                                it.copy(
                                    value = it.value * 10 + character.digitToInt(),
                                    length = it.length + 1
                                )
                            }
                            ?: NumberToken(
                                value = character.digitToInt(),
                                length = 1,
                                origin = Position(x, y)
                            )
                    )

                    character == '.' -> state.copy(
                        numbers = state.numbers + setOfNotNull(state.currentNumberToken),
                        currentNumberToken = null
                    )


                    else -> state.copy(
                        numbers = state.numbers + setOfNotNull(state.currentNumberToken),
                        symbols = state.symbols + SymbolToken(character.toString(), position = Position(x, y)),
                        currentNumberToken = null
                    )
                }
            }

            val numbers = result.numbers + setOfNotNull(result.currentNumberToken)
            val symbols = result.symbols

            numbers to symbols
        }
            .reduce { (accumulatedNumbers, accumulatedSymbols), (numbers, symbols) ->
                (accumulatedNumbers + numbers) to (accumulatedSymbols + symbols)
            }
    }
}
