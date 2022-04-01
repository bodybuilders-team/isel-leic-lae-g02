package pt.isel.jsonParser.setter

import pt.isel.JsonTokens
import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonConvert.JsonConvertData
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parse
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * Class for setting an annotated property based on json tokens.
 *
 * @param klass the class that the property belongs to.
 * @property kParam the parameter
 * @property jsonConvertData Data used to convert the JSON value to the property type.
 * If null, the property type is used for parsing.
 */
class AnnotatedPropertySetter(klass: KClass<*>, private val kParam: KParameter) : Setter {
    private val kProp = klass
        .memberProperties
        .firstOrNull { it.name == kParam.name }
        ?: throw ParseException("Property ${kParam.name} doesn't exist")

    private val jsonConvertData = kParam.findAnnotation<JsonConvert>()?.let(::JsonConvertData)

    override fun apply(target: Any, tokens: JsonTokens) {
        val propValue = jsonConvertData?.parse(tokens) ?: parse(tokens, kParam.type)

        (kProp as KMutableProperty<*>).setter.call(target, propValue)
    }
}
