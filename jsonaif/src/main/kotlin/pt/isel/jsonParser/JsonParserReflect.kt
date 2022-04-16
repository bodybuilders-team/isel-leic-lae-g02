package pt.isel.jsonParser

import pt.isel.COLON
import pt.isel.JsonTokens
import pt.isel.OBJECT_END
import pt.isel.OBJECT_OPEN
import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonParser.JsonParserReflect.setters
import pt.isel.jsonParser.setter.Setter
import pt.isel.jsonParser.setter.param.AnnotatedParamSetter
import pt.isel.jsonParser.setter.param.ParamSetter
import pt.isel.jsonParser.setter.property.AnnotatedPropertySetter
import pt.isel.jsonParser.setter.property.PropertySetter
import pt.isel.jsonProperty.getJsonPropertyName
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * JSON parser using reflection.
 * @property setters for each domain class we keep a Map<String, Setter> relating properties names with their setters
 */
object JsonParserReflect : AbstractJsonParser() {

    private val setters = mutableMapOf<KClass<*>, Map<String?, Setter>>()
    private val parseableKClasses = mutableSetOf<KClass<*>>()

    private const val NULL_STRING = "null"

    /**
     * Parses the JSON primitive tokens with a class representation.
     * @param tokens JSON tokens
     * @param klass represents a class
     * @return [klass] instance with [tokens] data
     * @throws ParseException if something unexpected happens
     */
    override fun parsePrimitive(tokens: JsonTokens, klass: KClass<*>): Any? {
        val unparsedValue = tokens.popWordPrimitive().trim()
        if (unparsedValue == NULL_STRING) return null

        val parseException = ParseException.unexpectedClass(unparsedValue, klass.qualifiedName)
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
    override fun parseObject(tokens: JsonTokens, klass: KClass<*>): Any {
        tokens.pop(OBJECT_OPEN)
        tokens.trim() // Added to allow for empty object

        if (!isParseable(klass))
            throw ParseException(
                "Class ${klass.qualifiedName} is not valid to parse: " +
                    "all parameters of the primary constructor must be properties"
            )

        val hasNoArgsCtor = klass.hasOptionalPrimaryConstructor()
        setters.computeIfAbsent(klass) { loadSetters(klass, hasNoArgsCtor) }

        val parsedObject =
            if (hasNoArgsCtor)
                parseObjectWithInstance(tokens, klass)
            else
                parseObjectWithCtor(tokens, klass)

        tokens.pop(OBJECT_END)
        return parsedObject
    }

    /**
     * Checks if a KClass is parseable: all parameters of the primary constructor must be properties.
     *
     * @param klass represents a class
     * @return true if the [klass] is valid to parse
     */
    private fun isParseable(klass: KClass<*>): Boolean {
        if (klass in parseableKClasses)
            return true

        val properties = klass.memberProperties.associateBy { kProp -> kProp.name }

        return klass.primaryConstructor?.parameters
            ?.all { kParam -> kParam.name in properties }
            .also { parseableKClasses.add(klass) }
            ?: false
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
    private fun parseObjectWithInstance(tokens: JsonTokens, klass: KClass<*>): Any {
        val instance = klass.createInstance()

        traverseJsonObject(tokens, klass, instance)

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
    private fun parseObjectWithCtor(tokens: JsonTokens, klass: KClass<*>): Any {
        val constructorParams = mutableMapOf<KParameter, Any?>()

        traverseJsonObject(tokens, klass, constructorParams)

        return klass.primaryConstructor?.callBy(constructorParams)
            ?: throw ParseException("Klass ${klass.primaryConstructor} not have a primary constructor")
    }

    /**
     * Iterates through the JSON object, creates a setter for each property, and applies it to a specific target.
     *
     * @param tokens JSON tokens
     * @param klass represents a class
     * @param target target to apply setter
     */
    private fun traverseJsonObject(tokens: JsonTokens, klass: KClass<*>, target: Any) {
        while (tokens.current != OBJECT_END) {
            val propName = tokens.popWordFinishedWith(COLON).trim()
            val setter = setters[klass]?.get(propName)
                ?: throw ParseException("Property $propName doesn't exist")

            setter.apply(target, tokens)

            popCommaIfExists(tokens)
        }
    }

    /**
     * Loads a map with parameter name as key and the parameter itself as a value
     * for all parameters in the primary constructor of a given [KClass]
     *
     * @param klass representation of a class
     * @param hasNoArgsCtor true if the [klass]' primary constructor are all mutable properties with default values
     *
     * @return map for accessing a parameter by its name
     */
    private fun loadSetters(klass: KClass<*>, hasNoArgsCtor: Boolean): Map<String?, Setter> =
        klass.primaryConstructor?.parameters?.associate { kParam ->
            Pair(
                getJsonPropertyName(kParam),
                getSetter(klass, kParam, hasNoArgsCtor)
            )
        } ?: throw ParseException("Klass ${klass.primaryConstructor} not have a primary constructor")

    /**
     * Gets the [kParam] setter.
     *
     * @param klass representation of a class
     * @param kParam param to get setter
     * @param hasNoArgsCtor true if the [klass]' primary constructor are all mutable properties with default values
     *
     * @return [kParam] setter
     */
    private fun getSetter(klass: KClass<*>, kParam: KParameter, hasNoArgsCtor: Boolean): Setter {
        val isAnnotated = kParam.hasAnnotation<JsonConvert>()

        return when {
            isAnnotated && hasNoArgsCtor -> AnnotatedPropertySetter(klass, kParam)
            isAnnotated && !hasNoArgsCtor -> AnnotatedParamSetter(kParam)
            !isAnnotated && hasNoArgsCtor -> PropertySetter(klass, kParam)
            else -> ParamSetter(kParam)
        }
    }
}
