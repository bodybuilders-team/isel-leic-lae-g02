package pt.isel.jsonParser.setter

import pt.isel.JsonTokens
import pt.isel.jsonParser.parse
import kotlin.reflect.KParameter

/**
 * Class for setting a parameter based on json tokens.
 *
 * @property kParam the parameter
 */
class ParamSetter(private val kParam: KParameter) : Setter {

    override fun apply(target: Any, tokens: JsonTokens) {
        val propValue = parse(tokens, kParam.type)

        @Suppress("UNCHECKED_CAST")
        val paramMap: MutableMap<KParameter, Any?> = target as MutableMap<KParameter, Any?>
        paramMap[kParam] = propValue
    }
}
