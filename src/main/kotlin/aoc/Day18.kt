package aoc

import kotlin.math.abs

/**
 * This puzzle has to find a way to avoid "space filling" inside a boundary, since it would take a long time.
 *
 * The shoelace formula is used to calculate the area - https://en.wikipedia.org/wiki/Shoelace_formula.
 */
class Day18(val input: List<String>) {

	/**
	 * Get the direction from the input data, the value is encoded for part 2.
	 */
	private fun getDirection(line: String, two: Boolean): String {
		return if (two) {
			val encoded = line.substringAfter("#").dropLast(1)

			encoded.takeLast(1).replace("0", "R").replace("1", "D").replace("2", "L").replace("3", "U")
		} else {
			line.substringBefore(" ")
		}
	}

	/**
	 * Get the distance from the input data, the value is hex encoded for part 2.
	 */
	private fun getDistance(line: String, two: Boolean): Long {
		return if (two) {
			val encoded = line.substringAfter("#").dropLast(1)

			encoded.dropLast(1).toLong(16)
		} else {
			line.substringAfter(" ").substringBefore(" ").toLong()
		}
	}

	/**
	 * Get the next XY coordinate according to:
	 *
	 *   1) Current XY coordinate
	 *   2) Next direction
	 *   3) Next distance
	 */
	private fun getNextPosition(currentPosition: Pair<Long, Long>, direction: String, distance: Long): Pair<Long, Long> {
		val currentCol = currentPosition.second
		val currentRow = currentPosition.first

		return when (direction) {
			"D" -> Pair(currentRow + distance, currentCol)
			"L" -> Pair(currentRow, currentCol - distance)
			"R" -> Pair(currentRow, currentCol + distance)
			else -> Pair(currentRow - distance, currentCol) // U
		}
	}

	/**
	 * Follow all the steps (direction and distance).
	 *
	 * Return the total number of steps taken.
	 */
	private fun parse(two: Boolean): Long {
		var currentPosition = Pair(0L, 0L)

		var results = 1L // Starting spot

		input.forEach { line ->
			val direction = getDirection(line, two)
			val distance = getDistance(line, two)

			currentPosition = getNextPosition(currentPosition, direction, distance)
			results += distance
		}

		return results
	}

	/**
	 * Follow all the steps (direction and distance).
	 *
	 * Return all the edges (position after each direction and distance is applied).
	 */
	private fun parseEdges(two: Boolean): MutableList<Pair<Long, Long>> {
		var currentPosition = Pair(0L, 0L)

		val results = mutableListOf(
			Pair(currentPosition.first, currentPosition.second)
		)

		input.forEach { line ->
			val direction = getDirection(line, two)
			val distance = getDistance(line, two)

			currentPosition = getNextPosition(currentPosition, direction, distance)

			results.add(currentPosition)
		}

		return results
	}

	fun part1(): Long {
		val cubes = parse(false)
		val edges = parseEdges(false)

		return solveShoeLace(cubes, edges)
	}

	fun part2(): Long {
		val cubes = parse(true)
		val edges = parseEdges(true)

		return solveShoeLace(cubes, edges)
	}

	/**
	 * Use the "shoelace" formula to find the space inside a shape.
	 */
	private fun solveShoeLace(cubes: Long, edges: List<Pair<Long, Long>>): Long {
		val results = edges.mapIndexed { index, edge ->
			val nextEdge = edges.getOrNull(index + 1) ?: edges[0] // Roll over to first

			edge.first * nextEdge.second - edge.second * nextEdge.first
		}

		val sum = abs(results.sum()) + cubes

		return sum / 2 + 1
	}

}
