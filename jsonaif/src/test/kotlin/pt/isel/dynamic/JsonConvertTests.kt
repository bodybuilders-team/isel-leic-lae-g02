package pt.isel.dynamic

import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic
import pt.isel.sample.generalTests.Date
import pt.isel.sample.parseWithJsonConvert.cake.Cake
import pt.isel.sample.parseWithJsonConvert.cake.Cake2
import pt.isel.sample.parseWithJsonConvert.cake.Cake6
import pt.isel.sample.parseWithJsonConvert.cake.Cake7
import pt.isel.sample.parseWithJsonConvert.cake.Cake8
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JsonConvertTests {
    @Test
    fun `Parse object with optional mutable property annotated with JsonConvert (AnnotatedParamSetter)`() {
        val json = "{ expDate: \"1998-11-17\" }"
        val cake = JsonParserDynamic.parse(json, Cake::class) as Cake

        assertEquals(cake.expDate, Date(17, 11, 1998))
        assertEquals(cake.mainFlavor, "Cocoa")
    }

    @Test
    fun `Parse object with non optional property throws ParseException`() {
        val json = "{ expDate: \"1998-11-17\" }"

        assertFailsWith<ParseException> {
            JsonParserDynamic.parse(json, Cake2::class) as Cake2
        }
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class is missing convert function`() {
        val json = "{ expDate: \"1998-11-17\" }"

        assertFailsWith<ParseException> {
            JsonParserDynamic.parse(json, Cake6::class) as Cake6
        }
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class doesn't implement JsonConverter interface`() {
        val json = "{ expDate: \"1998-11-17\" }"

        assertFailsWith<ParseException> {
            JsonParserDynamic.parse(json, Cake7::class) as Cake7
        }
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class isn't an object`() {
        val json = "{ expDate: \"1998-11-17\" }"

        assertFailsWith<ParseException> {
            JsonParserDynamic.parse(json, Cake8::class) as Cake8
        }
    }
}
