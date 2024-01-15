package aoc.data

class Brick22(val id: Int, val x: Int, val y: Int, var z: Int, val xEnd: Int, val yEnd: Int, var zEnd: Int) {

	var cannotMove = false

	fun isMatchingXY(other: Brick22): Boolean {
		val xRange = x..xEnd
		val yRange = y..yEnd

		val xOtherRange = other.x..other.xEnd
		val yOtherRange = other.y..other.yEnd

		val xIntersect = xRange.intersect(xOtherRange)
		val yIntersect = yRange.intersect(yOtherRange)

		return xIntersect.isNotEmpty() && yIntersect.isNotEmpty()
	}

	override fun toString(): String {
		return "id=$id x=$x y=$y z=$z xEnd=$xEnd yEnd=$yEnd zEnd=$zEnd cannotMove=$cannotMove"
	}

}
