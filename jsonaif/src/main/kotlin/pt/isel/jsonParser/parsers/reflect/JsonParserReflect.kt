package pt.isel.jsonParser.parsers.reflect

import pt.isel.JsonTokens
import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonParser.AbstractJsonParser
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parsers.reflect.JsonParserReflect.setters
import pt.isel.jsonParser.parsers.reflect.setters.Setter
import pt.isel.jsonParser.parsers.reflect.setters.param.AnnotatedParamSetter
import pt.isel.jsonParser.parsers.reflect.setters.param.ParamSetter
import pt.isel.jsonParser.parsers.reflect.setters.property.AnnotatedPropertySetter
import pt.isel.jsonParser.parsers.reflect.setters.property.PropertySetter
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

/**
 * JSON parser using reflection.
 *
 * @property setters for each domain class we keep a Map<String, Setter> relating properties names with their setters
 */
object JsonParserReflect : AbstractJsonParser() {

    override fun getInstance(tokens: JsonTokens, klass: KClass<*>, hasNoArgsCtor: Boolean): Any =
        if (hasNoArgsCtor)
            parseObjectWithInstance(tokens, klass)
        else
            parseObjectWithCtor(tokens, klass)

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
     * Gets the [kParam] setter.
     *
     * @param klass representation of a class
     * @param kParam param to get setter
     * @param hasNoArgsCtor true if the [klass]' primary constructor are all mutable properties with default values
     *
     * @return [kParam] setter
     */
    override fun getSetter(klass: KClass<*>, kParam: KParameter, hasNoArgsCtor: Boolean): Setter {
        val isAnnotated = kParam.hasAnnotation<JsonConvert>()

        return when {
            isAnnotated && hasNoArgsCtor -> AnnotatedPropertySetter(klass, kParam)
            isAnnotated && !hasNoArgsCtor -> AnnotatedParamSetter(kParam)
            !isAnnotated && hasNoArgsCtor -> PropertySetter(klass, kParam)
            else -> ParamSetter(kParam)
        }
    }
}
