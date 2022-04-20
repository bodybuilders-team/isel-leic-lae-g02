package pt.isel.dynamic

import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic
import pt.isel.sample.generalTests.Person
import kotlin.test.Test
import kotlin.test.assertEquals

class ArrayTests {

    @Test
    fun `Parse empty array works`() {
        val json = "[ ]"

        @Suppress("UNCHECKED_CAST")
        val persons = JsonParserDynamic.parse(json, Person::class) as List<Person>

        assertEquals(0, persons.size)
    }
}
