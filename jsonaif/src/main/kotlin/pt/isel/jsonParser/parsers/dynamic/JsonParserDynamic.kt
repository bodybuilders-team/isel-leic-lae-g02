package pt.isel.jsonParser.parsers.dynamic

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import pt.isel.JsonTokens
import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonConvert.JsonConvertData
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.basicParser
import pt.isel.jsonParser.capitalize
import pt.isel.jsonParser.loadAndCreateInstance
import pt.isel.jsonParser.parsers.reflect.setters.Setter
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmName

/**
 * JSON parser using runtime source file generation (using JavaPoet).
 */
object JsonParserDynamic : AbstractJsonParserDynamic() {

    /**
     * Gets the [klass] instance with [tokens] data.
     *
     * @param tokens JSON tokens
     * @param klass represents a class
     * @param hasNoArgsCtor true if the [klass]' primary constructor are all mutable properties with default values
     *
     * @return [klass] instance with [tokens] data
     */
    override fun <T : Any> getInstance(tokens: JsonTokens, klass: KClass<T>, hasNoArgsCtor: Boolean): T {
        val instance = klass.createInstance()

        traverseJsonObject(tokens, klass, instance)

        return instance
    }

    /**
     * Gets the [kParam] setter.
     *
     * If [kParam] has a [JsonConvert] annotation, calls [getAnnotatedPropertySetter] otherwise calls [getPropertySetter].
     *
     * @param klass representation of a class
     * @param kParam param to get setter
     * @param hasNoArgsCtor true if the [klass]' primary constructor are all mutable properties with default values
     *
     * @return [kParam] setter
     * @throws ParseException if [hasNoArgsCtor] is false
     */
    override fun <T : Any> getSetter(klass: KClass<T>, kParam: KParameter, hasNoArgsCtor: Boolean) =
        when {
            !hasNoArgsCtor ->
                throw ParseException(
                    "Dynamic parser only parses objects that primary constructor are " +
                        "all mutable properties with default values"
                )
            kParam.hasAnnotation<JsonConvert>() -> getAnnotatedPropertySetter(klass, kParam)
            else -> getPropertySetter(klass, kParam)
        }

    /**
     * Gets the property setter without [JsonConvert] annotation.
     *
     * @param klass representation of a class
     * @param kParam param to get setter
     *
     * @return [kParam] setter
     */
    private fun <T : Any> getPropertySetter(klass: KClass<T>, kParam: KParameter): Setter =
        setter(klass, kParam, kParam.type, valueDeclaration = "value")

    /**
     * Gets the property setter knowing it has a [JsonConvert] annotation.
     * As such, in the setter, the value should be converted by calling the convert function
     * of the converter class passed to [JsonConvert].
     *
     * @param klass representation of a class
     * @param kParam param to get setter
     *
     * @return [kParam] setter
     */
    private fun <T : Any> getAnnotatedPropertySetter(klass: KClass<T>, kParam: KParameter): Setter {
        val annotation = kParam.findAnnotation<JsonConvert>()
            ?: throw ParseException("The parameter $kParam is not annotated with @JsonConvert")

        val jsonConvertData = JsonConvertData(annotation)
        val converterClass = jsonConvertData.converterClass

        return setter(
            klass,
            kParam,
            jsonConvertData.propType,
            valueDeclaration = "${converterClass.qualifiedName}.INSTANCE.convert(value)"
        )
    }

    /**
     * Gets the setter, by building a specific setter class for the property and a .java file for it.
     *
     * The code of apply method of the setter consists of the following:
     * - the parsing of the property into a variable
     * - the call to the setter of the property
     *
     * @param klass representation of a class
     * @param kParam param to get setter
     * @param jsonPropertyKType property type
     * @param valueDeclaration expression to be used in the setter
     *
     * @return [kParam] setter
     */
    private fun setter(
        klass: KClass<*>,
        kParam: KParameter,
        jsonPropertyKType: KType,
        valueDeclaration: String
    ): Setter {
        val klassName = klass.qualifiedName ?: throw ParseException("Class name is null")
        val kParamName = kParam.name ?: throw ParseException("Param name is null")

        val applyCode = CodeBlock
            .builder()
            .add(getPropertyParsingCode(jsonPropertyKType))
            .add("(($klassName)target).set${kParamName.capitalize()}($valueDeclaration);\n")
            .build()

        val apply = MethodSpec.methodBuilder("apply")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Any::class.java, "target")
            .addParameter(JsonTokens::class.java, "tokens")
            .addCode(applyCode)
            .returns(Void.TYPE)
            .build()

        val setter = TypeSpec.classBuilder("DynamicSetter${klass.simpleName}_$kParamName")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(Setter::class.java)
            .addMethod(apply)
            .build()

        val javaFile = JavaFile.builder("", setter)
            .build()

        return loadAndCreateInstance(javaFile) as Setter
    }

    /**
     * Gets the code to be used in [setter] regarding the parsing of the property into a variable.
     *
     * If the [propertyKType] is a primitive type, the property is parsed using its primitive string parser.
     * Otherwise, the property is parsed using [JsonParserDynamic] parse and then cast to the type.
     *
     * @param propertyKType property type
     *
     * @return parsing code
     */
    private fun getPropertyParsingCode(propertyKType: KType): String {
        val propertyKlass = propertyKType.classifier as KClass<*>
        val listObjectTypeKlass: KClass<*>? = getListObjectType(propertyKType, propertyKlass)

        val kParamObjectTypeName = if (listObjectTypeKlass == null)
            propertyKlass.javaObjectType.name
        else
            propertyKType.javaType.typeName

        return if (basicParser[propertyKlass] != null)
            "${propertyKlass.jvmName} value = ${propertyKlass.javaObjectType.simpleName}" +
                ".parse${propertyKlass.simpleName}(tokens.popWordPrimitive().trim());\n"
        else
            "$kParamObjectTypeName value = ($kParamObjectTypeName) ${JsonParserDynamic::class.qualifiedName}" +
                ".INSTANCE.parse${if (listObjectTypeKlass != null) "Array" else ""}(tokens, kotlin.jvm.JvmClassMappingKt.getKotlinClass(" +
                "${listObjectTypeKlass?.javaObjectType?.name ?: propertyKlass.javaObjectType.name}.class));\n"
    }
}
