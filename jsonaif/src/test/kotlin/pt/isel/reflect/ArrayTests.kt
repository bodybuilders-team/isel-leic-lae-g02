package pt.isel.reflect

import pt.isel.jsonParser.parseArray
import pt.isel.jsonParser.parsers.reflect.JsonParserReflect
import pt.isel.sample.generalTests.Person
import kotlin.test.Test
import kotlin.test.assertEquals

class ArrayTests {

    // parse

    @Test
    fun `Parse array with parse works`() {
        val json = "[{ name: \"Ze Manel\" }, { name: \"Candida Raimunda\" }, { name: \"Kata Mandala\" }]"

        @Suppress("UNCHECKED_CAST")
        val persons = JsonParserReflect.parse(json, Person::class) as List<Person>

        assertEquals(3, persons.size)
        assertEquals("Ze Manel", persons[0].name)
        assertEquals("Candida Raimunda", persons[1].name)
        assertEquals("Kata Mandala", persons[2].name)
    }

    @Test
    fun `Parse empty array with parse works`() {
        val json = "[ ]"

        @Suppress("UNCHECKED_CAST")
        val persons = JsonParserReflect.parse(json, Person::class) as List<Person>

        assertEquals(0, persons.size)
    }

    // parseArray

    @Test
    fun `Parse array with parseArray works`() {
        val json = "[{ name: \"Ze Manel\" }, { name: \"Candida Raimunda\" }, { name: \"Kata Mandala\" }]"

        val persons = JsonParserReflect.parseArray<Person>(json)

        assertEquals(3, persons.size)
        assertEquals("Ze Manel", persons[0].name)
        assertEquals("Candida Raimunda", persons[1].name)
        assertEquals("Kata Mandala", persons[2].name)
    }

    @Test
    fun `Parse empty array with parseArray works`() {
        val json = "[ ]"

        val persons = JsonParserReflect.parseArray<Person>(json)

        assertEquals(0, persons.size)
    }
}
