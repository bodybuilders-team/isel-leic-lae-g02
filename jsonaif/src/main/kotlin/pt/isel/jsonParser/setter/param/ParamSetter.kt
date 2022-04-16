package pt.isel.jsonParser.setter.param

import pt.isel.JsonTokens
import pt.isel.jsonParser.setter.AbstractSetter
import pt.isel.jsonParser.setter.Setter
import kotlin.reflect.KParameter

/**
 * Class for setting a parameter based on json tokens.
 *
 * @property kParam the parameter
 */
class ParamSetter(private val kParam: KParameter) : AbstractSetter(kParam), Setter {

    override fun apply(target: Any, tokens: JsonTokens) {
        val propValue = parse(tokens)

        @Suppress("UNCHECKED_CAST")
        val paramMap: MutableMap<KParameter, Any?> = target as MutableMap<KParameter, Any?>
        paramMap[kParam] = propValue
    }
}
