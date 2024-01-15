package aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day22Test : BaseTest() {

	private val input = readInput("day22.txt")
	private val sample = readInput("day22.sample.txt")

	@Test
	fun part1() {
		assertEquals(441, Day22(input).part1())
	}

	@Test
	fun part2() {
		assertEquals(80778, Day22(input).part2())
	}

	@Test
	fun sample1() {
		assertEquals(5, Day22(sample).part1())
	}

	@Test
	fun sample2() {
		assertEquals(7, Day22(sample).part2())
	}

}
