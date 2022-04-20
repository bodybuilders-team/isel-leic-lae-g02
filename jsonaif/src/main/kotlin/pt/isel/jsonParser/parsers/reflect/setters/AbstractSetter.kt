package pt.isel.jsonParser.parsers.reflect.setters

import pt.isel.JsonTokens
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parsers.reflect.JsonParserReflect
import java.lang.reflect.GenericSignatureFormatError
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/**
 * Represents and Abstract Setter.
 */
abstract class AbstractSetter(private val kParam: KParameter) {

    private val typeKlass: KClass<*> = kParam.type.classifier as KClass<*>

    private val listObjectTypeKlass: KClass<*>? = getListObjectType()
    private val isNullable = kParam.type.isMarkedNullable

    /**
     * Gets the type of the list object.
     * @return representation of the list object type
     */
    private fun getListObjectType(): KClass<*>? {
        val kParamType = kParam.type
        val kParamKlass = kParamType.classifier as KClass<*>

        return if (kParamKlass == List::class) {
            val type = kParamType.arguments.first().type
                ?: throw GenericSignatureFormatError("List generics cannot have star projection types")

            type.classifier as KClass<*>
        } else null
    }

    /**
     * Parses the JSON tokens with a class representation.
     * The class representation can be [listObjectTypeKlass] if the param is a list
     * or [typeKlass] otherwise.
     *
     * @param tokens JSON tokens
     *
     * @return parsed value
     */
    fun parse(tokens: JsonTokens): Any? {
        val propValue = if (listObjectTypeKlass != null)
            JsonParserReflect.parse(tokens, listObjectTypeKlass)
        else
            JsonParserReflect.parse(tokens, typeKlass)

        if (propValue == null && !isNullable)
            throw ParseException("Value of parameter ${kParam.name} should not be null")

        return propValue
    }
}
