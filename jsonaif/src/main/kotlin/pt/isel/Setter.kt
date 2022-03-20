package pt.isel

/**
 * Represents a class property setter.
 */
interface Setter {

	/**
	 * Sets the target property with the data from [tokens]
	 * @param target instance where the property will be set
	 * @param tokens JSON tokens
	 */
	fun apply(target: Any, tokens: JsonTokens)
}
