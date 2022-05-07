package pt.isel.jsonParser.parsers.reflect.setters

import pt.isel.jsonParser.ParseException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

interface IPropertySetter {
    val kProp: KProperty<*>
    val setterFunction: KFunction<*>
}

class IPropertySetterImpl(klass: KClass<*>, kParam: KParameter) : IPropertySetter {
    override val kProp = getKProp(klass, kParam)
    override val setterFunction = (kProp as KMutableProperty<*>).setter

    companion object {
        /**
         * Get the KProperty from the KParameter
         *
         * @param klass the class of the object
         * @param kParam the KParameter
         *
         * @return The [KProperty] object
         * @throws ParseException if the KParameter is not a KProperty
         */
        private fun getKProp(klass: KClass<*>, kParam: KParameter) =
            klass
                .memberProperties
                .firstOrNull { it.name == kParam.name }
                ?: throw ParseException("Property ${kParam.name} doesn't exist")
    }
}
