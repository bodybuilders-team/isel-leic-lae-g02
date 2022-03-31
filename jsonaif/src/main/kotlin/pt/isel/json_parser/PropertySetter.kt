package pt.isel.json_parser

import pt.isel.JsonTokens
import pt.isel.Setter
import pt.isel.json_convert.JsonConvert
import pt.isel.json_convert.JsonConvertData
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * Class for setting a property based on json tokens.
 *
 * @param klass the class that the property belongs to.
 * @property kParam the parameter associated to the property
 * @property kProp the property to be set
 * @property jsonConvertData Data used to convert the JSON value to the property type.
 * If null, the property type is used for parsing.
 */
class PropertySetter(klass: KClass<*>, private val kParam: KParameter) : Setter {
    private val kProp = (
        klass.memberProperties.firstOrNull { it.name == kParam.name }
            ?: throw ParseException("Property ${kParam.name} doesn't exist")
        )

    private val jsonConvertData = kParam.findAnnotation<JsonConvert>()?.let(::JsonConvertData)

    override fun apply(target: Any, tokens: JsonTokens) {
        val propValue = jsonConvertData?.parse(tokens) ?: parse(tokens, kParam.type)

        if (target is MutableMap<*, *>) {
            @Suppress("UNCHECKED_CAST")
            val paramMap: MutableMap<KParameter, Any?> = target as MutableMap<KParameter, Any?>
            paramMap[kParam] = propValue
        } else
            (kProp as KMutableProperty<*>).setter.call(target, propValue)
    }
}
