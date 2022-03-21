package pt.isel

import kotlin.reflect.*

/**
 * Represents constructor parameters
 */
interface Params {

    fun add(params: MutableMap<KParameter, Any?>, tokens: JsonTokens)
}