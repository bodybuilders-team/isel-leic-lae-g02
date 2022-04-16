package pt.isel.jsonParser.setter.property

import pt.isel.JsonTokens
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.setter.AbstractSetter
import pt.isel.jsonParser.setter.Setter
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties

/**
 * Class for setting a property based on json tokens.
 *
 * @param klass the class that the property belongs to.
 * @property kParam the parameter associated to the property
 * @property kProp the property to be set
 */
class PropertySetter(klass: KClass<*>, private val kParam: KParameter) : AbstractSetter(kParam), Setter {
    private val kProp = klass
        .memberProperties
        .firstOrNull { it.name == kParam.name }
        ?: throw ParseException("Property ${kParam.name} doesn't exist")

    override fun apply(target: Any, tokens: JsonTokens) {
        val propValue = parse(tokens)

        (kProp as KMutableProperty<*>).setter.call(target, propValue)
    }
}
