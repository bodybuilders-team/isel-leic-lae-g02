package pt.isel.jsonParser.parsers.dynamic

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import pt.isel.JsonTokens
import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonParser.AbstractJsonParser
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.basicParser
import pt.isel.jsonParser.capitalize
import pt.isel.jsonParser.loadAndCreateInstance
import pt.isel.jsonParser.parsers.reflect.setters.Setter
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
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

        val klassName = klass.qualifiedName ?: throw ParseException("Class name is null")
        val simpleKlassName = klass.qualifiedName ?: throw ParseException("Class name is null")
        val kParamKlass = kParam.type.classifier as KClass<*>
        val kParamKlassName = kParamKlass.qualifiedName ?: throw ParseException("Param type name is null")
        val kParamSimpleKlassName = kParamKlass.simpleName ?: throw ParseException("Param type name is null")
        val kParamName = kParam.name ?: throw ParseException("Param name is null")

        val applyCodeVariableDeclaration = if (basicParser[kParamKlass] != null)
            "${kParamKlass.jvmName} value = ${kParamKlass.javaObjectType.simpleName}" +
                ".parse${kParamKlass.simpleName}(tokens.popWordPrimitive().trim());\n"
        else
            "${kParamKlass.javaObjectType.name} value = (${kParamKlass.javaObjectType.name}) ${JsonParserDynamic::class.qualifiedName}" +
                ".INSTANCE.parse(tokens, kotlin.jvm.JvmClassMappingKt.getKotlinClass(${kParamKlass.javaObjectType.name}.class));\n"

        val applyCode = CodeBlock
            .builder()
            .add(applyCodeVariableDeclaration)
            .add("(($simpleKlassName)target).set${kParamName.capitalize()}(value);\n")
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
}
