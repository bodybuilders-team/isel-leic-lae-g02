package pt.isel

import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic
import pt.isel.jsonParser.parsers.reflect.JsonParserReflect

fun jsonReflectParse(json: String, javaClass: Class<*>): Any? {
    return JsonParserReflect.parse(json, javaClass.kotlin)
}

fun jsonDynamicParse(json: String, javaClass: Class<*>): Any? {
    return JsonParserDynamic.parse(json, javaClass.kotlin)
}
