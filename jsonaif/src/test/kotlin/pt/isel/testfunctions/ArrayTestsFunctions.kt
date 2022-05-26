package pt.isel.testfunctions

import pt.isel.jsonParser.JsonParser
import pt.isel.jsonParser.parseArray
import pt.isel.sample.generalTests.Person
import pt.isel.sample.generalTests.Person2
import kotlin.test.assertEquals

object ArrayTestsFunctions {

    fun `Parse array works`(jsonParser: JsonParser) {
        val json = "[{ name: \"Ze Manel\" }, { name: \"Candida Raimunda\" }, { name: \"Kata Mandala\" }]"

        val persons = jsonParser.parseArray<Person2>(json)

        assertEquals(3, persons.size)
        assertEquals("Ze Manel", persons[0].name)
        assertEquals("Candida Raimunda", persons[1].name)
        assertEquals("Kata Mandala", persons[2].name)
    }

    fun `Parse empty array works`(jsonParser: JsonParser) {
        val json = "[ ]"

        val persons = jsonParser.parseArray<Person>(json)

        assertEquals(0, persons.size)
    }
}
