package pt.isel.jsonConvert

import java.lang.reflect.GenericSignatureFormatError
import kotlin.reflect.KClass
import kotlin.reflect.KType

fun getJsonPropertyType(converterClass: KClass<*>): KType =
    converterClass.run {
        val convertInterface = supertypes.singleOrNull { it.classifier == JsonConverter::class }
            ?: throw NotImplementedError(
                "Class passed as argument to JsonConvert annotation should implement JsonConverter interface"
            )

        convertInterface.arguments.first().type
            ?: throw GenericSignatureFormatError("JsonConverter interface types should not be wildcards")
    }
