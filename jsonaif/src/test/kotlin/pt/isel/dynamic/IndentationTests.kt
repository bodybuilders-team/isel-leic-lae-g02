package pt.isel.dynamic

import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic
import pt.isel.sample.Person
import pt.isel.sample.Student
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class IndentationTests {

    @Test
    fun `Parse empty object with no characters between brackets`() {
        val json = "{}"
        val student = JsonParserDynamic.parse(json, Student::class) as Student

        assertEquals(0, student.nr)
        assertNull(student.name)
    }

    @Test
    fun `Parse empty object with spaces between brackets`() {
        val json = "{          }"
        val student = JsonParserDynamic.parse(json, Student::class) as Student

        assertEquals(0, student.nr)
        assertNull(student.name)
    }

    @Test
    fun `Parse empty object with spaces before opening brackets`() {
        val json = "          {}"
        val student = JsonParserDynamic.parse(json, Student::class) as Student

        assertEquals(0, student.nr)
        assertNull(student.name)
    }

    @Test
    fun `Parse empty object with spaces after closing brackets`() {
        val json = "{}          "
        val student = JsonParserDynamic.parse(json, Student::class) as Student

        assertEquals(0, student.nr)
        assertNull(student.name)
    }

    @Test
    fun `Parse empty array with no characters between brackets`() {
        val json = "[]"

        @Suppress("UNCHECKED_CAST")
        val persons = JsonParserDynamic.parse(json, Person::class) as List<Person>

        assertEquals(0, persons.size)
    }

    @Test
    fun `Parse empty array with spaces between brackets`() {
        val json = "[          ]"

        @Suppress("UNCHECKED_CAST")
        val persons = JsonParserDynamic.parse(json, Person::class) as List<Person>

        assertEquals(0, persons.size)
    }

    @Test
    fun `Parse empty array with spaces before opening brackets`() {
        val json = "          []"

        @Suppress("UNCHECKED_CAST")
        val persons = JsonParserDynamic.parse(json, Person::class) as List<Person>

        assertEquals(0, persons.size)
    }

    @Test
    fun `Parse empty array with spaces after closing brackets`() {
        val json = "[]          "

        @Suppress("UNCHECKED_CAST")
        val persons = JsonParserDynamic.parse(json, Person::class) as List<Person>

        assertEquals(0, persons.size)
    }
}
