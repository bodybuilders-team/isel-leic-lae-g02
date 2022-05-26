package pt.isel.testfunctions

import pt.isel.jsonParser.JsonParser
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parseNotNull
import pt.isel.sample.generalTests.MissingPrimaryConstructor
import pt.isel.sample.generalTests.Person
import pt.isel.sample.generalTests.Person2
import pt.isel.sample.generalTests.student.Student
import pt.isel.sample.generalTests.student.Student2
import pt.isel.sample.parseWithNoOptionalParams.Classroom
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

object ObjectTestsFunctions {

    fun `Parse object with optional mutable properties given all properties`(jsonParser: JsonParser) {
        val json = "{ name: \"Ze Manel\", nr: 7353 }"
        val student = jsonParser.parseNotNull<Student>(json)
        assertEquals("Ze Manel", student.name)
        assertEquals(7353, student.nr)
    }

    fun `Parse object with optional immutable properties given all properties`(jsonParser: JsonParser) {
        val json = "{ name: \"Ze Manel\", nr: 7353 }"
        val student = jsonParser.parseNotNull<Student2>(json)
        assertEquals("Ze Manel", student.name)
        assertEquals(7353, student.nr)
    }

    fun `Parse object without primitives`(jsonParser: JsonParser) {
        val json = "{ name: \"Ze Manel\" }"
        val student = jsonParser.parseNotNull<Student>(json)
        assertEquals("Ze Manel", student.name)
        assertEquals(0, student.nr)
    }

    fun `Parse object only with primitives`(jsonParser: JsonParser) {
        val json = "{ nr: 1234 }"
        val student = jsonParser.parseNotNull<Student>(json)
        assertEquals(1234, student.nr)
    }

    fun `Parse object with null property`(jsonParser: JsonParser) {
        val json = "{ name: null }"
        val student = jsonParser.parseNotNull<Student>(json)
        assertNull(student.name)
    }

    fun `Parse object with non existent property throws`(jsonParser: JsonParser) {
        val json = "{ age: \"Ze Manel\" }"
        assertFailsWith<ParseException> {
            jsonParser.parseNotNull<Student>(json)
        }
    }

    fun `Parse object with wrong property type throws`(jsonParser: JsonParser) {
        val json = "{ name: 123 }"
        assertFailsWith<ParseException> {
            jsonParser.parseNotNull<Student>(json)
        }
    }

    fun `Parse object with non optional property`(jsonParser: JsonParser) {
        val json = "{ id: 94646, name: \"Ze Manel\" }"
        val person = jsonParser.parseNotNull<Person>(json)

        assertEquals(94646, person.id)
        assertEquals("Ze Manel", person.name)
    }

    fun `Parse empty object works`(jsonParser: JsonParser) {
        val json = "{ }"
        val student = jsonParser.parseNotNull<Student>(json)

        assertEquals(0, student.nr)
        assertNull(student.name)
    }

    fun `Parse object having objects as properties works`(jsonParser: JsonParser) {
        val json =
            "{ id: 94646, name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19 }, sibling: { name: \"Kata Badala\" }}"
        val person = jsonParser.parseNotNull<Person2>(json)

        assertEquals(94646, person.id)
        assertEquals("Ze Manel", person.name)
        assertEquals(19, person.birth?.day)
        assertEquals(9, person.birth?.month)
        assertEquals(1999, person.birth?.year)
    }

    fun `Parse object with no primary constructor throws`(jsonParser: JsonParser) {
        val json = "{ i: 50 }"

        assertFailsWith<ParseException> {
            jsonParser.parseNotNull<MissingPrimaryConstructor>(json)
        }
    }

    // Object with no optional parameters (all required)

    fun `Parse object with no optional parameters given all parameters`(jsonParser: JsonParser) {
        val json =
            "{ name: \"LAE\", students: [{ name: \"André Páscoa\" }, { name: \"André Jesus\" }, {name: \"Nyckollas Brandão\" }]}"
        val classroom = jsonParser.parseNotNull<Classroom>(json)

        assertEquals("LAE", classroom.name)
        assertEquals(3, classroom.students.size)
        assertEquals("André Páscoa", classroom.students[0].name)
        assertEquals("André Jesus", classroom.students[1].name)
        assertEquals("Nyckollas Brandão", classroom.students[2].name)
    }

    fun `Parse object with null on a not nullable property`(jsonParser: JsonParser) {
        val json = "{ name: \"LAE\", students: null }"

        assertFailsWith<ParseException> {
            jsonParser.parseNotNull<Classroom>(json)
        }
    }
}
