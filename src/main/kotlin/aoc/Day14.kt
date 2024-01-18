package aoc

import aoc.data.Rock14

/**
 * This puzzle has to find a way to avoid simulating every possible outcome, since it would take a very long time.
 *
 * A pattern of repeating results is found by simulating a small number of scenarios.
 * The pattern can be used to jump to any future iteration number.
 */
class Day14(val input: List<String>) {

	/**
	 * Return a string containing all rock positions and values, for comparison.
	 */
	private fun getPrintable(rocks: Map<String, Rock14>): String {
		return rocks.values.joinToString(" ") {
			"${it.row}.${it.col}.${it.type}"
		}
	}

	/**
	 * Return the sum of all rock "load" values.
	 *
	 * A load value is <amount of rows> - <row index of rock>.
	 *
	 * If there are 10 rows and the rock is in the first row (index 0) the value is 10.
	 */
	private fun getResult(rocks: Collection<Rock14>): Int {
		val rows = input.size

		return rocks.filter {
			it.type == 'O' // Ignore the "#" rocks
		}.sumOf {
			rows - it.row
		}
	}

	/**
	 * Create a map of all "O" and "#" rocks by position.
	 */
	private fun parse(): MutableMap<String, Rock14> {
		val results = mutableMapOf<String, Rock14>()

		input.forEachIndexed { row, line ->
			line.forEachIndexed { col, char ->
				if (char != '.') {
					results["$row.$col"] = Rock14(row, col, char)
				}
			}
		}

		return results
	}

	/**
	 * Tilt once and get the result.
	 */
	fun part1(): Int {
		val rocks = parse()

		tiltNorth(rocks)

		return getResult(rocks.values)
	}

	/**
	 * Keep tilting (all four directions per iteration) until the same result appears twice.
	 * Save the iteration number with each result to find how long it takes for a repeat to occur.
	 *
	 * The iteration number and gap can be used to "jump" towards 1,000,000,000 iterations.
	 * For example if the same result occurred at 60 and 260 it will also occur at 999,999,860.
	 *
	 * Calculate the difference between the "jump" and 1,000,000,000 to determine which saved result to retrieve.
	 * For example 1,000,000,000 - 999,999,860 = 140 means the result will be saved at iteration 200 (60 + 140).
	 */
	fun part2(): Int {
		val rocks = parse()

		var count = 1
		val grids = mutableMapOf<String, Int>() // Grid pattern -> Iteration number
		var repeatSize = 0
		var repeatFrom = 0
		val results = mutableMapOf<Int, Int>() // Iteration number -> Load

		while (repeatSize == 0) {
			tiltNorth(rocks)
			tiltWest(rocks)
			tiltSouth(rocks)
			tiltEast(rocks)

			val printable = getPrintable(rocks)

			repeatFrom = grids[printable] ?: 0

			if (repeatFrom != 0) {
				repeatSize = count - repeatFrom
			}

			grids[printable] = count
			results[count] = getResult(rocks.values)

			count++
		}

		var jumpTo = repeatFrom + (1000000000 / repeatSize) * repeatSize

		// If jumped past 1,000,000,000 reduce by one "repeat size" each time.
		while (jumpTo > 1000000000) {
			jumpTo -= repeatSize
		}

		val difference = 1000000000 - jumpTo

		// Get the required result from the saved results.
		val result = results[repeatFrom + difference] ?: results[repeatFrom + difference - results.size]

		return result ?: 0
	}

	/**
	 * Separate tilt for each direction for readability.
	 */
	private fun tiltEast(rocks: MutableMap<String, Rock14>) {
		val colMax = input.first().length - 1

		for (c in colMax - 1 downTo 0) {
			for (r in input.indices) {
				val rock = rocks["$r.$c"]

				if (rock != null && rock.type == 'O') {
					var finished = false

					while (!finished && rock.col < colMax) {
						val above = rocks["$r.${rock.col + 1}"]

						if (above == null) {
							rock.col++
						} else {
							finished = true
						}
					}

					// Update map keys
					if (rock.col != c) {
						rocks.remove("$r.$c")

						rocks["${rock.row}.${rock.col}"] = rock
					}
				}
			}
		}
	}

	private fun tiltNorth(rocks: MutableMap<String, Rock14>) {
		val width = input.first().length

		for (r in 1 until input.size) {
			for (c in 0 until width) {
				val rock = rocks["$r.$c"]

				if (rock != null && rock.type == 'O') {
					var finished = false

					while (!finished && rock.row > 0) {
						val above = rocks["${rock.row - 1}.$c"]

						if (above == null) {
							rock.row--
						} else {
							finished = true
						}
					}

					// Update map keys
					if (rock.row != r) {
						rocks.remove("$r.$c")

						rocks["${rock.row}.${rock.col}"] = rock
					}
				}
			}
		}
	}

	private fun tiltSouth(rocks: MutableMap<String, Rock14>) {
		val rowMax = input.size - 1
		val width = input.first().length

		for (r in rowMax - 1 downTo 0) {
			for (c in 0 until width) {
				val rock = rocks["$r.$c"]

				if (rock != null && rock.type == 'O') {
					var finished = false

					while (!finished && rock.row < rowMax) {
						val above = rocks["${rock.row + 1}.$c"]

						if (above == null) {
							rock.row++
						} else {
							finished = true
						}
					}

					// Update map keys
					if (rock.row != r) {
						rocks.remove("$r.$c")

						rocks["${rock.row}.${rock.col}"] = rock
					}
				}
			}
		}
	}

	private fun tiltWest(rocks: MutableMap<String, Rock14>) {
		val width = input.first().length

		for (c in 1 until width) {
			for (r in input.indices) {
				val rock = rocks["$r.$c"]

				if (rock != null && rock.type == 'O') {
					var finished = false

					while (!finished && rock.col > 0) {
						val above = rocks["$r.${rock.col - 1}"]

						if (above == null) {
							rock.col--
						} else {
							finished = true
						}
					}

					// Update map keys
					if (rock.col != c) {
						rocks.remove("$r.$c")

						rocks["${rock.row}.${rock.col}"] = rock
					}
				}
			}
		}
	}

}
