package pt.isel.json_parser

import pt.isel.*
import pt.isel.json_convert.JsonConvert
import pt.isel.json_convert.JsonConverter
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

	private const val NULL_STRING = "null"

	/**
	 * Parses the JSON primitive tokens with a class representation.
	 * @param tokens JSON tokens
	 * @param klass represents a class
	 * @return [klass] instance with [tokens] data
	 * @throws ParseException if something unexpected happens
	 */
	override fun parsePrimitive(tokens: JsonTokens, klass: KClass<*>): Any? {
		val unparsedValue = tokens.popWordPrimitive().trim()  // TODO: 20/03/2022 Check this "trim" in the future
		if (unparsedValue == NULL_STRING) return null

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
	 * Parses the JSON object tokens with a class representation.
	 * @param tokens JSON tokens
	 * @param klass represents a class
	 * @return [klass] instance with [tokens] data
	 */
	override fun parseObject(tokens: JsonTokens, klass: KClass<*>): Any? {
		tokens.pop(OBJECT_OPEN)
		tokens.trim() // Added to allow for empty object

		setters.computeIfAbsent(klass, JsonParserReflect::loadSetters)

		val constructor = klass.getOptionalConstructor()

		val parsedObject = if (constructor != null)
			parseObjectWithInstance(tokens, klass, constructor)
		else parseObjectWithCtor(tokens, klass)

		tokens.pop(OBJECT_END)
		return parsedObject
	}


	/**
	 * Parses an object using an instance returned by KClass.createInstance() method,
	 * calling a parameterless constructor or a constructor in which all parameters are optional.
	 *
	 * @param tokens JSON tokens
	 * @param klass represents a class
	 *
	 * @return parsed object
	 * @throws ParseException if something unexpected happens
	 */
	private fun parseObjectWithInstance(tokens: JsonTokens, klass: KClass<*>, constructor: KFunction<*>): Any {
		val instance = klass.createInstance()

		traverseJsonObject(tokens) { propName ->
			val setter = setters[klass]?.get(propName) ?: throw ParseException("Parameter $propName doesn't exist")
			setter.apply(target = instance, tokens)
		}

		return instance
	}


	/**
	 * Parses an object by collecting parameters and only in the end calling a constructor with the obtained parameters.
	 *
	 * @param tokens JSON tokens
	 * @param klass represents a class
	 *
	 * @return parsed object
	 * @throws ParseException if something unexpected happens
	 */
	private fun parseObjectWithCtor(tokens: JsonTokens, klass: KClass<*>): Any? {
		val constructorParams = mutableMapOf<KParameter, Any?>()

		traverseJsonObject(tokens) { propName ->
			val setter = setters[klass]?.get(propName) ?: throw ParseException("Parameter $propName doesn't exist")
			setter.apply(target = constructorParams, tokens)
		}

		return klass.primaryConstructor?.callBy(constructorParams)
	}


	/**
	 * Iterates through the JSON object and calls a [usePropCb] for each property.
	 *
	 * @param tokens JSON tokens
	 * @param usePropCb callback to be called with each property
	 */
	private fun traverseJsonObject(tokens: JsonTokens, usePropCb: (String) -> Unit) {
		while (tokens.current != OBJECT_END) {
			val propName = tokens.popWordFinishedWith(COLON).trim()
			usePropCb(propName)

			popCommaIfExists(tokens)
		}
	}


	/**
	 * Gets the property value calling the convert function in [convertAnnotation] converter.
	 * @param convertAnnotation annotation with converter
	 * @param tokens JSON tokens
	 * @return property value
	 */
	private fun getPropValueFromConverter(convertAnnotation: JsonConvert, tokens: JsonTokens): Any? =
		convertAnnotation.converter.apply {
			val convertInterface = supertypes.singleOrNull { it.classifier == JsonConverter::class }
				?: throw NotImplementedError(
					"Class passed as argument to JsonConvert annotation should implement JsonConverter interface"
				)

			// TODO: 21/03/2022 Check generic type (!!)
			val jsonType = convertInterface.arguments.first().type!!.classifier as KClass<*>
			val convertFunction = memberFunctions.first()

			return convertFunction.call(objectInstance, parse(tokens, jsonType))
		}

	/**
	 * Loads a map with parameter name as key and the parameter itself as a value
	 * for all parameters in the primary constructor of a given [KClass]
	 *
	 * @param klass representation of a class
	 * @return map for accessing a parameter by its name
	 */
	private fun loadSetters(klass: KClass<*>): Map<String, Setter> =
		klass.primaryConstructor!!.parameters.associate { kParam ->
			Pair(
				getJsonPropertyName(kParam),
				object : Setter {
					val kProp = (klass.memberProperties.firstOrNull { it.name == kParam.name }
						?: throw ParseException("Property ${kParam.name} doesn't exist"))

					override fun apply(target: Any, tokens: JsonTokens) {
						val converterAnnotation = kParam.findAnnotation<JsonConvert>()

						val propValue =
							if (converterAnnotation != null)
								getPropValueFromConverter(converterAnnotation, tokens)
							else
								parse(tokens, kParam.type)

						if (target is MutableMap<*, *>)
							(target as MutableMap<KParameter, Any?>)[kParam] = propValue
						else
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
				parse(tokens, type.arguments.first().type!!.classifier!! as KClass<*>)
			else
				parse(tokens, klass)

		if (propValue == null && !type.isMarkedNullable)
			throw ParseException("Value of property with type ${klass.qualifiedName} should not be null")

		return propValue
	}
}


/**
 * Checks if a KClass has constructors with no arguments.
 * @return true if the class has one or more constructors with no arguments
 */
fun <T : Any> KClass<T>.getOptionalConstructor(): KFunction<T>? {
	val propMap = declaredMemberProperties.associateBy { it.name }

	return constructors.firstOrNull { constructor ->
		constructor.valueParameters.all {
			it.isOptional && propMap.containsKey(it.name) && propMap[it.name] is KMutableProperty<*>
		}
	}
}

