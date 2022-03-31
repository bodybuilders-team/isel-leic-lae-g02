package pt.isel.json_convert

import pt.isel.JsonTokens
import pt.isel.json_parser.parse
import java.lang.reflect.GenericSignatureFormatError
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.memberFunctions

/**
 * Allows for different representation of the KClass in JSON format, using a converter.
 * @property converter external JSON converter
 */
// TODO: 31/03/2022 - Add Target annotation
annotation class JsonConvert(val converter: KClass<*>)

private const val CONVERT_FUNCTION_NAME = "convert"

/**
 * Converts a JSON value to a Kotlin object based on a [converterClass].
 *
 *  @param convertAnnotation annotation that contains the converter class
 *  @property converterClass Class that implements the [JsonConverter] interface
 *  @property convertFunction function that converts a JSON value to a Kotlin object
 *  @property jsonType Type of the JSON value
 */
class JsonConvertData(convertAnnotation: JsonConvert) {
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
    fun parse(jsonTokens: JsonTokens): Any? =
        convertFunction.call(obj, parse(jsonTokens, jsonType))
}
