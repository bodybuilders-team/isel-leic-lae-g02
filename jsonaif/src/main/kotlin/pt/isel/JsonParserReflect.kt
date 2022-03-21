package pt.isel

import kotlin.reflect.*
import kotlin.reflect.full.*


// TODO: 21/03/2022 REMOVE DOUBLE BANG OPERATORS !!
/**
 * JSON parser using reflection.
 * @property setters for each domain class we keep a Map<String, Setter> relating properties names with their setters
 * @property params for each domain class we keep a Map<String, Params> relating klass constructor params names with their params
 */
object JsonParserReflect : AbstractJsonParser() {

	private val setters = mutableMapOf<KClass<*>, Map<String, Setter>>()
	private val params = mutableMapOf<KClass<*>, Map<String, Params>>()

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


	/**
	 * Parse an object
	 *
	 * @param tokens JSON tokens
	 * @param klass  class of the object to parse
	 *
	 * @return parsed object
	 */
	override fun parseObject(tokens: JsonTokens, klass: KClass<*>): Any? {
		tokens.pop(OBJECT_OPEN)

		val hasNoArgsCtor = klass.hasNoArgsConstructor()
		val instance = if (hasNoArgsCtor) klass.createInstance() else null
		val constructorParams = if (!hasNoArgsCtor) mutableMapOf<KParameter, Any?>() else null

		setters.computeIfAbsent(klass, ::loadSetters)
		params.computeIfAbsent(klass, ::loadParams)

		while (tokens.current != OBJECT_END) {
			val propName = tokens.popWordFinishedWith(COLON).trim()

			//TODO: Maybe add functions for each if scope
			if (hasNoArgsCtor) {
				val setter = setters[klass]!![propName] ?: throw ParseException("Parameter $propName doesn't exist")
				setter.apply(target = instance!!, tokens)
			} else {
				val params = params[klass]!![propName] ?: throw ParseException("Parameter $propName doesn't exist")
				params.add(params = constructorParams!!, tokens)
			}
			if (tokens.current == COMMA)
				tokens.pop(COMMA)
		}

		tokens.pop(OBJECT_END)

		return if (!hasNoArgsCtor)
			klass.primaryConstructor?.callBy(constructorParams!!)
		else
			instance
	}

	/**
	 * Loads a map with parameter name as key and the parameter itself as a value
	 * for all parameters in the primary constructor of a given [KClass]
	 *
	 * @param klass representation of a class
	 * @return map for accessing a parameter by its name
	 */
	private fun loadParams(klass: KClass<*>): Map<String, Params> =
		klass.primaryConstructor!!.parameters.associate { kParam ->
			Pair(
				getJsonPropertyAnnotationName(kParam) ?: kParam.name!!,
				object : Params {
					override fun add(params: MutableMap<KParameter, Any?>, tokens: JsonTokens) {
						val propValue = parse(tokens, kParam.type)
						params[kParam] = propValue
					}
				}
			)
		}


	/**
	 * Gets the [klass] properties setters.
	 * @param klass representation of a class
	 * @return map with pairs PropertyName : PropertySetter
	 */
	private fun loadSetters(klass: KClass<*>): Map<String, Setter> =
		klass.memberProperties.associate { kProp ->
			Pair(
				getJsonPropertyAnnotationName(kProp) ?: kProp.name,
				object : Setter {
					override fun apply(target: Any, tokens: JsonTokens) {
						val propValue = parse(tokens, kProp.returnType)
						(kProp as KMutableProperty<*>).setter.call(target, propValue)
					}
				}
			)
		}

	/**
	 * Parses [tokens] and returns the value of the parsed object with type [type]
	 *
	 * @param tokens tokens to parse
	 * @param type type of the object to parse
	 * @return parsed object
	 *
	 * @throws ParseException if the object can't be parsed
	 */
	private fun parse(tokens: JsonTokens, type: KType): Any? {
		val klass = type.classifier as KClass<*>
		val propValue =
			if (type.classifier == List::class)
				parse(tokens, type.arguments[0].type!!.classifier!! as KClass<*>)
			else
				parse(tokens, type.classifier as KClass<*>)

		if (propValue == null && !type.isMarkedNullable)
			throw ParseException("Value of property with type ${klass.qualifiedName} should not be null")

		return propValue
	}
}

/**
 * Retrieves [JsonProperty.value] or [kProp] name if the annotation is not present
 * @param kProp Property to get the name from
 */
private fun getJsonPropertyAnnotationName(kProp: KProperty<*>): String {
	val annotation = kProp.findAnnotation<JsonProperty>()

	return annotation?.value ?: kProp.name
}

/**
 * Retrieves [JsonProperty.value] or [kParam] name if the annotation is not present
 * @param kParam Parameter to get the name from
 */
private fun getJsonPropertyAnnotationName(kParam: KParameter): String {
	val annotation = kParam.findAnnotation<JsonProperty>()

	return annotation?.value ?: kParam.name!!
}

/**
 * Checks if a KClass has no arguments constructors.
 * @return true if the class has one or more constructors with no arguments
 */
fun <T : Any> KClass<T>.hasNoArgsConstructor(): Boolean =
	constructors.any { constructor ->
		constructor.parameters.all(KParameter::isOptional)
	}

