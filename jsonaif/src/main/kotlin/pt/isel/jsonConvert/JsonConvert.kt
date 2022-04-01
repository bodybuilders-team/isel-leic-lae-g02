package pt.isel.jsonConvert

import kotlin.reflect.KClass

/**
 * Allows for different representation of the KClass in JSON format, using a converter.
 * @property converter external JSON converter
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class JsonConvert(val converter: KClass<*>)
