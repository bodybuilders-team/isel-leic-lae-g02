package pt.isel

import kotlin.reflect.KClass

annotation class JsonConvert(val converter: KClass<*>)

interface JsonConverter<K, T> {
	fun convert(from: K): T
}
