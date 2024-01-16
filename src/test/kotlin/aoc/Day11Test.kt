package aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11Test : BaseTest() {

	private val input = readInput("day11.txt")
	private val sample = readInput("day11.sample.txt")

	@Test
	fun part1() {
		assertEquals(9599070L, Day11(input).part1())
	}

	@Test
	fun part2() {
		assertEquals(842645913794L, Day11(input).part2(1000000))
	}

	@Test
	fun sample1() {
		assertEquals(374L, Day11(sample).part1())
	}

	@Test
	fun sample2a() {
		assertEquals(1030L, Day11(sample).part2(10))
	}

	@Test
	fun sample2b() {
		assertEquals(8410L, Day11(sample).part2(100))
	}

}
