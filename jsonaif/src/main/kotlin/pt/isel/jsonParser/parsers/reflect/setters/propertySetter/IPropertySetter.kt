package pt.isel.jsonParser.parsers.reflect.setters.propertySetter

import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

/**
 * Represents a setter for a property of a class.
 *
 * @property kProp the property to be set
 * @property setterFunction the function to be used to set the property
 */
interface IPropertySetter {
    val kProp: KProperty<*>
    val setterFunction: KFunction<*>
}
