package pt.isel.json_parser

import kotlin.reflect.KClass

/**
 * Representation of a JSON parser.
 *
 * @see <a href="https://www.json.org">JSON specs</a>
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
