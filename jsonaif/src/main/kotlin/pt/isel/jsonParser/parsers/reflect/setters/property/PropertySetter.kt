package pt.isel.jsonParser.parsers.reflect.setters.property

import pt.isel.JsonTokens
import pt.isel.jsonParser.parsers.reflect.setters.AbstractSetter
import pt.isel.jsonParser.parsers.reflect.setters.Setter
import pt.isel.jsonParser.parsers.reflect.setters.propertySetter.IPropertySetter
import pt.isel.jsonParser.parsers.reflect.setters.propertySetter.IPropertySetterImpl
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/**
 * Class for setting a property based on json tokens.
 *
 * @param klass the class that the property belongs to.
 * @property kParam the parameter associated to the property
 * @property kProp the property to be set
 */
class PropertySetter(klass: KClass<*>, private val kParam: KParameter) :
    AbstractSetter(kParam),
    IPropertySetter by IPropertySetterImpl(klass, kParam),
    Setter {

    override fun apply(target: Any, tokens: JsonTokens) {
        val propValue = parse(tokens)

        setterFunction.call(target, propValue)
    }
}
