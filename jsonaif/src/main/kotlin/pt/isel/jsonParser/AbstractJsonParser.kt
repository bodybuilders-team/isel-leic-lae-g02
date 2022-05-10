package pt.isel.jsonParser

import pt.isel.ARRAY_END
import pt.isel.ARRAY_OPEN
import pt.isel.COLON
import pt.isel.DOUBLE_QUOTES
import pt.isel.JsonTokens
import pt.isel.OBJECT_END
import pt.isel.OBJECT_OPEN
import pt.isel.jsonParser.parsers.reflect.setters.Setter
import pt.isel.jsonProperty.getJsonPropertyName
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * Abstract implementation of a JSON parser.
 */
abstract class AbstractJsonParser : JsonParser {

    protected val setters = mutableMapOf<KClass<*>, Map<String?, Setter>>()
    private val parseableKClasses = mutableSetOf<KClass<*>>()

    companion object {
        private const val NULL_STRING = "null"
    }

    override fun parse(source: String, klass: KClass<*>): Any? = parse(JsonTokens(source), klass)

    /**
     * Parses the JSON tokens with a class representation.
     * @param tokens JSON tokens
     * @param klass represents a class
     * @return [klass] instance with [tokens] data
     */
    fun parse(tokens: JsonTokens, klass: KClass<*>): Any? {
        tokens.trim()
        return when (tokens.current) {
            OBJECT_OPEN -> parseObject(tokens, klass)
            ARRAY_OPEN -> parseArray(tokens, klass)
            DOUBLE_QUOTES -> parseString(tokens)
            else -> parsePrimitive(tokens, klass)
        }
    }

    /**
     * Parses the JSON primitive tokens with a class representation.
     * @param tokens JSON tokens
     * @param klass represents a class
     * @return [klass] instance with [tokens] data
     * @throws ParseException if something unexpected happens
     */
    private fun parsePrimitive(tokens: JsonTokens, klass: KClass<*>): Any? {
        val unparsedValue = tokens.popWordPrimitive().trim()
        if (unparsedValue == NULL_STRING) return null

        val parseException = ParseException("Value $unparsedValue is not of expected class ${klass.qualifiedName}")
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
    protected abstract fun parseObject(tokens: JsonTokens, klass: KClass<*>): Any

    /**
     * Parses the JSON string tokens.
     * @param tokens JSON tokens
     * @return string with [tokens] data
     */
    private fun parseString(tokens: JsonTokens): String {
        tokens.pop(DOUBLE_QUOTES)
        return tokens.popWordFinishedWith(DOUBLE_QUOTES)
    }

    /**
     * Parses the JSON array tokens with a class representation.
     *
     * @param tokens JSON tokens
     * @param klass represents the array elements type
     * @return string instance with [tokens] data
     */
    private fun parseArray(tokens: JsonTokens, klass: KClass<*>): List<Any?> {
        val list = mutableListOf<Any?>()
        tokens.pop(ARRAY_OPEN)
        tokens.trim() // Added to allow for empty arrays

        while (tokens.current != ARRAY_END) {
            val value = parse(tokens, klass)
            list.add(value)

            tokens.popCommaIfExists()
            tokens.trim()
        }

        tokens.pop(ARRAY_END)
        return list
    }

    /**
     * Checks if a KClass is parseable: all parameters of the primary constructor must be properties.
     *
     * @param klass represents a class
     * @return true if the [klass] is valid to parse
     */
    protected fun isParseable(klass: KClass<*>): Boolean {
        if (klass in parseableKClasses)
            return true

        val properties = klass.memberProperties.associateBy { kProp -> kProp.name }

        return klass.primaryConstructor?.parameters
            ?.all { kParam -> kParam.name in properties }
            .also { if (it == true) parseableKClasses.add(klass) }
            ?: false
    }

    /**
     * Iterates through the JSON object, creates a setter for each property, and applies it to a specific target.
     *
     * @param tokens JSON tokens
     * @param klass represents a class
     * @param target target to apply setter
     */
    protected fun traverseJsonObject(tokens: JsonTokens, klass: KClass<*>, target: Any) {
        while (tokens.current != OBJECT_END) {
            val propName = tokens.popWordFinishedWith(COLON).trim()
            val setter = setters[klass]?.get(propName)
                ?: throw ParseException("Property $propName doesn't exist")

            setter.apply(target, tokens)

            tokens.popCommaIfExists()
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
    protected fun loadSetters(klass: KClass<*>, hasNoArgsCtor: Boolean): Map<String?, Setter> =
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
    protected abstract fun getSetter(klass: KClass<*>, kParam: KParameter, hasNoArgsCtor: Boolean): Setter
}
