package pt.isel.testfunctions

import pt.isel.jsonParser.JsonParser
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parseNotNull
import pt.isel.sample.generalTests.Date
import pt.isel.sample.parseWithJsonConvert.cake.Cake
import pt.isel.sample.parseWithJsonConvert.cake.Cake3
import pt.isel.sample.parseWithJsonConvert.cake.Cake4
import pt.isel.sample.parseWithJsonConvert.cake.Cake5
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

object JsonConvertTestsFunctions {

    fun `Parse object with optional mutable property annotated with JsonConvert (AnnotatedParamSetter)`(jsonParser: JsonParser) {
        val json = "{ expDate: \"1998-11-17\", mainFlavor: \"Vanilla\" }"
        val cake = jsonParser.parseNotNull<Cake>(json)

        assertEquals(cake.expDate, Date(17, 11, 1998))
        assertEquals(cake.mainFlavor, "Vanilla")
    }

    fun `Parse object with property annotated with JsonConvert but the converter class is missing convert function`(
        jsonParser: JsonParser
    ) {
        val json = "{ expDate: \"1998-11-17\" }"

        assertFailsWith<ParseException> {
            jsonParser.parseNotNull<Cake3>(json)
        }
    }

    fun `Parse object with property annotated with JsonConvert but the converter class doesn't implement JsonConverter interface`(
        jsonParser: JsonParser
    ) {
        val json = "{ expDate: \"1998-11-17\" }"

        assertFailsWith<ParseException> {
            jsonParser.parseNotNull<Cake4>(json)
        }
    }

    fun `Parse object with property annotated with JsonConvert but the converter class isn't an object`(jsonParser: JsonParser) {
        val json = "{ expDate: \"1998-11-17\" }"

        assertFailsWith<ParseException> {
            jsonParser.parseNotNull<Cake5>(json)
        }
    }
}
