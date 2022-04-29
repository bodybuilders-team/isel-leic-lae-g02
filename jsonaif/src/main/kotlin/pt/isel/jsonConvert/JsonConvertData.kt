package pt.isel.jsonConvert

import pt.isel.jsonParser.ParseException
import java.lang.reflect.GenericSignatureFormatError
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions

/**
 * Gets data relative to JsonConvert annotation.
 *
 * @property converterClass the class that follows [JsonConverter] interface
 */
data class JsonConvertData(val convertAnnotation: JsonConvert) {
    val converterClass: KClass<*> = convertAnnotation.converter

    val propType = converterClass.run {
        val convertInterface = supertypes.singleOrNull { it.classifier == JsonConverter::class }
            ?: throw ParseException(
                "Class passed as argument to JsonConvert annotation should implement JsonConverter interface"
            )

        convertInterface.arguments.first().type
            ?: throw GenericSignatureFormatError("JsonConverter interface types should not be wildcards")
    }

    val convertFunction: KFunction<*> = converterClass.run {
        memberFunctions.singleOrNull { it.name == "convert" }
            ?: throw ParseException("JsonConvert argument class should have a convert function")
    }

    val obj = converterClass.objectInstance
        ?: throw ParseException("Class passed as argument to JsonConvert annotation should be an object class")
}