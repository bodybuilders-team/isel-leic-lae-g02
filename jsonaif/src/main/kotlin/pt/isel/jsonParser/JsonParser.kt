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

    /**
     * Parses the JSON string into a sequence with the given Class representation.
     *
     * @param json JSON string
     * @param klass represents a class
     *
     * @return [Sequence] sequence
     * @throws [ParseException] if the JSON is not a valid array
     */
    fun parseSequence(json: String, klass: KClass<*>): Sequence<Any?>
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
 * Parses the JSON string, ensuring that the result is not null.
 *
 * @param source JSON string
 * @param T represents a class
 *
 * @return [T] instance with [source] data
 */
inline fun <reified T> JsonParser.parseNotNull(source: String): T =
    parse(source, T::class) as T? ?: throw ParseException("Value shouldn't be null")

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

/**
 * Parses the JSON string into a sequence of [T].
 * The passed JSON needs to be a valid JSON array.
 *
 * @param source JSON string
 *
 * @return [T] sequence
 * @throws [ParseException] if the JSON is not a valid array
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> JsonParser.parseSequence(source: String): Sequence<T?> =
    parseSequence(source, T::class) as Sequence<T?>

/**
 * Parses the JSON string into a sequence of [T].
 * The passed JSON needs to be a valid JSON array.
 *
 * @param source JSON string
 *
 * @return [T] sequence
 * @throws [ParseException] if the JSON is not a valid array
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> JsonParser.parseSequenceNotNull(source: String): Sequence<T> =
    parseSequence(source, T::class) as Sequence<T>
