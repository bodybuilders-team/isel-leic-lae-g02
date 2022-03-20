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
		val unparsedValue = tokens.popWordPrimitive().trim()  //TODO: Check this "trim" in the future
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

	override fun parseObject(tokens: JsonTokens, klass: KClass<*>): Any? {
		tokens.pop(OBJECT_OPEN)
		val props = klass.memberProperties.associateBy { kProp -> kProp.name }
		val instance = klass.createInstance()

		while (tokens.current != OBJECT_END) {
			val propName = tokens.popWordFinishedWith(COLON).trim()
			val kProp = (
					props[propName] ?: throw NoSuchPropertyException(Exception("Property $propName doesn't exist"))
					) as KMutableProperty<*>

			val propValue = parse(tokens, kProp.returnType.classifier as KClass<*>)

			if (propValue == null && !kProp.returnType.isMarkedNullable)
				throw ParseException("Value of property $propName should not be null")

			kProp.setter.call(instance, propValue)

			if (tokens.current == COMMA)
				tokens.pop(COMMA)
		}

		tokens.pop(OBJECT_END)
		return instance
	}
}


class ParseException(message: String) : Exception(message) {
	companion object {
		fun unexpectedClass(unparsedValue: String, className: String) =
			ParseException("Value $unparsedValue is not of expected class $className")
	}
}
