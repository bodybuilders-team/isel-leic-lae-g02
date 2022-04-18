package pt.isel.jsonParser.parsers.reflect.setters.property

import pt.isel.JsonTokens
import pt.isel.jsonParser.parsers.reflect.setters.AnnotatedAbstractSetter
import pt.isel.jsonParser.parsers.reflect.setters.Setter
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter

/**
 * Class for setting an annotated property based on json tokens.
 *
 * @param klass the class that the property belongs to.
 * @property kParam the parameter
 * If null, the property type is used for parsing.
 */
class AnnotatedPropertySetter(klass: KClass<*>, private val kParam: KParameter) :
    AnnotatedAbstractSetter(kParam),
    Setter {
    private val kProp = getKProp(klass, kParam)

    override fun apply(target: Any, tokens: JsonTokens) {
        val propValue = convert(parse(tokens))

        (kProp as KMutableProperty<*>).setter.call(target, propValue)
    }
}
