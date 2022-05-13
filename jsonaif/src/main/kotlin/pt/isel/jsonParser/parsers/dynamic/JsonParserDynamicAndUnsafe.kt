package pt.isel.jsonParser.parsers.dynamic

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import pt.isel.JsonTokens
import pt.isel.UnsafeUtils.unsafe
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
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmName

/**
 * JSON parser using runtime source file generation (using JavaPoet).
 */
object JsonParserDynamicAndUnsafe : AbstractJsonParserDynamic() {

    /**
     * Gets the [klass] instance with [tokens] data.
     * @param tokens JSON tokens
     * @param klass represents a class
     * @param hasNoArgsCtor true if the [klass]' primary constructor are all mutable properties with default values
     *
     * @return [klass] instance with [tokens] data
     */
    override fun getInstance(tokens: JsonTokens, klass: KClass<*>, hasNoArgsCtor: Boolean): Any {
        val instance = unsafe.allocateInstance(klass.java)

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
    override fun getSetter(klass: KClass<*>, kParam: KParameter, hasNoArgsCtor: Boolean) =
        when {
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
        setter(klass, kParam, kParam.type, valueDeclaration = "value", castToType = false)

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
            valueDeclaration = "${converterClass.qualifiedName}.INSTANCE.convert(value)",
            castToType = true
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
     * @param castToType true if the value should be cast to the property type
     *
     * @return [kParam] setter
     */
    private fun setter(
        klass: KClass<*>,
        kParam: KParameter,
        jsonPropertyKType: KType,
        valueDeclaration: String,
        castToType: Boolean
    ): Setter {
        val kParamName = kParam.name ?: throw ParseException("Param name is null")

        val applyCode = CodeBlock
            .builder()
            .add(getPropertyParsingCode(jsonPropertyKType, castToType))
            .add("unsafe.put${getUnsafeSetterTypeString(jsonPropertyKType)}(target, offset, $valueDeclaration);\n")
            .build()

        val apply = MethodSpec.methodBuilder("apply")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Any::class.java, "target")
            .addParameter(JsonTokens::class.java, "tokens")
            .addCode(applyCode)
            .returns(Void.TYPE)
            .build()

        val unsafeUtils = ClassName.get("pt.isel", "UnsafeUtils")

        val staticBlock = CodeBlock
            .builder()
            .beginControlFlow("try")
            .addStatement(
                "offset = unsafe.objectFieldOffset(${klass.qualifiedName}.class.getDeclaredField(\"$kParamName\"))"
            )
            .nextControlFlow("catch (NoSuchFieldException e)")
            .addStatement("throw new RuntimeException(e)")
            .endControlFlow()
            .build()

        val setter = TypeSpec.classBuilder("DynamicAndUnsafeSetter${klass.simpleName}_$kParamName")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(Setter::class.java)
            .addField(Long::class.java, "offset", Modifier.STATIC, Modifier.PRIVATE)
            .addStaticBlock(staticBlock)
            .addMethod(apply)
            .build()

        val javaFile = JavaFile.builder("", setter)
            .addStaticImport(unsafeUtils, "unsafe")
            .build()

        return loadAndCreateInstance(javaFile) as Setter
    }

    /**
     * Retrieves the unsafe function setter name type string.
     */
    private fun getUnsafeSetterTypeString(propertyKType: KType): String {
        val propertyKlass = propertyKType.classifier as KClass<*>

        return if (basicParser[propertyKlass] != null)
            propertyKlass.jvmName.capitalize()
        else
            "Object"
    }

    /**
     * Gets the code to be used in [setter] regarding the parsing of the property into a variable.
     *
     * If the [propertyKType] is a primitive type, the property is parsed using its primitive string parser.
     * Otherwise, the property is parsed using [JsonParserDynamicAndUnsafe] parse and then cast to the type.
     *
     * @param propertyKType property type
     */
    private fun getPropertyParsingCode(propertyKType: KType, castToType: Boolean): String {
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
            "${if (castToType) kParamObjectTypeName else "Object"} value =" +
                " ${if (castToType) "($kParamObjectTypeName)" else ""}" +
                " ${JsonParserDynamicAndUnsafe::class.qualifiedName}" +
                ".INSTANCE.parse(tokens, kotlin.jvm.JvmClassMappingKt.getKotlinClass(" +
                "${listObjectTypeKlass?.javaObjectType?.name ?: propertyKlass.javaObjectType.name}.class));\n"
    }
}
