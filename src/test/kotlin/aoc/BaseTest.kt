package aoc

import org.junit.jupiter.api.fail

open class BaseTest {

	fun readInput(name: String): List<String> {
		val resource = javaClass.getResource("/$name") ?: fail("File not found: $name")

		return resource.readText().lines()
	}

}
