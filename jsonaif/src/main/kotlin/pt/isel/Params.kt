package pt.isel

import kotlin.reflect.*

/**
 * Represents constructor parameters
 */
interface Params {

	/**
	 * Adds parameter represented as [tokens] to [params] map.
	 * @param params map of params
	 * @param tokens JSON tokens
	 */
	fun add(params: MutableMap<KParameter, Any?>, tokens: JsonTokens)
}