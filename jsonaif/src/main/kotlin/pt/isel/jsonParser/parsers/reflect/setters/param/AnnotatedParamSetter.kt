package pt.isel.jsonParser.parsers.reflect.setters.param

import pt.isel.JsonTokens
import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonConvert.JsonConvertData
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parsers.reflect.setters.AbstractSetter
import pt.isel.jsonParser.parsers.reflect.setters.Setter
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

/**
 * Class for setting an annotated parameter based on json tokens.
 *
 * @property kParam the parameter associated to the property
 * @property jsonConvertData Data used to convert the JSON value to the property type.
 * If null, the property type is used for parsing.
 */
class AnnotatedParamSetter(private val kParam: KParameter) : AbstractSetter(kParam), Setter {

    private val jsonConvertData = kParam.findAnnotation<JsonConvert>()?.let(::JsonConvertData)
        ?: throw ParseException("Parameter ${kParam.name} doesn't have a JsonConvert annotation")

    override fun apply(target: Any, tokens: JsonTokens) {
        val propValue = jsonConvertData.convert(parse(tokens))

        @Suppress("UNCHECKED_CAST")
        val paramMap: MutableMap<KParameter, Any?> = target as MutableMap<KParameter, Any?>
        paramMap[kParam] = propValue
    }
}
