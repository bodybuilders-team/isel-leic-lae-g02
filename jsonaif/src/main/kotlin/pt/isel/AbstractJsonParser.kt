package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.full.createType


/**
 * Abstract representation of a JSON parser.
 */
abstract class AbstractJsonParser : JsonParser {

	override fun parse(source: String, klass: KClass<*>): Any? {
		return parse(JsonTokens(source), klass)
	}

	/**
	 * Parses the JSON tokens with a class representation.
	 * @param tokens JSON tokens
	 * @param klass represents a class
	 * @return [klass] instance with [tokens] data
	 */
	fun parse(tokens: JsonTokens, klass: KClass<*>) = when (tokens.current) {
		OBJECT_OPEN -> parseObject(tokens, klass)
		ARRAY_OPEN -> parseArray(tokens, klass)
		DOUBLE_QUOTES -> parseString(tokens)
		else -> parsePrimitive(tokens, klass)
	}

	/**
	 * Parses the JSON primitive tokens with a class representation.
	 * @param tokens JSON tokens
	 * @param klass represents a class
	 * @return [klass] instance with [tokens] data
	 */
	abstract fun parsePrimitive(tokens: JsonTokens, klass: KClass<*>): Any?

	/**
	 * Parses the JSON object tokens with a class representation.
	 * @param tokens JSON tokens
	 * @param klass represents a class
	 * @return [klass] instance with [tokens] data
	 */
	abstract fun parseObject(tokens: JsonTokens, klass: KClass<*>): Any?

	/**
	 * Parses the JSON string tokens.
	 * @param tokens JSON tokens
	 * @return string with [tokens] data
	 */
	private fun parseString(tokens: JsonTokens): String {
		tokens.pop(DOUBLE_QUOTES) // Discard double quotes "
		return tokens.popWordFinishedWith(DOUBLE_QUOTES)
	}

	/**
	 * Parses the JSON array tokens with a class representation.
	 *
	 * @param tokens JSON tokens
	 * @param klass represents a class
	 * @return string instance with [tokens] data
	 */
	private fun parseArray(tokens: JsonTokens, klass: KClass<*>): List<Any?> {
		val list = mutableListOf<Any?>()
		tokens.pop(ARRAY_OPEN) // Discard square brackets [ ARRAY_OPEN
		tokens.trim() // Added to allow for empty arrays
		while (tokens.current != ARRAY_END) {
			val v = parse(tokens, klass)
			list.add(v)

			if (tokens.current == COMMA) // The last element finishes with ] rather than a comma
				tokens.pop(COMMA) // Discard COMMA
			else break
			tokens.trim()
		}
		tokens.pop(ARRAY_END) // Discard square bracket ] ARRAY_END
		return list
	}

}
