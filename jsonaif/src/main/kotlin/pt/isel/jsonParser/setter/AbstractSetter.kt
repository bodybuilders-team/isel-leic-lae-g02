package pt.isel.jsonParser.setter

import pt.isel.JsonTokens
import pt.isel.jsonParser.JsonParserReflect
import pt.isel.jsonParser.ParseException
import java.lang.reflect.GenericSignatureFormatError
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

abstract class AbstractSetter(private val kParam: KParameter) {
    private val typeKlass: KClass<*> = kParam.type.classifier as KClass<*>

    private val listObjectType: KClass<*>? = calculateListObjectType()
    private val isNullable = kParam.type.isMarkedNullable

    private fun calculateListObjectType(): KClass<*>? {
        val type = kParam.type
        val kParamKlass = type.classifier as KClass<*>
        if (kParamKlass == List::class) {
            return (
                type.arguments.first().type
                    ?: throw GenericSignatureFormatError("List generics cannot have star projection types")
                ).classifier as KClass<*>
        }

        return null
    }

    fun parse(tokens: JsonTokens): Any? {
        val propValue = if (listObjectType != null)
            JsonParserReflect.parse(tokens, listObjectType)
        else
            JsonParserReflect.parse(tokens, typeKlass)

        if (propValue == null && !isNullable)
            throw ParseException("Value of parameter ${kParam.name} should not be null")

        return propValue
    }
}
