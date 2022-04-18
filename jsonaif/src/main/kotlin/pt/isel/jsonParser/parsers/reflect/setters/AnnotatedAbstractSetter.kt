package pt.isel.jsonParser.parsers.reflect.setters

import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonParser.ParseException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions

abstract class AnnotatedAbstractSetter(private val kParam: KParameter) : AbstractSetter(kParam) {
    private val convertAnnotation = kParam.findAnnotation<JsonConvert>()
        ?: throw ParseException("Parameter ${kParam.name} doesn't have a JsonConvert annotation")

    companion object {
        private const val CONVERT_FUNCTION_NAME = "convert"
    }

    private val converterClass: KClass<*> = convertAnnotation.converter

    private val convertFunction: KFunction<*> = converterClass.run {
        memberFunctions.singleOrNull { it.name == CONVERT_FUNCTION_NAME }
            ?: throw NotImplementedError("JsonConvert argument class should have a convert function")
    }

    private val obj = converterClass.objectInstance
        ?: throw NotImplementedError("Class passed as argument to JsonConvert annotation should be an object class")

    /**
     * Parses a JSON value to a Kotlin object based on a [converterClass].
     */
    protected fun convert(convObject: Any?): Any? =
        convertFunction.call(obj, convObject)
}
