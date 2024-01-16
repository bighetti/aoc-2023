package aoc

import aoc.data.Galaxy11

/**
 * This puzzle uses a data structure (Galaxy11) to manage coordinates in a two-dimensional grid.
 *
 * Begin by recording the XY position of each galaxy on the grid.
 *
 * For part 1 calculate the sum of distances between each pair.
 *
 * For part 2 find all the empty column or row numbers and multiply them.
 * Start from the end so that all the previous (smaller) numbers do not need to be readjusted each time.
 */
class Day11(private val input: List<String>) {

	/**
	 * Parse the input grid into galaxy objects.
	 *
	 * Multiply the number of empty columns and rows, a value of 1 will do nothing.
	 */
	private fun parse(input: List<String>, multiply: Int): List<Galaxy11> {
		val results = mutableListOf<Galaxy11>()

		val emptyCols = mutableListOf<Int>()
		val emptyRows = mutableListOf<Int>()

		// Create
		// Find the empty rows
		input.forEachIndexed { row, line ->
			if (!line.contains("#")) {
				emptyRows.add(row)
			}

			line.forEachIndexed { col, char ->
				if (char == '#') {
					results.add(
						Galaxy11(results.size + 1, row.toLong(), col.toLong())
					)
				}
			}
		}

		// Find the empty columns
		for (c in 0 until input.first().length) {
			if (results.find { it.col == c.toLong() } == null) {
				emptyCols.add(c)
			}
		}

		// Expand
		// Start from the end so that all following empty numbers do not need to adjusted also.
		emptyCols.reversed().forEach { c ->
			// Update the column number for any galaxy beyond this column
			results.filter { it.col > c }.forEach { galaxy ->
				galaxy.col += multiply
			}
		}

		emptyRows.reversed().forEach { r ->
			// Update the row number for any galaxy beyond this row
			results.filter { it.row > r }.forEach { galaxy ->
				galaxy.row += multiply
			}
		}

		return results
	}

	fun part1(): Long {
		val galaxies = parse(input, 1)

		return solve(galaxies)
	}

	fun part2(increase: Int): Long {
		// Subtract one for the empty column or row that already exists
		val galaxies = parse(input, increase - 1)

		return solve(galaxies)
	}

	private fun solve(galaxies: List<Galaxy11>): Long {
		val distances = galaxies.map { galaxy ->
			// Don't count a pair twice
			galaxies.filter { it.id > galaxy.id }.map {
				galaxy.getDistance(it)
			}
		}

		return distances.flatten().sumOf { it }
	}

}
