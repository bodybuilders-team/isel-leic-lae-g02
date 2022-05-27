package pt.isel

import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic
import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamicAndUnsafe
import pt.isel.jsonParser.parsers.reflect.JsonParserReflect

/**
 * Parses a JSON string using the [JsonParserReflect] parser.
 *
 * @param json the JSON string to parse
 * @param javaClass the class of the object to parse
 *
 * @return the parsed object
 */
fun jsonReflectParse(json: String, javaClass: Class<*>): Any? {
    return JsonParserReflect.parse(json, javaClass.kotlin)
}

/**
 * Parses a JSON string using the [JsonParserDynamic] parser.
 *
 * @param json the JSON string to parse
 * @param javaClass the class of the object to parse
 *
 * @return the parsed object
 */
fun jsonDynamicParse(json: String, javaClass: Class<*>): Any? {
    return JsonParserDynamic.parse(json, javaClass.kotlin)
}

/**
 * Parses a JSON string using the [JsonParserDynamicAndUnsafe] parser.
 *
 * @param json the JSON string to parse
 * @param javaClass the class of the object to parse
 *
 * @return the parsed object
 */
fun jsonDynamicAndUnsafeParse(json: String, javaClass: Class<*>): Any? {
    return JsonParserDynamicAndUnsafe.parse(json, javaClass.kotlin)
}
