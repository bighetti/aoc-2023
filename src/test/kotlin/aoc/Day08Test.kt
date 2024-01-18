package aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day08Test : BaseTest() {

	private val input = readInput("day08.txt")
	private val sample = readInput("day08.sample.txt")
	private val sample2 = readInput("day08.sample2.txt")
	private val sample3 = readInput("day08.sample3.txt")

	@Test
	fun part1() {
		assertEquals(18673L, Day08(input).part1())
	}

	@Test
	fun part2() {
		assertEquals(17972669116327L, Day08(input).part2())
	}

	@Test
	fun sample1() {
		assertEquals(2L, Day08(sample).part1())
	}

	@Test
	fun sample2a() {
		assertEquals(6L, Day08(sample2).part2())
	}

	@Test
	fun sample2b() {
		assertEquals(6L, Day08(sample3).part2())
	}

}
