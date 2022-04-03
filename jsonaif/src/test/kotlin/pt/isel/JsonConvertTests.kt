package pt.isel

import pt.isel.jsonParser.JsonParserReflect
import pt.isel.sample.Cake
import pt.isel.sample.Cake2
import pt.isel.sample.Cake3
import pt.isel.sample.Cake4
import pt.isel.sample.Cake5
import pt.isel.sample.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JsonConvertTests {
    @Test
    fun `Parse object with optional mutable property annotated with JsonConvert (AnnotatedParamSetter)`() {
        val json = "{ expDate: \"1998-11-17\" }"
        val cake = JsonParserReflect.parse(json, Cake::class) as Cake

        assertEquals(cake.expDate, Date(17, 11, 1998))
        assertEquals(cake.mainFlavor, "Cocoa")
    }

    @Test
    fun `Parse object with non optional property annotated with JsonConvert (AnnotatedPropertySetter)`() {
        val json = "{ expDate: \"1998-11-17\" }"
        val cake = JsonParserReflect.parse(json, Cake2::class) as Cake2

        assertEquals(cake.expDate, Date(17, 11, 1998))
        assertEquals(cake.mainFlavor, "Cocoa")
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class is missing convert function`() {
        val json = "{ expDate: \"1998-11-17\" }"

        assertFailsWith<NotImplementedError> {
            JsonParserReflect.parse(json, Cake3::class) as Cake3
        }
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class doesn't implement JsonConverter interface`() {
        val json = "{ expDate: \"1998-11-17\" }"

        assertFailsWith<NotImplementedError> {
            JsonParserReflect.parse(json, Cake4::class) as Cake4
        }
    }

    @Test
    fun `Parse object with property annotated with JsonConvert but the converter class isn't an object`() {
        val json = "{ expDate: \"1998-11-17\" }"

        assertFailsWith<NotImplementedError> {
            JsonParserReflect.parse(json, Cake5::class) as Cake5
        }
    }
}
