package pt.isel.dynamicAndUnsafe

import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamicAndUnsafe
import pt.isel.sample.generalTests.Person
import kotlin.test.Test
import kotlin.test.assertEquals

class ArrayTests {
    @Test
    fun `Parse array works`() {
        val json = "[{ name: \"Ze Manel\" }, { name: \"Candida Raimunda\" }, { name: \"Kata Mandala\" }]"

        @Suppress("UNCHECKED_CAST")
        val persons = JsonParserDynamicAndUnsafe.parse(json, Person::class) as List<Person>

        assertEquals(3, persons.size)
        assertEquals("Ze Manel", persons[0].name)
        assertEquals("Candida Raimunda", persons[1].name)
        assertEquals("Kata Mandala", persons[2].name)
    }

    @Test
    fun `Parse empty array works`() {
        val json = "[ ]"

        @Suppress("UNCHECKED_CAST")
        val persons = JsonParserDynamicAndUnsafe.parse(json, Person::class) as List<Person>

        assertEquals(0, persons.size)
    }
}
