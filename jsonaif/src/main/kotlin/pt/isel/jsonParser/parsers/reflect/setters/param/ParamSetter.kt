package pt.isel.jsonParser.parsers.reflect.setters.param

import pt.isel.JsonTokens
import pt.isel.jsonParser.parsers.reflect.setters.AbstractSetter
import pt.isel.jsonParser.parsers.reflect.setters.Setter
import kotlin.reflect.KParameter

/**
 * Class for setting a parameter based on JSON tokens.
 *
 * @property kParam the parameter
 */
class ParamSetter(private val kParam: KParameter) : AbstractSetter(kParam), Setter {

    override fun apply(target: Any, tokens: JsonTokens) {
        val propValue = parse(tokens)

        @Suppress("UNCHECKED_CAST")
        (target as MutableMap<KParameter, Any?>)[kParam] = propValue
    }
}
