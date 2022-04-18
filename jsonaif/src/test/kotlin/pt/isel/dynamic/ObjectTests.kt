package pt.isel.dynamic

import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic
import pt.isel.sample.MissingPrimaryConstructor
import pt.isel.sample.Student
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ObjectTests {

    @Test
    fun `Parse object with optional mutable properties given all properties`() {
        val json = "{ name: \"Ze Manel\", nr: 7353 }"
        val student = JsonParserDynamic.parse(json, Student::class) as Student
        assertEquals("Ze Manel", student.name)
        assertEquals(7353, student.nr)
    }

    @Test
    fun `Parse object without primitives`() {
        val json = "{ name: \"Ze Manel\" }"
        val student = JsonParserDynamic.parse(json, Student::class) as Student
        assertEquals("Ze Manel", student.name)
        assertEquals(0, student.nr)
    }

    @Test
    fun `Parse object only with primitives`() {
        val json = "{ nr: 1234 }"
        val student = JsonParserDynamic.parse(json, Student::class) as Student
        assertEquals(1234, student.nr)
    }

    @Test
    fun `Parse object with null property`() {
        val json = "{ name: null }"
        val student = JsonParserDynamic.parse(json, Student::class) as Student
        assertNull(student.name)
    }

    @Test
    fun `Parse object with non existent property throws`() {
        val json = "{ age: \"Ze Manel\" }"
        assertFailsWith<ParseException> {
            JsonParserDynamic.parse(json, Student::class) as Student
        }
    }

    @Test
    fun `Parse object with wrong property type throws`() {
        val json = "{ name: 123 }"
        assertFailsWith<ParseException> {
            JsonParserDynamic.parse(json, Student::class) as Student
        }
    }

    @Test
    fun `Parse empty object works`() {
        val json = "{ }"
        val student = JsonParserDynamic.parse(json, Student::class) as Student

        assertEquals(0, student.nr)
        assertNull(student.name)
    }

    // Object with no optional parameters (all required)

    @Test
    fun `Parse object with no primary constructor throws`() {
        val json = "{ i: 50 }"

        assertFailsWith<ParseException> {
            JsonParserDynamic.parse(json, MissingPrimaryConstructor::class) as MissingPrimaryConstructor
        }
    }
}
