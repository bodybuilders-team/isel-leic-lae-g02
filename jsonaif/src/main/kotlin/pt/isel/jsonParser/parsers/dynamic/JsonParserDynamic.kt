package pt.isel.jsonParser.parsers.dynamic

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import pt.isel.JsonTokens
import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonConvert.getJsonPropertyType
import pt.isel.jsonParser.AbstractJsonParser
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.basicParser
import pt.isel.jsonParser.capitalize
import pt.isel.jsonParser.loadAndCreateInstance
import pt.isel.jsonParser.parsers.reflect.setters.Setter
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmName

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

        return when {
            isAnnotated -> getAnnotatedPropertySetter(klass, kParam, hasNoArgsCtor)
            else -> getPropertySetter(klass, kParam, hasNoArgsCtor)
        }
    }

    /**
     * Gets the property setter without annotation.
     */
    private fun getPropertySetter(klass: KClass<*>, kParam: KParameter, hasNoArgsCtor: Boolean): Setter {
        val jsonPropertyKlass = kParam.type.classifier as KClass<*>

        return setter(klass, kParam, jsonPropertyKlass, "value")
    }

    private fun getAnnotatedPropertySetter(klass: KClass<*>, kParam: KParameter, hasNoArgsCtor: Boolean): Setter {
        val annotation = kParam.findAnnotation<JsonConvert>()
            ?: throw ParseException("The parameter $kParam is not annotated with @JsonConvert")

        val jsonConverterClass = annotation.converter

        val jsonPropertyKlass = getJsonPropertyType(jsonConverterClass).classifier as KClass<*>

        return setter(klass, kParam, jsonPropertyKlass, "${jsonConverterClass.qualifiedName}.INSTANCE.convert(value)")
    }

    /**
     * Gets the setter, based on the [jsonPropertyKlass] and the [valueDeclaration] expression.
     *
     * @param klass representation of a class
     * @param kParam param to get setter
     * @param jsonPropertyKlass property type
     * @param valueDeclaration expression to be used in the setter
     *
     * @return [kParam] setter
     */
    private fun setter(
        klass: KClass<*>,
        kParam: KParameter,
        jsonPropertyKlass: KClass<*>,
        valueDeclaration: String
    ): Setter {
        val simpleKlassName = klass.qualifiedName ?: throw ParseException("Class name is null")

        val kParamName = kParam.name ?: throw ParseException("Param name is null")

        val applyCode = CodeBlock
            .builder()
            .add(getApplyCodeVariableDeclaration(jsonPropertyKlass))
            .add(
                "(($simpleKlassName)target).set${kParamName.capitalize()}($valueDeclaration);\n"
            )
            .build()

        val apply = MethodSpec.methodBuilder("apply")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Any::class.java, "target")
            .addParameter(JsonTokens::class.java, "tokens")
            .addCode(applyCode)
            .returns(Void.TYPE)
            .build()

        val setter = TypeSpec.classBuilder("Setter${klass.simpleName}_${kParam.name}")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(Setter::class.java)
            .addMethod(apply)
            .build()

        val javaFile = JavaFile.builder("", setter)
            .build()

        return loadAndCreateInstance(javaFile) as Setter
    }

    /**
     * Gets the [kParam] getter.
     */
    private fun getApplyCodeVariableDeclaration(jsonPropertyKlass: KClass<*>): String {
        val kParamObjectTypeName = jsonPropertyKlass.javaObjectType.name

        return if (basicParser[jsonPropertyKlass] != null)
            "${jsonPropertyKlass.jvmName} value = ${jsonPropertyKlass.javaObjectType.simpleName}" +
                ".parse${jsonPropertyKlass.simpleName}(tokens.popWordPrimitive().trim());\n"
        else
            "$kParamObjectTypeName value = ($kParamObjectTypeName) ${JsonParserDynamic::class.qualifiedName}" +
                ".INSTANCE.parse(tokens, kotlin.jvm.JvmClassMappingKt.getKotlinClass($kParamObjectTypeName.class));\n"
    }
}

// TODO: 18/04/2022 JsonProperty annotation
