package pt.isel.jsonParser.parsers.dynamic

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import pt.isel.JsonTokens
import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonConvert.JsonConvertData
import pt.isel.jsonParser.AbstractJsonParser
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.basicParser
import pt.isel.jsonParser.capitalize
import pt.isel.jsonParser.loadAndCreateInstance
import pt.isel.jsonParser.parsers.reflect.setters.Setter
import java.lang.reflect.GenericSignatureFormatError
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmName

/**
 * JSON parser using runtime source file generation (using JavaPoet).
 */
object JsonParserDynamic : AbstractJsonParser() {

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
    override fun getSetter(klass: KClass<*>, kParam: KParameter, hasNoArgsCtor: Boolean) =
        when {
            !hasNoArgsCtor ->
                throw ParseException(
                    "Dynamic parser only parses objects that primary constructor are all mutable properties with default values"
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
    private fun getPropertySetter(klass: KClass<*>, kParam: KParameter): Setter =
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
    private fun getAnnotatedPropertySetter(klass: KClass<*>, kParam: KParameter): Setter {
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
     * Gets the setter, by building a specific setter class for the property a .java file for it.
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

        val setter = TypeSpec.classBuilder("Setter${klass.simpleName}_$kParamName")
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
                ".INSTANCE.parse(tokens, kotlin.jvm.JvmClassMappingKt.getKotlinClass(" +
                "${listObjectTypeKlass?.javaObjectType?.name ?: propertyKlass.javaObjectType.name}.class));\n"
    }

    /**
     * Gets the type of the list object.
     *
     * @param propertyKType JSON property KType
     * @param propertyKlass JSON property KClass
     *
     * @return representation of the list object type
     */
    private fun getListObjectType(propertyKType: KType, propertyKlass: KClass<*>): KClass<*>? =
        if (propertyKlass == List::class) {
            val type = propertyKType.arguments.first().type
                ?: throw GenericSignatureFormatError("List generics cannot have star projection types")

            type.classifier as KClass<*>
        } else null
}
