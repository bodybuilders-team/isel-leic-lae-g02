package pt.isel.jsonParser

import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonParser.JsonParserReflect.setters
import pt.isel.jsonParser.setter.Setter
import pt.isel.jsonParser.setter.param.AnnotatedParamSetter
import pt.isel.jsonParser.setter.param.ParamSetter
import pt.isel.jsonParser.setter.property.AnnotatedPropertySetter
import pt.isel.jsonParser.setter.property.PropertySetter
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation

/**
 * JSON parser using reflection.
 * @property setters for each domain class we keep a Map<String, Setter> relating properties names with their setters
 */
object JsonParserReflect : AbstractJsonParser() {

    /**
     * Gets the [kParam] setter.
     *
     * @param klass representation of a class
     * @param kParam param to get setter
     * @param hasNoArgsCtor true if the [klass]' primary constructor are all mutable properties with default values
     *
     * @return [kParam] setter
     */
    override fun getSetter(klass: KClass<*>, kParam: KParameter, hasNoArgsCtor: Boolean): Setter {
        val isAnnotated = kParam.hasAnnotation<JsonConvert>()

        return when {
            isAnnotated && hasNoArgsCtor -> AnnotatedPropertySetter(klass, kParam)
            isAnnotated && !hasNoArgsCtor -> AnnotatedParamSetter(kParam)
            !isAnnotated && hasNoArgsCtor -> PropertySetter(klass, kParam)
            else -> ParamSetter(kParam)
        }
    }
}
