package pt.isel.json_parser

import kotlin.reflect.KClass

/**
 * Association between KClass and parser for primitive types.
 */
val basicParser: Map<KClass<*>, (String) -> Any> = mapOf(
    Byte::class to { it.toByte() },
    Short::class to { it.toShort() },
    Int::class to { it.toInt() },
    Long::class to { it.toLong() },
    Float::class to { it.toFloat() },
    Double::class to { it.toDouble() },
    Boolean::class to { it.toBooleanStrict() }
)
