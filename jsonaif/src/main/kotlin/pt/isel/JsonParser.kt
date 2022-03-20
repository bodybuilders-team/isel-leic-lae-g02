package pt.isel

import kotlin.reflect.KClass

/**
 * Representation of a JSON parser.
 */
interface JsonParser {

	/**
	 * Parses the JSON string with a Class representation.
	 * @param source JSON string
	 * @param klass represents a class
	 * @return [klass] instance with [source] data
	 */
	fun parse(source: String, klass: KClass<*>): Any?
}
