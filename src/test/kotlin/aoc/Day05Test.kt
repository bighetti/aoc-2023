package aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day05Test : BaseTest() {

	private val input = readInput("day05.txt")
	private val sample = readInput("day05.sample.txt")

	@Test
	fun part1() {
		assertEquals(525792406L, Day05(input).part1())
	}

	@Test
	fun part2() {
		assertEquals(79004094L, Day05(input).part2())
	}

	@Test
	fun sample1() {
		assertEquals(35L, Day05(sample).part1())
	}

	@Test
	fun sample2() {
		assertEquals(46L, Day05(sample).part2())
	}

}
