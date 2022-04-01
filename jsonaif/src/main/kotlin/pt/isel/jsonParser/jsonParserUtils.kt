package pt.isel.jsonParser

import pt.isel.JsonTokens
import java.lang.reflect.GenericSignatureFormatError
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

/**
 * Parses [tokens] and returns the value of the parsed object with type [type]
 *
 * @param tokens tokens to parse
 * @param type type of the object to parse
 * @return parsed object
 *
 * @throws ParseException if the object can't be parsed
 */
fun parse(tokens: JsonTokens, type: KType): Any? {
    val klass = type.classifier as KClass<*>
    val propValue =
        if (type.classifier == List::class) {
            val listObjectType = (
                type.arguments.first().type
                    ?: throw GenericSignatureFormatError("List generics cannot have star projection types")
                ).classifier as KClass<*>

            JsonParserReflect.parse(tokens, listObjectType)
        } else
            JsonParserReflect.parse(tokens, klass)

    if (propValue == null && !type.isMarkedNullable)
        throw ParseException("Value of property with type ${klass.qualifiedName} should not be null")

    return propValue
}

/**
 * Checks if a KClass's primary constructor has all properties mutable and with default values.
 *
 * @return true if a KClass's primary constructor are all mutable properties with default values
 */
fun <T : Any> KClass<T>.hasOptionalPrimaryConstructor(): Boolean {
    val propMap = declaredMemberProperties.associateBy { it.name }

    return primaryConstructor?.valueParameters?.all {
        it.isOptional && propMap.containsKey(it.name) && propMap[it.name] is KMutableProperty<*>
    } ?: throw ParseException("Klass $qualifiedName does not have a primary constructor")
}
