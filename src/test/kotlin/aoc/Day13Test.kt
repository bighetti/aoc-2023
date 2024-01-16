package aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day13Test : BaseTest() {

	private val input = readInput("day13.txt")
	private val sample = readInput("day13.sample.txt")

	@Test
	fun part1() {
		assertEquals(27505, Day13(input).part1())
	}

	@Test
	fun part2() {
		assertEquals(22906, Day13(input).part2())
	}

	@Test
	fun sample1() {
		assertEquals(405, Day13(sample).part1())
	}

	@Test
	fun sample2() {
		assertEquals(400, Day13(sample).part2())
	}

}
