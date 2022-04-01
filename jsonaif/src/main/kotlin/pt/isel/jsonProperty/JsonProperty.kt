package pt.isel.jsonProperty

/**
 * Replaces the json name representation of the property with the given [value].
 * @property value name of the JSON property
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class JsonProperty(val value: String)
