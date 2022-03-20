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
		TODO("Not yet implemented")
	}

	override fun parseObject(tokens: JsonTokens, klass: KClass<*>): Any? {
		tokens.pop(OBJECT_OPEN)
		val props = klass.memberProperties.associateBy { kProp -> kProp.name }
		val instance = klass.createInstance()

		while (tokens.current != OBJECT_END) {
			val propName = tokens.popWordFinishedWith(COLON)
			val kProp = (
					props[propName] ?: throw NoSuchPropertyException(Exception("Property $propName doesn't exist"))
					) as KMutableProperty<*>

			val propValue = parse(tokens, kProp.javaClass.kotlin)

			kProp.setter.call(instance, propValue)

			if (tokens.current == COMMA)
				tokens.pop(COMMA)
		}

		tokens.pop(OBJECT_END)
		return instance
	}
}
