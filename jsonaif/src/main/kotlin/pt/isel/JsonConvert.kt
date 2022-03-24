package pt.isel

import kotlin.reflect.KClass

// TODO: 22/03/2022 Comment
annotation class JsonConvert(val converter: KClass<*>)

// TODO: 22/03/2022 Comment and maybe put in another file
interface JsonConverter<K, T> {
	fun convert(from: K): T
}
