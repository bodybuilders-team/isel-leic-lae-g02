package pt.isel.jsonParser

import com.squareup.javapoet.JavaFile
import pt.isel.COMMA
import pt.isel.JsonTokens
import java.io.File
import java.net.URLClassLoader
import java.util.Locale
import javax.tools.ToolProvider
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

/**
 * Checks if a KClass's primary constructor has all properties mutable and with default values.
 *
 * @return true if a KClass's primary constructor are all mutable properties with default values
 */
fun <T : Any> KClass<T>.hasOptionalPrimaryConstructor(): Boolean {
    val propMap = declaredMemberProperties.associateBy { it.name }

    return primaryConstructor?.valueParameters?.all {
        it.isOptional && propMap.containsKey(it.name) && propMap[it.name] is KMutableProperty<*>
    } ?: throw ParseException("Klass $qualifiedName does not have a primary constructor")
}

/**
 * Checks if the current token is a COMMA.
 */
fun JsonTokens.popCommaIfExists() {
    if (current == COMMA)
        pop(COMMA)
}

private val root = File("./build")
private val classLoader = URLClassLoader.newInstance(arrayOf(root.toURI().toURL()))
private val compiler = ToolProvider.getSystemJavaCompiler()

/**
 * Loads the specified class and creates an instance of it
 * (considering it has a parameterless constructor).
 *
 * @param source class to load
 *
 * @return created instance
 */
fun loadAndCreateInstance(source: JavaFile): Any {
    // Save source in .java file.
    source.writeToFile(root)

    // Compile source file.
    compiler.run(null, null, null, "${root.path}/${source.typeSpec.name}.java")

    // Load and instantiate compiled class.
    return classLoader
        .loadClass(source.typeSpec.name)
        .getDeclaredConstructor()
        .newInstance()
}

fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
