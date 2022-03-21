package pt.isel

import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

/**
 * Replaces the json name representation of the property with the given [value].
 * @property value name of the JSON property
 */
annotation class JsonProperty(val value: String)


/**
 * Retrieves [JsonProperty.value] or [kProp] name if the annotation is not present.
 * @param kProp Property to get the name from
 * @return [JsonProperty.value] or [kProp] name
 */
fun getJsonPropertyName(kProp: KProperty<*>): String {
	val annotation = kProp.findAnnotation<JsonProperty>()
	return annotation?.value ?: kProp.name
}


/**
 * Retrieves [JsonProperty.value] or [kParam] name if the annotation is not present.
 * @param kParam Parameter to get the name from
 * @return [JsonProperty.value] or [kParam] name
 */
fun getJsonPropertyName(kParam: KParameter): String {
	val annotation = kParam.findAnnotation<JsonProperty>()
	return annotation?.value ?: kParam.name!!
}
