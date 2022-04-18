package pt.isel.reflect

import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parsers.reflect.JsonParserReflect
import pt.isel.sample.Classroom
import pt.isel.sample.MissingPrimaryConstructor
import pt.isel.sample.Person
import pt.isel.sample.Student
import pt.isel.sample.Student2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ObjectTests {

    @Test
    fun `Parse object with optional mutable properties given all properties`() {
        val json = "{ name: \"Ze Manel\", nr: 7353 }"
        val student = JsonParserReflect.parse(json, Student::class) as Student
        assertEquals("Ze Manel", student.name)
        assertEquals(7353, student.nr)
    }

    @Test
    fun `Parse object with optional immutable properties given all properties`() {
        val json = "{ name: \"Ze Manel\", nr: 7353 }"
        val student = JsonParserReflect.parse(json, Student2::class) as Student2
        assertEquals("Ze Manel", student.name)
        assertEquals(7353, student.nr)
    }

    @Test
    fun `Parse object without primitives`() {
        val json = "{ name: \"Ze Manel\" }"
        val student = JsonParserReflect.parse(json, Student::class) as Student
        assertEquals("Ze Manel", student.name)
        assertEquals(0, student.nr)
    }

    @Test
    fun `Parse object only with primitives`() {
        val json = "{ nr: 1234 }"
        val student = JsonParserReflect.parse(json, Student::class) as Student
        assertEquals(1234, student.nr)
    }

    @Test
    fun `Parse object with null property`() {
        val json = "{ name: null }"
        val student = JsonParserReflect.parse(json, Student::class) as Student
        assertNull(student.name)
    }

    @Test
    fun `Parse object with non existent property throws`() {
        val json = "{ age: \"Ze Manel\" }"
        assertFailsWith<ParseException> {
            JsonParserReflect.parse(json, Student::class) as Student
        }
    }

    @Test
    fun `Parse object with wrong property type throws`() {
        val json = "{ name: 123 }"
        assertFailsWith<ParseException> {
            JsonParserReflect.parse(json, Student::class) as Student
        }
    }

    @Test
    fun `Parse object with non optional property`() {
        val json = "{ id: 94646, name: \"Ze Manel\" }"
        val person = JsonParserReflect.parse(json, Person::class) as Person

        assertEquals(94646, person.id)
        assertEquals("Ze Manel", person.name)
    }

    @Test
    fun `Parse empty object works`() {
        val json = "{ }"
        val student = JsonParserReflect.parse(json, Student::class) as Student

        assertEquals(0, student.nr)
        assertNull(student.name)
    }

    @Test
    fun `parse object having objects as properties works`() {
        val json =
            "{ id: 94646, name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19 }, sibling: { name: \"Kata Badala\" }}"
        val person = JsonParserReflect.parse(json, Person::class) as Person

        assertEquals(94646, person.id)
        assertEquals("Ze Manel", person.name)
        assertEquals(19, person.birth?.day)
        assertEquals(9, person.birth?.month)
        assertEquals(1999, person.birth?.year)
    }

    // Object with no optional parameters (all required)

    @Test
    fun `Parse object with no optional parameters given all parameters`() {
        val json =
            "{ name: \"LAE\", students: [{ name: \"André Páscoa\" }, { name: \"André Jesus\" }, {name: \"Nyckollas Brandão\" }]}"
        val classroom = JsonParserReflect.parse(json, Classroom::class) as Classroom

        assertEquals("LAE", classroom.name)
        assertEquals(3, classroom.students.size)
        assertEquals("André Páscoa", classroom.students[0].name)
        assertEquals("André Jesus", classroom.students[1].name)
        assertEquals("Nyckollas Brandão", classroom.students[2].name)
    }

    @Test
    fun `Parse object with no optional parameters missing parameters throws`() {
        val json = "{ name: \"LAE\" }"

        assertFailsWith<IllegalArgumentException> {
            JsonParserReflect.parse(json, Classroom::class) as Classroom
        }
    }

    @Test
    fun `Parse object with no primary constructor throws`() {
        val json = "{ i: 50 }"

        assertFailsWith<ParseException> {
            JsonParserReflect.parse(json, MissingPrimaryConstructor::class) as MissingPrimaryConstructor
        }
    }

    @Test
    fun `Parse object with null on a not nullable property throws`() {
        val json = "{ name: \"LAE\", students: null }"

        assertFailsWith<ParseException> {
            JsonParserReflect.parse(json, Classroom::class) as Classroom
        }
    }
}
