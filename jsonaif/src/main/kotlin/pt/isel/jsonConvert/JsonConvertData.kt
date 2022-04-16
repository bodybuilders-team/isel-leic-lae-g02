package pt.isel.jsonConvert

import java.lang.reflect.GenericSignatureFormatError
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.memberFunctions

/**
 * Converts a JSON value to a Kotlin object based on a [converterClass].
 *
 *  @param convertAnnotation annotation that contains the converter class
 *  @property converterClass Class that implements the [JsonConverter] interface
 *  @property convertFunction function that converts a JSON value to a Kotlin object
 *  @property jsonType Type of the JSON value
 */
class JsonConvertData(convertAnnotation: JsonConvert) {

    companion object {
        private const val CONVERT_FUNCTION_NAME = "convert"
    }

    private val converterClass: KClass<*> = convertAnnotation.converter

    private val convertFunction: KFunction<*> = converterClass.run {
        memberFunctions.singleOrNull { it.name == CONVERT_FUNCTION_NAME }
            ?: throw NotImplementedError("JsonConvert argument class should have a convert function")
    }

    private val jsonType: KType = converterClass.run {
        val convertInterface = supertypes.singleOrNull { it.classifier == JsonConverter::class }
            ?: throw NotImplementedError(
                "Class passed as argument to JsonConvert annotation should implement JsonConverter interface"
            )

        convertInterface.arguments.first().type
            ?: throw GenericSignatureFormatError("JsonConverter interface types should not be wildcards")
    }

    private val obj = converterClass.objectInstance
        ?: throw NotImplementedError("Class passed as argument to JsonConvert annotation should be an object class")

    /**
     * Parses a JSON value to a Kotlin object based on a [converterClass].
     */
    fun convert(convObject: Any?): Any? =
        convertFunction.call(obj, convObject)
}
