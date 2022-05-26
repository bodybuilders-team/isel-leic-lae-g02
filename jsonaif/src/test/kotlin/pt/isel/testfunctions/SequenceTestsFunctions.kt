package pt.isel.testfunctions

import pt.isel.jsonParser.JsonParser
import pt.isel.jsonParser.parseSequenceNotNull
import pt.isel.sample.generalTests.Person2
import kotlin.test.assertEquals

object SequenceTestsFunctions {

    fun `Parse valid sequence`(jsonParser: JsonParser) {
        val json = "[{ name: \"Ze Manel\" }, { name: \"Candida Raimunda\" }, { name: \"Kata Mandala\" }]"

        val persons = jsonParser.parseSequenceNotNull<Person2>(json).toList()

        assertEquals(3, persons.size)
        assertEquals("Ze Manel", persons[0].name)
        assertEquals("Candida Raimunda", persons[1].name)
        assertEquals("Kata Mandala", persons[2].name)
    }
}
