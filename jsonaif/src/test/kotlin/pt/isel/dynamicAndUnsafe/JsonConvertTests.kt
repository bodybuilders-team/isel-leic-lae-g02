package pt.isel.dynamicAndUnsafe

import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamicAndUnsafe
import pt.isel.testfunctions.JsonConvertTestsFunctions
import kotlin.test.Test

class JsonConvertTests {

    @Test
    fun `Parse object with optional mutable property annotated with JsonConvert (AnnotatedParamSetter)`() {
        JsonConvertTestsFunctions.`Parse object with optional mutable property annotated with JsonConvert (AnnotatedParamSetter)`(
            JsonParserDynamicAndUnsafe
        )
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class is missing convert function`() {
        JsonConvertTestsFunctions.`Parse object with property annotated with JsonConvert but the converter class is missing convert function`(
            JsonParserDynamicAndUnsafe
        )
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class doesn't implement JsonConverter interface`() {
        JsonConvertTestsFunctions.`Parse object with property annotated with JsonConvert but the converter class doesn't implement JsonConverter interface`(
            JsonParserDynamicAndUnsafe
        )
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class isn't an object`() {
        JsonConvertTestsFunctions.`Parse object with property annotated with JsonConvert but the converter class isn't an object`(
            JsonParserDynamicAndUnsafe
        )
    }
}
