package pt.isel.dynamicAndUnsafe

import pt.isel.jsonParser.parseNotNull
import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamicAndUnsafe
import pt.isel.sample.parseWithNoOptionalParams.Classroom
import pt.isel.testfunctions.ObjectTestsFunctions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ObjectTests {

    @Test
    fun `Parse object with optional mutable properties given all properties`() {
        ObjectTestsFunctions.`Parse object with optional mutable properties given all properties`(
            JsonParserDynamicAndUnsafe
        )
    }

    @Test
    fun `Parse object with optional immutable properties given all properties`() {
        ObjectTestsFunctions.`Parse object with optional immutable properties given all properties`(
            JsonParserDynamicAndUnsafe
        )
    }

    @Test
    fun `Parse object without primitives`() {
        ObjectTestsFunctions.`Parse object without primitives`(JsonParserDynamicAndUnsafe)
    }

    @Test
    fun `Parse object only with primitives`() {
        ObjectTestsFunctions.`Parse object only with primitives`(JsonParserDynamicAndUnsafe)
    }

    @Test
    fun `Parse object with null property`() {
        ObjectTestsFunctions.`Parse object with null property`(JsonParserDynamicAndUnsafe)
    }

    @Test
    fun `Parse object with non existent property throws`() {
        ObjectTestsFunctions.`Parse object with non existent property throws`(JsonParserDynamicAndUnsafe)
    }

    @Test
    fun `Parse object with wrong property type throws`() {
        ObjectTestsFunctions.`Parse object with wrong property type throws`(JsonParserDynamicAndUnsafe)
    }

    @Test
    fun `Parse object with non optional property`() {
        ObjectTestsFunctions.`Parse object with non optional property`(JsonParserDynamicAndUnsafe)
    }

    @Test
    fun `Parse empty object works`() {
        ObjectTestsFunctions.`Parse empty object works`(JsonParserDynamicAndUnsafe)
    }

    @Test
    fun `Parse object having objects as properties works`() {
        ObjectTestsFunctions.`Parse object having objects as properties works`(JsonParserDynamicAndUnsafe)
    }

    @Test
    fun `Parse object with no primary constructor throws`() {
        ObjectTestsFunctions.`Parse object with no primary constructor throws`(JsonParserDynamicAndUnsafe)
    }

    // Object with no optional parameters (all required)

    @Test
    fun `Parse object with no optional parameters given all parameters`() {
        ObjectTestsFunctions.`Parse object with no optional parameters given all parameters`(JsonParserDynamicAndUnsafe)
    }

    @Test
    fun `Parse object with no optional parameters missing parameters`() {
        val json = "{ name: \"LAE\" }"

        val classroom = JsonParserDynamicAndUnsafe.parseNotNull<Classroom>(json)

        assertEquals("LAE", classroom.name)
        assertNull(classroom.students)
    }

    @Test
    fun `Parse object with null on a not nullable property`() {
        val json = "{ name: \"LAE\", students: null }"

        val classroom = JsonParserDynamicAndUnsafe.parseNotNull<Classroom>(json)

        assertEquals("LAE", classroom.name)
        assertNull(classroom.students)
    }
}
