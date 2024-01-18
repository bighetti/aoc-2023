package aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day14Test : BaseTest() {

	private val input = readInput("day14.txt")
	private val sample = readInput("day14.sample.txt")

	@Test
	fun part1() {
		assertEquals(108840, Day14(input).part1())
	}

	@Test
	fun part2() {
		assertEquals(103445, Day14(input).part2())
	}

	@Test
	fun sample1() {
		assertEquals(136, Day14(sample).part1())
	}

	@Test
	fun sample2() {
		assertEquals(64, Day14(sample).part2())
	}

}
