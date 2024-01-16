package aoc

/**
 * This puzzle compares columns and rows in a two-dimensional grid.
 *
 * The input file contains many grids.
 *
 * For each grid find a mirror column or row that meets these conditions:
 *
 *   1) Two consecutive columns or rows are the same
 *   2) Working outwards in each direction, equidistant columns or rows on each side are the same
 *   3) Stop when one side reaches the end
 *
 * For part 2 make copies of each grid, with one character switched in each copy.
 *
 * Only one of the changed grids should find a different mirror column or row.
 * Make sure to ignore the column or row found in the original (unmodified) grid, as it may still be a mirror.
 */
class Day13(private val input: List<String>) {

	/**
	 * Create a copy of the grid each time a single coordinate is changed to the opposite character.
	 */
	private fun getPossibleGrids(original: List<String>): List<List<String>> {
		val results = mutableListOf<List<String>>()

		original.forEachIndexed { indexLine, line ->
			line.forEachIndexed { indexChar, char ->
				// Switch the character to the opposite type
				val newChar = if (char == '.') "#" else "."

				// Create a new grid with the change applied
				val newLines = original.toMutableList()
				val newLine = line.replaceRange(indexChar, indexChar + 1, newChar)

				newLines[indexLine] = newLine

				results.add(newLines)
			}
		}

		return results
	}

	/**
	 * Find a mirror column in the grid, can be -1 if the mirror is a row instead.
	 */
	private fun getMirrorCol(lines: List<String>, ignore: Int? = null): Int {
		// Create strings containing the value from the same column of every row, i.e. vertical instead of horizontal
		// This will allow the getMirrorRow() function to be reused
		val newLines = mutableListOf<String>()

		for (i in lines.first().indices) {
			val row = lines.map { it[i] }.joinToString("")

			newLines.add(row)
		}

		return getMirrorRow(newLines, ignore)
	}

	/**
	 * Find a mirror row in the grid, can be -1 if the mirror is a column instead.
	 */
	private fun getMirrorRow(lines: List<String>, ignore: Int? = null): Int {
		val possibleResults = mutableListOf<Int>()

		for (index in 0 until lines.size - 1) {
			// For part 2 ignore the initial mirror result, as a second mirror column or row will appear
			if (lines[index] == lines[index + 1] && index != ignore) {
				possibleResults.add(index)
			}
		}

		if (possibleResults.isEmpty()) {
			return -1
		}

		// Check that the top or bottom edge can be reached - whichever is closer
		possibleResults.forEach { row ->
			// Get all rows on either side of the mirror
			var rowsBefore = lines.take(row)
			var rowsAfter = lines.drop(row + 2)

			// Create an equal size of rows on each side of the mirror - ignore any extra on one side only
			if (rowsBefore.size > rowsAfter.size) {
				rowsBefore = rowsBefore.drop(rowsBefore.size - rowsAfter.size)
			} else if (rowsBefore.size < rowsAfter.size) {
				rowsAfter = rowsAfter.dropLast(rowsAfter.size - rowsBefore.size)
			}

			// Compare
			// Moving outwards in opposite directions so one list has to be reversed
			if (rowsBefore == rowsAfter.reversed()) {
				return row
			}
		}

		return -1
	}

	fun part1(): Int {
		val grids = input.joinToString("\n").split("\n\n")

		val results = grids.map { grid ->
			val lines = grid.split("\n")

			// Only one of the two should return a result for each grid
			val mirrorCol = getMirrorCol(lines)
			val mirrorRow = getMirrorRow(lines)

			if (mirrorCol > -1) {
				mirrorCol + 1
			} else if (mirrorRow > -1) {
				(mirrorRow + 1) * 100
			} else {
				throw Exception("I AM ERROR")
			}
		}

		return results.sumOf { it }
	}

	fun part2(): Int {
		val grids = input.joinToString("\n").split("\n\n")

		val results = grids.map { grid ->
			val lines = grid.split("\n")
			var result = 0

			// Save the old grid result (column or row number) so that it can be ignored in the modified grids
			val ignoreCol = getMirrorCol(lines)
			val ignoreRow = getMirrorRow(lines)

			val possibleGrids = getPossibleGrids(lines)

			// Try every possible modified grid until a result is found
			possibleGrids.forEach { newLines ->
				// Do not need to keep checking this input grid if a result was found
				if (result == 0) {
					val mirrorCol = getMirrorCol(newLines, ignoreCol)
					val mirrorRow = getMirrorRow(newLines, ignoreRow)

					if (mirrorCol > -1) {
						result = mirrorCol + 1
					} else if (mirrorRow > -1) {
						result = (mirrorRow + 1) * 100
					}
				}
			}

			result
		}

		return results.sumOf { it }
	}

}
