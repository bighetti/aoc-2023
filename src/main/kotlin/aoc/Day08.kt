package aoc

import aoc.data.Network8

/**
 * This puzzle has to find a way to avoid testing one iteration at a time, since it would take a very long time.
 * It requires a mathematical equation (lowest common multiple) to calculate the final result.
 *
 * For part 1 begin from "AAA".
 * For part 2 begin from each name that ends with "A".
 *
 * Process each left (L) or (R) instruction to determine what the next destination is.
 *
 *   1) Keep count of the steps
 *   2) Use the remainder of "steps % instruction length" to avoid reaching the end of the instructions
 *
 * For part 1 stop when "ZZZ" is reached.
 * For part 2 stop when a name ending with "Z" is reached.
 *
 * For part 2 the number of steps when all tests will reach "Z" at the same time needs to be found.
 * Save the result of each test and then calculate the LCM of all values.
 */
class Day08(val input: List<String>) {

	private fun findLCM(first: Long, second: Long): Long {
		val larger = if (first > second) first else second
		val maxLcm = first * second
		var lcm = larger

		while (lcm <= maxLcm) {
			if (lcm % first == 0L && lcm % second == 0L) {
				return lcm
			}

			lcm += larger // Keep multiplying the larger value until a match is found
		}

		return maxLcm
	}

	private fun findLCM(numbers: List<Long>): Long {
		var result = numbers[0]

		for (i in 1 until numbers.size) {
			result = findLCM(result, numbers[i]) // Carry over the best result from a pair
		}

		return result
	}

	private fun parse(): Map<String, Network8> {
		val result = mutableMapOf<String, Network8>()

		input.drop(2).forEach { line ->
			val name = line.substringBefore(" = ")
			val left = line.substringAfter("(").substringBefore(", ")
			val right = line.substringAfter(", ").substringBefore(")")

			result[name] = Network8(name, left, right)
		}

		return result
	}

	fun part1(): Long {
		val instructions = input[0]
		val maps = parse()

		val map = maps["AAA"] ?: throw Exception("Not found: AAA")

		return solve(instructions, maps, map, "ZZZ")
	}

	fun part2(): Long {
		val instructions = input[0]
		val maps = parse()

		val currentMaps = maps.values.filter { it.name.endsWith("A") }
		val results = mutableMapOf<String, Long>() // name -> number of steps

		// Get the result for each **A to **Z process
		currentMaps.forEach { map ->
			val result = solve(instructions, maps, map, "Z")

			results[map.name] = result
		}

		return findLCM(results.values.toList())
	}

	/**
	 * For part 1 there is only one destination - equals "ZZZ".
	 * For part 2 there is a corresponding destination for each origin - ends with "Z"
	 *
	 * Stop when the destination is reached and return the number of steps.
	 *
	 * The number of steps may be more than the number of instructions, use the remainder for the instruction index.
	 */
	private fun solve(instructions: String, maps: Map<String, Network8>, map: Network8, endsWith: String): Long {
		var currentMap = map
		var finished = false
		var result = 0L

		while (!finished) {
			val index = (result % instructions.length).toInt() // Resets from 0 if result >= instructions.length
			val instruction = instructions[index]

			currentMap = if (instruction == 'L') {
				maps[currentMap.left] ?: throw Exception("Not found: ${currentMap.left}") // Never errors
			} else {
				maps[currentMap.right] ?: throw Exception("Not found: ${currentMap.right}") // Never errors
			}

			finished = currentMap.name.endsWith(endsWith)

			result++
		}

		return result
	}

}
