package pt.isel.jsonParser

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

/**
 * Checks if a KClass's primary constructor has all properties mutable and with default values.
 *
 * @return true if a KClass's primary constructor are all mutable properties with default values
 */
fun <T : Any> KClass<T>.hasOptionalPrimaryConstructor(): Boolean {
    val propMap = declaredMemberProperties.associateBy { it.name }

    return primaryConstructor?.valueParameters?.all {
        it.isOptional && propMap.containsKey(it.name) && propMap[it.name] is KMutableProperty<*>
    } ?: throw ParseException("Klass $qualifiedName does not have a primary constructor")
}
