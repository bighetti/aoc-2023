package aoc

import aoc.data.Brick22

/**
 * This puzzle is another coordinate intersection problem, with three dimensions.
 *
 * All bricks are size 1x1x1 or greater, so they represent cubes rather than points on a grid.
 *
 * The bricks can continue to fall (decrease Z by 1) as long as there is no brick occupying part of that space already.
 */
class Day22(private val input: List<String>) {

	/**
	 * For each brick find the bricks directly below it.
	 *
	 * If there is only one brick directly below it cannot be disintegrated (would cause others to fall).
	 */
	private fun getCannotDisintegrate(bricks: List<Brick22>): Set<Int> {
		val unsafe = mutableSetOf<Int>()

		bricks.forEach { brick ->
			val directlyBelow = getDirectlyBelow(bricks, brick)

			if (directlyBelow.size == 1) {
				unsafe.add(directlyBelow.first().id) // Add the brick below to the results.
			}
		}

		return unsafe
	}

	/**
	 * Find bricks directly above a brick that was just removed.
	 * Check if those brick still have any other bricks below.
	 */
	private fun getDirectlyAboveWithNoneBelow(bricks: List<Brick22>, removedBrick: Brick22): List<Brick22> {
		val maxZ = maxOf(removedBrick.z, removedBrick.zEnd)

		val above = bricks.filter { minOf(it.z, it.zEnd) == maxZ + 1 } // Has Z + 1
		val directlyAbove = above.filter { it.isMatchingXY(removedBrick) } // Has matching X & Y

		// Has no other bricks below
		val directlyAboveWithNoneBelow = directlyAbove.filter {
			getDirectlyBelow(bricks, it).isEmpty()
		}

		return directlyAboveWithNoneBelow.distinct() // Avoid possible duplicates
	}

	/**
	 * Find all bricks that are directly below this brick.
	 * This means it has Z-1 compared to the current brick, and intersecting X and Y.
	 */
	private fun getDirectlyBelow(bricks: List<Brick22>, brick: Brick22): Set<Brick22> {
		val minZ = minOf(brick.z, brick.zEnd)

		val below = bricks.filter { maxOf(it.z, it.zEnd) == minZ - 1 } // Has Z - 1
		val directlyBelow = below.filter { it.isMatchingXY(brick) } // Has matching X & Y

		return directlyBelow.toSet()
	}

	/**
	 * Decrease Z by 1 for all bricks that can move.
	 * A brick can move if it is not at Z=1 and does not have a brick directly below it.
	 *
	 * Start with the bricks with lowest Z value to "free up" space for those above.
	 *
	 * Return the bricks that can still move again.
	 */
	private fun move(bricks: List<Brick22>): Boolean {
		val sorted = bricks.filter {
			!it.cannotMove
		}.sortedBy {
			minOf(it.z, it.zEnd)
		}

		sorted.forEach { brick ->
			val minZ = minOf(brick.z, brick.zEnd)

			val below = bricks.filter { maxOf(it.z, it.zEnd) == minZ - 1 } // Has Z - 1
			val directlyBelow = below.find { it.isMatchingXY(brick) } // Has matching X & Y

			if (directlyBelow == null) {
				brick.z--
				brick.zEnd--

				if (brick.z == 1 || brick.zEnd == 1) {
					brick.cannotMove = true // Reached the bottom
				}
			} else {
				brick.cannotMove = true
			}
		}

		return bricks.find { !it.cannotMove } != null
	}

	/**
	 * Create the bricks with original coordinates.
	 * Continue to move all bricks down until none can be moved.
	 */
	private fun parseAndDrop(): List<Brick22> {
		val results =  input.mapIndexed { index, line ->
			val start = line.substringBefore("~").split(",").map { it.toInt() }
			val end = line.substringAfter("~").split(",").map { it.toInt() }

			val brick = Brick22(index, start[0], start[1], start[2], end[0], end[1], end[2])

			if (start[2] == 1 || end[2] == 1) {
				brick.cannotMove = true // At Z=1 already
			}

			brick
		}

		var movesLeft = move(results)

		while (movesLeft) {
			movesLeft = move(results)
		}

		return results
	}

	fun part1(): Int {
		val bricks = parseAndDrop()

		var movesLeft = move(bricks)

		while (movesLeft) {
			movesLeft = move(bricks)
		}

		val cannotDisintegrate = getCannotDisintegrate(bricks)

		return bricks.size - cannotDisintegrate.size
	}

	fun part2(): Int {
		val bricks = parseAndDrop()

		val cannotDisintegrate = getCannotDisintegrate(bricks)

		val results = cannotDisintegrate.map { brickId ->
			removeOne(bricks, brickId)
		}

		return results.sumOf { it }
	}

	/**
	 * Remove a brick that cannot be disintegrated (would cause others to fall).
	 *
	 * Find the bricks immediately above that now have no bricks below them.
	 * Those bricks are removed next, and the test repeated until no more bricks found.
	 */
	private fun removeOne(bricks: List<Brick22>, id: Int): Int {
		val bricksCopy = bricks.toMutableList() // A copy of the original list for each evaluation
		val brick = bricksCopy.find { it.id == id } ?: throw Exception("Not found: $id")

		var toRemove = setOf(brick)

		while (toRemove.isNotEmpty()) {
			val nextToRemove = mutableSetOf<Brick22>()

			toRemove.forEach { removeBrick ->
				bricksCopy.remove(removeBrick) // Remove brick from the list

				val bricksAbove = getDirectlyAboveWithNoneBelow(bricksCopy, removeBrick) // Find next bricks to remove

				nextToRemove.addAll(bricksAbove)
			}

			toRemove = nextToRemove
		}

		return bricks.size - (bricksCopy.size + 1) // the first brick removed does not count in the result
	}

}
