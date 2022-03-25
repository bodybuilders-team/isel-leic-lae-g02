/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package pt.isel

import pt.isel.json_parser.JsonParserReflect
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonParserObjects {

    @Test fun parsePerson() {
        val json = "{ name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19}}"
        val student = parsePerson(json, JsonParserReflect)
        assertEquals("Ze Manel", student.name)
        assertEquals(19, student.birth?.day)
        assertEquals(9, student.birth?.month)
        assertEquals(1999, student.birth?.year)
    }

    @Test fun parseDate() {
        val json = "{ year: 1999, month: 9, day: 19}"
        val dt = parseDate(json, JsonParserReflect)
        assertEquals(19, dt.day)
        assertEquals(9, dt.month)
        assertEquals(1999, dt.year)
    }
}
