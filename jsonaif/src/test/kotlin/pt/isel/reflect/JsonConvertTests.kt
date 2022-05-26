package pt.isel.reflect

import pt.isel.jsonParser.parseNotNull
import pt.isel.jsonParser.parsers.reflect.JsonParserReflect
import pt.isel.sample.generalTests.Date
import pt.isel.sample.parseWithJsonConvert.cake.Cake2
import pt.isel.testfunctions.JsonConvertTestsFunctions
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonConvertTests {

    @Test
    fun `Parse object with optional mutable property annotated with JsonConvert (AnnotatedParamSetter)`() {
        JsonConvertTestsFunctions.`Parse object with optional mutable property annotated with JsonConvert (AnnotatedParamSetter)`(
            JsonParserReflect
        )
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class is missing convert function`() {
        JsonConvertTestsFunctions.`Parse object with property annotated with JsonConvert but the converter class is missing convert function`(
            JsonParserReflect
        )
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class doesn't implement JsonConverter interface`() {
        JsonConvertTestsFunctions.`Parse object with property annotated with JsonConvert but the converter class doesn't implement JsonConverter interface`(
            JsonParserReflect
        )
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class isn't an object`() {
        JsonConvertTestsFunctions.`Parse object with property annotated with JsonConvert but the converter class isn't an object`(
            JsonParserReflect
        )
    }

    @Test
    fun `Parse object with non optional property annotated with JsonConvert (AnnotatedPropertySetter)`() {
        val json = "{ expDate: \"1998-11-17\" }"
        val cake = JsonParserReflect.parseNotNull<Cake2>(json)

        assertEquals(cake.expDate, Date(17, 11, 1998))
        assertEquals(cake.mainFlavor, "Cocoa")
    }
}
