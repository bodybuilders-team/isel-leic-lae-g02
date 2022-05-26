package pt.isel.testfunctions

import pt.isel.jsonParser.JsonParser
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parse
import pt.isel.jsonParser.parseNotNull
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

object PrimitivesTestsFunctions {

    fun `Parse string works`(jsonParser: JsonParser) {
        val unparsedStr = "\"hello world\","
        val parsedStr = jsonParser.parseNotNull<String>(unparsedStr)
        assertEquals("hello world", parsedStr)
    }

    fun `Parse integer number works`(jsonParser: JsonParser) {
        val unparsedNr = "123,"
        val nr = jsonParser.parseNotNull<Int>(unparsedNr)
        assertEquals(123, nr)
    }

    fun `Parse long number works`(jsonParser: JsonParser) {
        val unparsedNr = "1235379344321,"
        val nr = jsonParser.parseNotNull<Long>(unparsedNr)
        assertEquals(1235379344321, nr)
    }

    fun `Parse short number works`(jsonParser: JsonParser) {
        val unparsedNr = "123,"
        val nr = jsonParser.parseNotNull<Short>(unparsedNr)
        assertEquals(123, nr)
    }

    fun `Parse byte number works`(jsonParser: JsonParser) {
        val unparsedNr = "90,"
        val nr = jsonParser.parseNotNull<Byte>(unparsedNr)
        assertEquals(90, nr)
    }

    fun `Parse floating-point number with 'f' character specified works`(jsonParser: JsonParser) {
        val unparsedNr = "123.2f,"
        val nr = jsonParser.parseNotNull<Float>(unparsedNr)
        assertEquals(123.2f, nr)
    }

    fun `Parse floating-point number works`(jsonParser: JsonParser) {
        val unparsedNr = "123.2,"
        val nr = jsonParser.parseNotNull<Float>(unparsedNr)
        assertEquals(123.2f, nr)
    }

    fun `Parse double precision floating-point number works`(jsonParser: JsonParser) {
        val unparsedNr = "123.1248989235,"
        val nr = jsonParser.parseNotNull<Double>(unparsedNr)
        assertEquals(123.1248989235, nr)
    }

    fun `Parse floating-point number requiring double precision as Float reduces precision`(jsonParser: JsonParser) {
        val unparsedNr = "123.1248989235,"
        val nr = jsonParser.parseNotNull<Float>(unparsedNr)
        assertEquals(123.1249f, nr)
    }

    fun `Parse true boolean value works`(jsonParser: JsonParser) {
        val unparsedValue = "true,"
        val parsed = jsonParser.parseNotNull<Boolean>(unparsedValue)
        assertTrue(parsed)
    }

    fun `Parse false boolean value works`(jsonParser: JsonParser) {
        val unparsedValue = "false,"
        val parsed = jsonParser.parseNotNull<Boolean>(unparsedValue)
        assertFalse(parsed)
    }

    fun `Parse null value works`(jsonParser: JsonParser) {
        val unparsedNr = "null,"
        val nr = jsonParser.parse<Int>(unparsedNr)
        assertEquals(null, nr)
    }

    fun `Parse value with wrong klass throws`(jsonParser: JsonParser) {
        val unparsedNr = "true,"
        assertFailsWith<ParseException> {
            jsonParser.parseNotNull<Int>(unparsedNr)
        }
    }

    fun `Parse non-boolean value as boolean throws`(jsonParser: JsonParser) {
        val unparsedNr = "1243,"
        assertFailsWith<ParseException> {
            jsonParser.parseNotNull<Boolean>(unparsedNr)
        }
    }

    fun `Parse unknown value (therefore with wrong klass) throws`(jsonParser: JsonParser) {
        val unparsedValue = "Hello,"
        assertFailsWith<ParseException> {
            jsonParser.parseNotNull<String>(unparsedValue)
        }
    }
}
