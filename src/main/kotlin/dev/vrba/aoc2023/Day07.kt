package dev.vrba.aoc2023


private typealias Rank = Char

@Solved
object Day07 : Task<Int>(7, "Camel Cards") {

    override fun part1(lines: List<String>): Int {
        val ranks = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
        val rounds = parseInput(lines, ranks)

        return rounds.sortedDescending()
            .mapIndexed { index, round -> (index + 1) * round.bet }
            .sum()
    }

    override fun part2(lines: List<String>): Int {
        val ranks = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')
        val rounds = parseInput(lines, ranks, 'J')

        return rounds.sortedDescending()
            .mapIndexed { index, round -> (index + 1) * round.bet }
            .sum()
    }

    private fun parseInput(lines: List<String>, ranks: List<Rank>, joker: Rank? = null): List<Round> {
        return lines.map { line ->
            val (cards, bet) = line.split(" ").map { it.trim() }

            Round(
                hand = Hand(
                    cards = cards.map { Card(it, ranks) },
                    joker = joker
                ),
                bet = bet.toInt()
            )
        }
    }

    private data class Card(
        val rank: Rank,
        val ranks: List<Rank>
    ) : Comparable<Card> {
        override fun compareTo(other: Card): Int = ranks.indexOf(other.rank) - ranks.indexOf(rank)
    }

    private enum class Combination {
        FiveOfKind,
        FourOfKind,
        FullHouse,
        ThreeOfKind,
        TwoPair,
        Pair,
        HighCard
    }

    private data class Hand(
        val cards: List<Card>,
        val joker: Rank? = null
    ) : Comparable<Hand> {
        val bestCombination: Combination
            get() {
                val (firstWithoutJokers, second) = (
                        cards
                            .filter { it.rank != joker }
                            .groupBy { it.rank }
                            .map { it.value.size }
                            .sortedDescending() + listOf(0, 0)
                        )

                val jokers = joker?.let { joker -> cards.count { it.rank == joker } } ?: 0
                val first = firstWithoutJokers + jokers

                return when {
                    first == 5 -> Combination.FiveOfKind
                    first == 4 -> Combination.FourOfKind
                    first == 3 && second == 2 -> Combination.FullHouse
                    first == 3 -> Combination.ThreeOfKind
                    first == 2 && second == 2 -> Combination.TwoPair
                    first == 2 -> Combination.Pair
                    else -> Combination.HighCard
                }
            }

        override fun toString() = cards.map { it.rank }.joinToString("")

        override fun compareTo(other: Hand): Int {
            val comparison = bestCombination compareTo other.bestCombination

            if (comparison == 0) {
                return cards
                    .zip(other.cards)
                    .map { (first, second) -> first compareTo second }
                    .first { it != 0 }
            }

            return comparison
        }
    }

    private data class Round(
        val hand: Hand,
        val bet: Int
    ) : Comparable<Round> {
        override fun compareTo(other: Round): Int = hand compareTo other.hand
        override fun toString(): String = "$hand ${hand.bestCombination} ($bet)"
    }
}
