package pt.isel.json_convert

import kotlin.reflect.KClass

/**
 * Allows for different representation of the KClass in JSON format, using a converter.
 * @property converter external JSON converter
 */
annotation class JsonConvert(val converter: KClass<*>)
