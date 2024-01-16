package aoc.data

import kotlin.math.absoluteValue

/**
 * A galaxy represents a "#" position on the grid.
 */
class Galaxy11(val id: Int, var row: Long, var col: Long) {

	/**
	 * Basic manhattan distance between two XY coordinates.
	 */
	fun getDistance(other: Galaxy11): Long {
		val colDiff = (col - other.col).absoluteValue
		val rowDiff = (row - other.row).absoluteValue

		return colDiff + rowDiff
	}

	override fun toString(): String {
		return "id=$id row=$row col=$col"
	}

}
