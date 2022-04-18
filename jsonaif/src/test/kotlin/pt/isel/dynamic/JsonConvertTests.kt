package pt.isel.dynamic

import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic
import pt.isel.sample.Date
import pt.isel.sample.jsonconvert.Cake
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonConvertTests {
    @Test
    fun `Parse object with optional mutable property annotated with JsonConvert (AnnotatedParamSetter)`() {
        val json = "{ expDate: \"1998-11-17\" }"
        val cake = JsonParserDynamic.parse(json, Cake::class) as Cake

        assertEquals(cake.expDate, Date(17, 11, 1998))
        assertEquals(cake.mainFlavor, "Cocoa")
    }
}
