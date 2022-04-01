package pt.isel.jsonProperty

import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

/**
 * Retrieves [JsonProperty.value] or [kParam] name if the annotation is not present.
 * @param kParam Parameter to get the name from
 * @return [JsonProperty.value] or [kParam] name
 */
fun getJsonPropertyName(kParam: KParameter): String? = kParam
    .findAnnotation<JsonProperty>()?.value
    ?: kParam.name
