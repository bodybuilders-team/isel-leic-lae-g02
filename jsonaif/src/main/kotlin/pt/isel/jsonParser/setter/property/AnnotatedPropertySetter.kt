package pt.isel.jsonParser.setter.property

import pt.isel.JsonTokens
import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonConvert.JsonConvertData
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.setter.AbstractSetter
import pt.isel.jsonParser.setter.Setter
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
class AnnotatedPropertySetter(klass: KClass<*>, private val kParam: KParameter) : AbstractSetter(kParam), Setter {
    private val kProp = klass
        .memberProperties
        .firstOrNull { it.name == kParam.name }
        ?: throw ParseException("Property ${kParam.name} doesn't exist")

    private val jsonConvertData = kParam.findAnnotation<JsonConvert>()?.let(::JsonConvertData)
        ?: throw ParseException("Property ${kParam.name} doesn't have a JsonConvert annotation")

    override fun apply(target: Any, tokens: JsonTokens) {
        val propValue = jsonConvertData.convert(parse(tokens))

        (kProp as KMutableProperty<*>).setter.call(target, propValue)
    }
}
