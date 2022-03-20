package pt.isel

import kotlin.reflect.*
import kotlin.reflect.full.*

/**
 * JSON parser using reflection.
 */
object JsonParserReflect : AbstractJsonParser() {

	/**
	 * For each domain class we keep a Map<String, Setter> relating properties names with their setters.
	 * This is for Part 2 of Jsonaif workout.
	 */
	private val setters = mutableMapOf<KClass<*>, Map<String, Setter>>()


	override fun parsePrimitive(tokens: JsonTokens, klass: KClass<*>): Any? {
		val unparsedValue = tokens.popWordPrimitive().trim()  // TODO: 20/03/2022 Check this "trim" in the future
		if (unparsedValue == "null") return null

		val parseException = ParseException.unexpectedClass(unparsedValue, klass.qualifiedName!!)
		val parser = basicParser[klass] ?: throw parseException

		try {
			return parser(unparsedValue)
		} catch (err: NumberFormatException) {
			throw parseException
		} catch (err: IllegalArgumentException) {
			throw parseException
		}
	}


	override fun parseObject(tokens: JsonTokens, klass: KClass<*>): Any? { // TODO: 20/03/2022 Ask why can be null
		tokens.pop(OBJECT_OPEN)
		val instance = klass.createInstance()

		if (setters[klass] == null)
			setters[klass] = getSetters(klass)

		while (tokens.current != OBJECT_END) {
			val propName = tokens.popWordFinishedWith(COLON).trim()

			val setter = setters[klass]!![propName] ?: throw ParseException("Property $propName doesn't exist")
			setter.apply(target = instance, tokens)

			if (tokens.current == COMMA)
				tokens.pop(COMMA)
		}

		tokens.pop(OBJECT_END)
		return instance
	}


	/**
	 * Gets the [klass] properties setters.
	 * @param klass representation of a class
	 * @return map with pairs PropertyName : PropertySetter
	 */
	private fun getSetters(klass: KClass<*>): Map<String, Setter> =
		klass.memberProperties.associate { kProp ->
			Pair(
				kProp.name,
				object : Setter {
					override fun apply(target: Any, tokens: JsonTokens) {
						val propValue = parse(tokens, kProp.returnType.classifier as KClass<*>)

						if (propValue == null && !kProp.returnType.isMarkedNullable)
							throw ParseException("Value of property ${kProp.name} should not be null")

						(kProp as KMutableProperty<*>).setter.call(target, propValue)
					}
				}
			)
		}
}
