package pt.isel.jsonParser.parsers.reflect.setters

import pt.isel.JsonTokens

/**
 * Represents a setter.
 */
interface Setter {

    /**
     * Sets the target with the data from [tokens]
     *
     * @param target where tokens value will be set
     * @param tokens JSON tokens
     */
    fun apply(target: Any, tokens: JsonTokens)
}
