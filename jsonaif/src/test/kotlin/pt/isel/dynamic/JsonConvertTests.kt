package pt.isel.dynamic

import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parseNotNull
import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic
import pt.isel.sample.parseWithJsonConvert.cake.Cake2
import pt.isel.testfunctions.JsonConvertTestsFunctions
import kotlin.test.Test
import kotlin.test.assertFailsWith

class JsonConvertTests {

    @Test
    fun `Parse object with optional mutable property annotated with JsonConvert (AnnotatedParamSetter)`() {
        JsonConvertTestsFunctions.`Parse object with optional mutable property annotated with JsonConvert (AnnotatedParamSetter)`(
            JsonParserDynamic
        )
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class is missing convert function`() {
        JsonConvertTestsFunctions.`Parse object with property annotated with JsonConvert but the converter class is missing convert function`(
            JsonParserDynamic
        )
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class doesn't implement JsonConverter interface`() {
        JsonConvertTestsFunctions.`Parse object with property annotated with JsonConvert but the converter class doesn't implement JsonConverter interface`(
            JsonParserDynamic
        )
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class isn't an object`() {
        JsonConvertTestsFunctions.`Parse object with property annotated with JsonConvert but the converter class isn't an object`(
            JsonParserDynamic
        )
    }

    @Test
    fun `Parse object with non optional property throws ParseException`() {
        val json = "{ expDate: \"1998-11-17\" }"

        assertFailsWith<ParseException> {
            JsonParserDynamic.parseNotNull<Cake2>(json)
        }
    }
}
