package pt.isel.reflect

import org.junit.Test
import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonConvert.JsonConverter
import pt.isel.jsonParser.parseSequence
import pt.isel.jsonParser.parsers.reflect.JsonParserReflect
import pt.isel.sample.generalTests.Date
import pt.isel.testfunctions.SequenceTestsFunctions
import kotlin.test.assertEquals

class SequenceTests {

    @Test
    fun `Parse valid sequence`() {
        SequenceTestsFunctions.`Parse valid sequence`(JsonParserReflect)
    }

    /**
     * Used to test parsing with JsonConvert annotation.
     * All parameters are optional.
     * All properties are "var".
     */
    data class SequenceCake(
        @JsonConvert(SequenceJsonToDate::class) var expDate: Date = Date(1, 1, 2000),
        var mainFlavor: String = "Cocoa"
    )

    object SequenceJsonToDate : JsonConverter<String, Date> {
        var cakeDateCounter = 0

        override fun convert(from: String): Date {
            val (year, month, day) = from.split("-").map { it.toInt() }
            println(from)
            cakeDateCounter++

            return Date(day, month, year)
        }
    }

    @Test
    fun `Parsing a sequence calculates lazily`() {
        val json =
            "[{ expDate: \"1998-11-17\",mainFlavor:\"Vanilla\" },{ expDate: \"1998-09-17\",mainFlavor:\"Vanilla\" },{ expDate: \"1998-12-17\",mainFlavor:\"Vanilla\" }]"
        val cakeSequenceIterator = JsonParserReflect.parseSequence<SequenceCake>(json).iterator()

        assertEquals(0, SequenceJsonToDate.cakeDateCounter)

        assertEquals(SequenceCake(Date(17, 11, 1998), "Vanilla"), cakeSequenceIterator.next())
        assertEquals(1, SequenceJsonToDate.cakeDateCounter)

        assertEquals(SequenceCake(Date(17, 9, 1998), "Vanilla"), cakeSequenceIterator.next())
        assertEquals(SequenceCake(Date(17, 12, 1998), "Vanilla"), cakeSequenceIterator.next())

        assertEquals(3, SequenceJsonToDate.cakeDateCounter)
    }
}
