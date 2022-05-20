package pt.isel.jsonParser

import kotlin.reflect.KClass

/**
 * Representation of a JSON parser.
 *
 * @see <a href="https://www.json.org">JSON specs</a>
 */
interface JsonParser {

    /**
     * Parses the JSON string with a Class representation.
     *
     * @param source JSON string
     * @param klass represents a class
     *
     * @return [klass] instance with [source] data
     */
    fun parse(source: String, klass: KClass<*>): Any?
}

/**
 * Parses the JSON string.
 *
 * @param source JSON string
 * @param T represents a class
 *
 * @return [T] instance with [source] data
 */
inline fun <reified T> JsonParser.parse(source: String): T? = parse(source, T::class) as T?

/**
 * Parses the JSON string into an array of [T] instances.
 *
 * @param source JSON string
 * @param T represents a class
 *
 * @return array of [T] instances with [source] data
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> JsonParser.parseArray(source: String): List<T> = parse(source, T::class) as List<T>
