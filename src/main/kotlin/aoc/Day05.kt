package aoc

/**
 * This puzzle has to find a way to avoid simulating every possible outcome, since it would take forever.
 * The number ranges are passed through each step instead.
 *
 * The input begins with the seed numbers:
 *
 *   Part 1 - single seeds
 *   Part 2 - pair of seeds as a range (start + size)
 *
 * The header is found which contains:
 *
 *   1) The next type, e.g. seed-to-soil
 *   2) The mapping instructions to convert matched source ranges to destination ranges
 *
 * Each input range is evaluated against all mappings to produce one or more output ranges.
 *
 * For example:
 *
 *   input = 10-100
 *   mapping #1 source = 10-20 destination = 110-120
 *   mapping #2 source = 21-30 destination = 221-230
 *   output #1 = 110-120
 *   output #2 = 221-230
 *   output #3 = 31-100 (did not match a mapping rule)
 *
 * Continue with the next pair of types until the end.
 */
class Day05(private val input: List<String>) {

	/**
	 * Get the intersection sub-range from two ranges, or null if no overlap.
	 */
	private fun getIntersect(inputRange: LongRange, mappingSourceRange: LongRange): LongRange? {
		val first = maxOf(inputRange.first, mappingSourceRange.first)
		val last = minOf(inputRange.last, mappingSourceRange.last)

		if (last < first) {
			return null
		}

		return LongRange(first, last)
	}

	/**
	 * Convert the instructions from the data file into two ranges, the input and the output.
	 *
	 * The data format for each line is <output first number> <input first number> <range size>.
	 */
	private fun getMappings(header: String): List<Pair<LongRange, LongRange>> {
		var adding = false
		val results = mutableListOf<Pair<LongRange, LongRange>>()

		input.forEach { line ->
			if (line.isEmpty()) {
				adding = false
			} else if (adding) {
				val destinationStart = line.split(" ")[0].toLong()
				val sourceStart = line.split(" ")[1].toLong()
				val range = line.split(" ")[2].toLong()

				results.add(
					Pair(
						LongRange(sourceStart, sourceStart + range - 1),
						LongRange(destinationStart, destinationStart + range - 1)
					)
				)
			} else {
				adding = line.startsWith(header)
			}
		}

		return results
	}

	/**
	 * Find the header that starts with the input type, e.g. seed-to-soil.
	 * Extract the output type from the string, e.g. soil
	 */
	private fun getNextName(name: String): String {
		val header = input.find { it.startsWith("$name-") } ?: "" // Empty means the last type is reached

		return header.substringAfter("-to-").substringBefore(" ")
	}

	/**
	 * A loop method to convert each input range into one or more output ranges.
	 *
	 * See getMappings() and getDestinationRanges() for more information.
	 */
	private fun getNextRanges(inputRanges: Set<LongRange>, name: String, nextName: String): Set<LongRange> {
		val mappings = getMappings("$name-to-$nextName")
		val results = mutableSetOf<LongRange>()

		inputRanges.forEach { oldRange ->
			val outputRanges = getOutputRanges(mappings, oldRange)

			results.addAll(outputRanges)
		}

		return results
	}

	/**
	 * The mappings contain all input to output conversions for a pair of types, e.g. seed-to-soil
	 *
	 * The old range is a single continuous range from the previous conversion results.
	 *
	 * For each mapping:
	 *   For each input range:
	 *     1) Find a sub-range that matches the input, i.e. the intersection
	 *     2) Convert the input sub-range into an output sub-range
	 *     2a) Add the output sub-range to the results
	 *     2b) Keep any unmatched input range(s) to check with the next mapping
	 *
	 * Note: Step 2b could produce two results, a sub-range before and after the intersection.
	 *
	 * After all mappings are checked, add any unmatched input ranges to the results also.
	 * There is an instruction that says all unmatched input should remain as-is, rather than discarded.
	 */
	private fun getOutputRanges(mappings: List<Pair<LongRange, LongRange>>, inputRange: LongRange): List<LongRange> {
		var inputRanges = listOf(inputRange)
		val results = mutableListOf<LongRange>()

		mappings.forEach { mapping ->
			val nextInputRanges = mutableListOf<LongRange>()

			val mappingDestinationRange = mapping.second
			val mappingSourceRange = mapping.first

			inputRanges.forEach { inputRange ->
				val intersect = getIntersect(inputRange, mappingSourceRange)

				if (intersect != null) {
					// Convert the intersection from source to destination range
					val startDiff = intersect.first - mappingSourceRange.first
					val endDiff = mappingSourceRange.last - intersect.last

					results.add(
						LongRange(
							mappingDestinationRange.first + startDiff,
							mappingDestinationRange.last - endDiff
						)
					)

					// Update the remaining (unused) ranges
					// Could be before and/or after the used range
					val remainingRanges = getRemainingRanges(inputRange, intersect)

					nextInputRanges.addAll(remainingRanges)
				} else {
					nextInputRanges.add(inputRange)
				}
			}

			inputRanges = nextInputRanges
		}

		// Add any unused range to the final result
		results.addAll(inputRanges)

		return results
	}

	/**
	 * Create 0, 1, or 2 ranges as remainder from the input that was not part of the intersection.
	 *
	 * If the input range was smaller than the mapping range, there will be no remainder.
	 * For example:
	 *
	 *   input = 10-20
	 *   mapping input = 5-25
	 *   intersection = 10-20
	 *   remainder = <none>
	 *
	 * If the input range was smaller than the mapping range, there will be some remainder before and/or after the intersection.
	 * For example:
	 *
	 *   input = 10-20
	 *   mapping = 14-16
	 *   intersection = 14-16
	 *   remainder = 10-13 and 17-20
	 */
	private fun getRemainingRanges(range: LongRange, intersect: LongRange): List<LongRange> {
		val results = mutableListOf<LongRange>()

		if (range.first < intersect.first) {
			results.add(
				LongRange(range.first, intersect.first - 1)
			)
		}

		if (range.last > intersect.last) {
			results.add(
				LongRange(intersect.last + 1, range.last)
			)
		}

		return results
	}

	/**
	 * Process the seed input data for part 1 (single seed) or part 2 (seed start number and range size).
	 */
	private fun getSeedRanges(part: Int): Set<LongRange> {
		val numbers = input.first().substringAfter(": ").split(" ").map { it.toLong() }

		return if (part == 1) {
			numbers.map { LongRange(it, it) }.toSet()
		} else {
			// Second value is the range (size) not the end number
			numbers.chunked(2).map { LongRange(it[0], it[0] + it[1] - 1) }.toSet()
		}
	}

	fun part1(): Any {
		return solve(1)
	}

	fun part2(): Any {
		return solve(2)
	}

	private fun solve(part: Int): Long {
		var name = "seed"
		var nextName = getNextName(name)
		var ranges = getSeedRanges(part)

		// Keep converting input ranges to output ranges until the last type (location) is reached.
		while (nextName.isNotEmpty()) {
			ranges = getNextRanges(ranges, name, nextName)

			name = nextName
			nextName = getNextName(name)
		}

		// Find the lowest number from any of the ranges
		return ranges.map { it.first }.minOf { it }
	}

}
