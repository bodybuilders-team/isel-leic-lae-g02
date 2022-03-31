package pt.isel.json_parser

import pt.isel.COLON
import pt.isel.JsonTokens
import pt.isel.OBJECT_END
import pt.isel.OBJECT_OPEN
import pt.isel.Setter
import pt.isel.getJsonPropertyName
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

// TODO: 21/03/2022 REMOVE DOUBLE BANG OPERATORS !!
/**
 * JSON parser using reflection.
 * @property setters for each domain class we keep a Map<String, Setter> relating properties names with their setters
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
        val unparsedValue = tokens.popWordPrimitive().trim()
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
    override fun parseObject(tokens: JsonTokens, klass: KClass<*>): Any {
        tokens.pop(OBJECT_OPEN)
        tokens.trim() // Added to allow for empty object

        if (!klass.isData)
            throw ParseException("Class ${klass.qualifiedName} is not a data class")

        setters.computeIfAbsent(klass, JsonParserReflect::loadSetters)

        val parsedObject = if (klass.hasOptionalPrimaryConstructor())
            parseObjectWithInstance(tokens, klass)
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
    private fun parseObjectWithInstance(tokens: JsonTokens, klass: KClass<*>): Any {
        val instance = klass.createInstance()

        traverseJsonObject(tokens) { propName ->
            val setter = setters[klass]?.get(propName) ?: throw ParseException("Property $propName doesn't exist")
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
    private fun parseObjectWithCtor(tokens: JsonTokens, klass: KClass<*>): Any {
        val constructorParams = mutableMapOf<KParameter, Any?>()

        traverseJsonObject(tokens) { propName ->
            val setter = setters[klass]?.get(propName) ?: throw ParseException("Property $propName doesn't exist")
            setter.apply(target = constructorParams, tokens)
        }

        return klass.primaryConstructor?.callBy(constructorParams)
            ?: throw ParseException("Klass ${klass.primaryConstructor} not have a primary constructor")
    }

    /**
     * Iterates through the JSON object and calls a [propCb] for each property.
     *
     * @param tokens JSON tokens
     * @param propCb callback to be called with each property
     */
    private fun traverseJsonObject(tokens: JsonTokens, propCb: (String) -> Unit) {
        while (tokens.current != OBJECT_END) {
            val propName = tokens.popWordFinishedWith(COLON).trim()
            propCb(propName)

            popCommaIfExists(tokens)
        }
    }

    /**
     * Loads a map with parameter name as key and the parameter itself as a value
     * for all parameters in the primary constructor of a given [KClass]
     *
     * @param klass representation of a class
     * @return map for accessing a parameter by its name
     */
    private fun loadSetters(klass: KClass<*>): Map<String, Setter> =
        klass.primaryConstructor?.parameters?.associate { kParam ->
            Pair(
                getJsonPropertyName(kParam),
                PropertySetter(klass, kParam)
            )
        } ?: throw ParseException("Klass ${klass.primaryConstructor} not have a primary constructor")
}
