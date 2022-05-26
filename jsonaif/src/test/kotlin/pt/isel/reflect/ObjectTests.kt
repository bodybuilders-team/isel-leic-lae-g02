package pt.isel.reflect

import pt.isel.jsonParser.parseNotNull
import pt.isel.jsonParser.parsers.reflect.JsonParserReflect
import pt.isel.sample.parseWithNoOptionalParams.Classroom
import pt.isel.testfunctions.ObjectTestsFunctions
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ObjectTests {

    @Test
    fun `Parse object with optional mutable properties given all properties`() {
        ObjectTestsFunctions.`Parse object with optional mutable properties given all properties`(JsonParserReflect)
    }

    @Test
    fun `Parse object with optional immutable properties given all properties`() {
        ObjectTestsFunctions.`Parse object with optional immutable properties given all properties`(JsonParserReflect)
    }

    @Test
    fun `Parse object without primitives`() {
        ObjectTestsFunctions.`Parse object without primitives`(JsonParserReflect)
    }

    @Test
    fun `Parse object only with primitives`() {
        ObjectTestsFunctions.`Parse object only with primitives`(JsonParserReflect)
    }

    @Test
    fun `Parse object with null property`() {
        ObjectTestsFunctions.`Parse object with null property`(JsonParserReflect)
    }

    @Test
    fun `Parse object with non existent property throws`() {
        ObjectTestsFunctions.`Parse object with non existent property throws`(JsonParserReflect)
    }

    @Test
    fun `Parse object with wrong property type throws`() {
        ObjectTestsFunctions.`Parse object with wrong property type throws`(JsonParserReflect)
    }

    @Test
    fun `Parse object with non optional property`() {
        ObjectTestsFunctions.`Parse object with non optional property`(JsonParserReflect)
    }

    @Test
    fun `Parse empty object works`() {
        ObjectTestsFunctions.`Parse empty object works`(JsonParserReflect)
    }

    @Test
    fun `Parse object having objects as properties works`() {
        ObjectTestsFunctions.`Parse object having objects as properties works`(JsonParserReflect)
    }

    @Test
    fun `Parse object with no primary constructor throws`() {
        ObjectTestsFunctions.`Parse object with no primary constructor throws`(JsonParserReflect)
    }

    // Object with no optional parameters (all required)

    @Test
    fun `Parse object with no optional parameters given all parameters`() {
        ObjectTestsFunctions.`Parse object with no optional parameters given all parameters`(JsonParserReflect)
    }

    @Test
    fun `Parse object with no optional parameters missing parameters`() {
        val json = "{ name: \"LAE\" }"

        assertFailsWith<IllegalArgumentException> {
            JsonParserReflect.parseNotNull<Classroom>(json)
        }
    }

    @Test
    fun `Parse object with null on a not nullable property`() {
        ObjectTestsFunctions.`Parse object with null on a not nullable property`(JsonParserReflect)
    }
}
