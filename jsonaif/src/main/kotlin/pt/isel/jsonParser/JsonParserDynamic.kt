package pt.isel.jsonParser

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import pt.isel.JsonTokens
import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonParser.setter.Setter
import pt.isel.jsonParser.setter.param.AnnotatedParamSetter
import pt.isel.jsonParser.setter.param.ParamSetter
import pt.isel.jsonParser.setter.property.AnnotatedPropertySetter
import pt.isel.jsonParser.setter.property.PropertySetter
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation

/**
 * JSON parser using runtime source file generation (using JavaPoet).
 */
object JsonParserDynamic : AbstractJsonParser() {

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

        // TODO: 18/04/2022 Implement setter
        val apply = MethodSpec.methodBuilder("apply")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(Void.TYPE)
            .addParameter(Any::class.java, "target")
            .addParameter(JsonTokens::class.java, "tokens")
            .build()

        val superclass = when {
            isAnnotated && hasNoArgsCtor -> AnnotatedPropertySetter::class.java
            isAnnotated && !hasNoArgsCtor -> AnnotatedParamSetter::class.java
            !isAnnotated && hasNoArgsCtor -> PropertySetter::class.java
            else -> ParamSetter::class.java
        }

        val setter = TypeSpec.classBuilder("Setter${klass.simpleName}_${kParam.name}")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(superclass)
            .addMethod(apply)
            .build()

        val javaFile = JavaFile.builder("pt.isel.jsonParser.setter", setter)
            .build()

        return loadAndCreateInstance(javaFile) as Setter
    }
}
