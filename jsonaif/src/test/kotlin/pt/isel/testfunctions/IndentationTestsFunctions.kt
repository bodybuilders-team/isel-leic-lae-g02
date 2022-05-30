package pt.isel.testfunctions

import pt.isel.jsonParser.JsonParser
import pt.isel.jsonParser.parseArrayNotNull
import pt.isel.jsonParser.parseNotNull
import pt.isel.sample.generalTests.student.Student
import kotlin.test.assertEquals
import kotlin.test.assertNull

object IndentationTestsFunctions {

    fun `Parse empty object with no characters between brackets`(jsonParser: JsonParser) {
        val json = "{}"
        val student = jsonParser.parseNotNull<Student>(json)

        assertEquals(0, student.nr)
        assertNull(student.name)
    }

    fun `Parse empty object with spaces between brackets`(jsonParser: JsonParser) {
        val json = "{          }"
        val student = jsonParser.parseNotNull<Student>(json)

        assertEquals(0, student.nr)
        assertNull(student.name)
    }

    fun `Parse empty object with spaces before opening brackets`(jsonParser: JsonParser) {
        val json = "          {}"
        val student = jsonParser.parseNotNull<Student>(json)

        assertEquals(0, student.nr)
        assertNull(student.name)
    }

    fun `Parse empty object with spaces after closing brackets`(jsonParser: JsonParser) {
        val json = "{}          "
        val student = jsonParser.parseNotNull<Student>(json)

        assertEquals(0, student.nr)
        assertNull(student.name)
    }

    fun `Parse empty array with no characters between brackets`(jsonParser: JsonParser) {
        val json = "[]"

        val persons = jsonParser.parseArrayNotNull<Student>(json)

        assertEquals(0, persons.size)
    }

    fun `Parse empty array with spaces between brackets`(jsonParser: JsonParser) {
        val json = "[          ]"

        val persons = jsonParser.parseArrayNotNull<Student>(json)

        assertEquals(0, persons.size)
    }

    fun `Parse empty array with spaces before opening brackets`(jsonParser: JsonParser) {
        val json = "          []"

        val persons = jsonParser.parseArrayNotNull<Student>(json)

        assertEquals(0, persons.size)
    }

    fun `Parse empty array with spaces after closing brackets`(jsonParser: JsonParser) {
        val json = "[]          "

        val persons = jsonParser.parseArrayNotNull<Student>(json)

        assertEquals(0, persons.size)
    }
}
