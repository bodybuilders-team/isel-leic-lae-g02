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
    fun <T : Any> parse(source: String, klass: KClass<T>): T?

    /**
     * Parses the JSON string into a sequence with the given Class representation.
     *
     * @param json JSON string
     * @param klass represents a class
     *
     * @return [Sequence] sequence
     * @throws [ParseException] if the JSON is not a valid array
     */
    fun <T : Any> parseSequence(json: String, klass: KClass<T>): Sequence<T?>

    /**
     * Parses the JSON string into an array of [T] instances.
     *
     * @param source JSON string
     * @param T represents a class
     *
     * @return array of [T] instances with [source] data
     */
    fun <T : Any> parseArray(source: String, klass: KClass<T>): List<T?>?
}

/**
 * Parses the JSON string.
 *
 * @param source JSON string
 * @param T represents a class
 *
 * @return [T] instance with [source] data
 */
inline fun <reified T : Any> JsonParser.parse(source: String): T? = parse(source, T::class)

/**
 * Parses the JSON string, ensuring that the result is not null.
 *
 * @param source JSON string
 * @param T represents a class
 *
 * @return [T] instance with [source] data
 */
inline fun <reified T : Any> JsonParser.parseNotNull(source: String): T =
    parse(source, T::class) ?: throw ParseException("Value shouldn't be null")

/**
 * Parses the JSON string into an array of [T] instances.
 *
 * @param source JSON string
 * @param T represents a class
 *
 * @return array of [T] instances with [source] data
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> JsonParser.parseArray(source: String): List<T?>? = parseArray(source, T::class)

/**
 * Parses the JSON string into an array of [T] instances, while ensuring there is no nullability.
 *
 * @param source JSON string
 * @param T represents a class
 *
 * @return array of [T] instances with [source] data
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> JsonParser.parseArrayNotNull(source: String): List<T> =
    parseArray(source, T::class) as List<T>

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
inline fun <reified T : Any> JsonParser.parseSequence(source: String): Sequence<T?> =
    parseSequence(source, T::class)

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
inline fun <reified T : Any> JsonParser.parseSequenceNotNull(source: String): Sequence<T> =
    parseSequence(source, T::class) as Sequence<T>
