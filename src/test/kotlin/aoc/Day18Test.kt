package aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day18Test : BaseTest() {

	private val input = readInput("day18.txt")
	private val sample = readInput("day18.sample.txt")

	@Test
	fun part1() {
		assertEquals(35991, Day18(input).part1())
	}

	@Test
	fun part2() {
		assertEquals(54058824661845, Day18(input).part2())
	}

	@Test
	fun sample1() {
		assertEquals(62, Day18(sample).part1())
	}

	@Test
	fun sample2() {
		assertEquals(952408144115, Day18(sample).part2())
	}

}
