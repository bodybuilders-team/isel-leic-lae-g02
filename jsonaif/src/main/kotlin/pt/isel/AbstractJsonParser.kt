package pt.isel

import kotlin.reflect.KClass


/**
 * Abstract implementation of a JSON parser.
 */
abstract class AbstractJsonParser : JsonParser {

	override fun parse(source: String, klass: KClass<*>): Any? = parse(JsonTokens(source), klass)


	/**
	 * Parses the JSON tokens with a class representation.
	 * @param tokens JSON tokens
	 * @param klass represents a class
	 * @return [klass] instance with [tokens] data
	 */
	fun parse(tokens: JsonTokens, klass: KClass<*>) = when (tokens.current) {
		OBJECT_OPEN 	-> parseObject(tokens, klass)
		ARRAY_OPEN 		-> parseArray(tokens, klass)
		DOUBLE_QUOTES 	-> parseString(tokens)
		else 			-> parsePrimitive(tokens, klass)
	}


	/**
	 * Parses the JSON primitive tokens with a class representation.
	 * @param tokens JSON tokens
	 * @param klass represents a class
	 * @return [klass] instance with [tokens] data
	 * @throws ParseException if something unexpected happens
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
		tokens.pop(DOUBLE_QUOTES)
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
		tokens.pop(ARRAY_OPEN)
		tokens.trim() // Added to allow for empty arrays

		while (tokens.current != ARRAY_END) {
			val value = parse(tokens, klass)
			list.add(value)

			popCommaIfExists(tokens)
			tokens.trim()
		}

		tokens.pop(ARRAY_END)
		return list
	}


	/**
	 * Checks if the current token is a COMMA.
	 * @param tokens JSON tokens
	 */
	fun popCommaIfExists(tokens: JsonTokens) {
		if (tokens.current == COMMA)
			tokens.pop(COMMA)
	}
}
