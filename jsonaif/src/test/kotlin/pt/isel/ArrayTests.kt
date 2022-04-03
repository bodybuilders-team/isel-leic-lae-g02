package pt.isel

import pt.isel.jsonParser.JsonParserReflect
import pt.isel.sample.Person
import kotlin.test.Test
import kotlin.test.assertEquals

class ArrayTests {
    @Test
    fun `Parse array works`() {
        val json = "[{ name: \"Ze Manel\" }, { name: \"Candida Raimunda\" }, { name: \"Kata Mandala\" }]"

        @Suppress("UNCHECKED_CAST")
        val persons = JsonParserReflect.parse(json, Person::class) as List<Person>

        assertEquals(3, persons.size)
        assertEquals("Ze Manel", persons[0].name)
        assertEquals("Candida Raimunda", persons[1].name)
        assertEquals("Kata Mandala", persons[2].name)
    }

    @Test
    fun `Parse empty array works`() {
        val json = "[ ]"

        @Suppress("UNCHECKED_CAST")
        val persons = JsonParserReflect.parse(json, Person::class) as List<Person>

        assertEquals(0, persons.size)
    }
}
