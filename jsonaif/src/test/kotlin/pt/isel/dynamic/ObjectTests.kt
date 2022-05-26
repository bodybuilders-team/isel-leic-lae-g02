package pt.isel.dynamic

import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parseNotNull
import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic
import pt.isel.sample.generalTests.Person
import pt.isel.sample.generalTests.student.Student2
import pt.isel.sample.parseWithNoOptionalParams.Classroom
import pt.isel.testfunctions.ObjectTestsFunctions
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ObjectTests {

    @Test
    fun `Parse object with optional mutable properties given all properties`() {
        ObjectTestsFunctions.`Parse object with optional mutable properties given all properties`(JsonParserDynamic)
    }

    @Test
    fun `Parse object with optional immutable properties given all properties`() {
        val json = "{ name: \"Ze Manel\", nr: 7353 }"
        assertFailsWith<ParseException> {
            JsonParserDynamic.parseNotNull<Student2>(json)
        }
    }

    @Test
    fun `Parse object without primitives`() {
        ObjectTestsFunctions.`Parse object without primitives`(JsonParserDynamic)
    }

    @Test
    fun `Parse object only with primitives`() {
        ObjectTestsFunctions.`Parse object only with primitives`(JsonParserDynamic)
    }

    @Test
    fun `Parse object with null property`() {
        ObjectTestsFunctions.`Parse object with null property`(JsonParserDynamic)
    }

    @Test
    fun `Parse object with non existent property throws`() {
        ObjectTestsFunctions.`Parse object with non existent property throws`(JsonParserDynamic)
    }

    @Test
    fun `Parse object with wrong property type throws`() {
        ObjectTestsFunctions.`Parse object with wrong property type throws`(JsonParserDynamic)
    }

    @Test
    fun `Parse object with non optional property`() {
        val json = "{ id: 94646, name: \"Ze Manel\" }"

        assertFailsWith<ParseException> {
            JsonParserDynamic.parseNotNull<Person>(json)
        }
    }

    @Test
    fun `Parse empty object works`() {
        ObjectTestsFunctions.`Parse empty object works`(JsonParserDynamic)
    }

    @Test
    fun `Parse object having objects as properties works`() {
        ObjectTestsFunctions.`Parse object having objects as properties works`(JsonParserDynamic)
    }

    @Test
    fun `Parse object with no primary constructor throws`() {
        ObjectTestsFunctions.`Parse object with no primary constructor throws`(JsonParserDynamic)
    }

    // Object with no optional parameters (all required)

    @Test
    fun `Parse object with no optional parameters given all parameters`() {
        val json =
            "{ name: \"LAE\", students: [{ name: \"André Páscoa\" }, { name: \"André Jesus\" }, {name: \"Nyckollas Brandão\" }]}"

        assertFailsWith<ParseException> {
            JsonParserDynamic.parseNotNull<Classroom>(json)
        }
    }

    @Test
    fun `Parse object with no optional parameters missing parameters`() {
        val json = "{ name: \"LAE\" }"

        assertFailsWith<ParseException> {
            JsonParserDynamic.parseNotNull<Classroom>(json)
        }
    }

    @Test
    fun `Parse object with null on a not nullable property`() {
        ObjectTestsFunctions.`Parse object with null on a not nullable property`(JsonParserDynamic)
    }
}
