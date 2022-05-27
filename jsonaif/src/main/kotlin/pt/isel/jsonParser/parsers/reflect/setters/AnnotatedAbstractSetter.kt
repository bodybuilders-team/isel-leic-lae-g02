package pt.isel.jsonParser.parsers.reflect.setters

import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonConvert.JsonConvertData
import pt.isel.jsonParser.ParseException
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

/**
 * Class for setting a parameter based on json tokens, using a JsonConvert annotation.
 *
 * @property kParam the parameter
 */
abstract class AnnotatedAbstractSetter(kParam: KParameter) : AbstractSetter(kParam) {

    private val convertAnnotation = kParam.findAnnotation<JsonConvert>()
        ?: throw ParseException("Parameter ${kParam.name} doesn't have a JsonConvert annotation")

    private val jsonConvertData = JsonConvertData(convertAnnotation)

    private val convertFunction: KFunction<*> = jsonConvertData.convertFunction

    private val obj = jsonConvertData.obj

    /**
     * Parses a JSON value to a Kotlin object based on a converter class.
     *
     * @param convObject object to be converted
     * @return the converted object
     */
    protected fun convert(convObject: Any?): Any? = convertFunction.call(obj, convObject)
}
