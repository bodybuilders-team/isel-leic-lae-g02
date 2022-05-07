package pt.isel.jsonParser.parsers.reflect.setters.param

import pt.isel.JsonTokens
import pt.isel.jsonParser.parsers.reflect.setters.AnnotatedAbstractSetter
import pt.isel.jsonParser.parsers.reflect.setters.Setter
import kotlin.reflect.KParameter

/**
 * Class for setting an annotated parameter based on json tokens.
 *
 * @property kParam the parameter associated to the property
 * If null, the property type is used for parsing.
 */
class AnnotatedParamSetter(private val kParam: KParameter) : AnnotatedAbstractSetter(kParam), Setter {

    override fun apply(target: Any, tokens: JsonTokens) {
        val propValue = convert(parse(tokens))

        @Suppress("UNCHECKED_CAST")
        (target as MutableMap<KParameter, Any?>)[kParam] = propValue
    }
}
