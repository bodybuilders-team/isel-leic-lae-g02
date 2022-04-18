package pt.isel.dynamic

import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * In these tests, primitives in the unparsed strings  are followed by a comma because JsonTokens.kt forces
 * a primitive to end in a comma, object end, or array end.
 */
class PrimitivesTests {
    @Test
    fun `Parse string works`() {
        val unparsedStr = "\"hello world\","
        val parsedStr = JsonParserDynamic.parse(unparsedStr, String::class)
        assertEquals("hello world", parsedStr)
    }

    @Test
    fun `Parse integer number works`() {
        val unparsedNr = "123,"
        val nr = JsonParserDynamic.parse(unparsedNr, Int::class) as Int
        assertEquals(123, nr)
    }

    @Test
    fun `Parse long number works`() {
        val unparsedNr = "1235379344321,"
        val nr = JsonParserDynamic.parse(unparsedNr, Long::class) as Long
        assertEquals(1235379344321, nr)
    }

    @Test
    fun `Parse short number works`() {
        val unparsedNr = "123,"
        val nr = JsonParserDynamic.parse(unparsedNr, Short::class) as Short
        assertEquals(123, nr)
    }

    @Test
    fun `Parse byte number works`() {
        val unparsedNr = "90,"
        val nr = JsonParserDynamic.parse(unparsedNr, Byte::class) as Byte
        assertEquals(90, nr)
    }

    @Test
    fun `Parse floating-point number with 'f' character specified works`() {
        val unparsedNr = "123.2f,"
        val nr = JsonParserDynamic.parse(unparsedNr, Float::class) as Float
        assertEquals(123.2f, nr)
    }

    @Test
    fun `Parse floating-point number works`() {
        val unparsedNr = "123.2,"
        val nr = JsonParserDynamic.parse(unparsedNr, Float::class) as Float
        assertEquals(123.2f, nr)
    }

    @Test
    fun `Parse double precision floating-point number works`() {
        val unparsedNr = "123.1248989235,"
        val nr = JsonParserDynamic.parse(unparsedNr, Double::class) as Double
        assertEquals(123.1248989235, nr)
    }

    @Test
    fun `Parse floating-point number requiring double precision as Float reduces precision`() {
        val unparsedNr = "123.1248989235,"
        val nr = JsonParserDynamic.parse(unparsedNr, Float::class) as Float
        assertEquals(123.1249f, nr)
    }

    @Test
    fun `Parse true boolean value works`() {
        val unparsedNr = "true,"
        val parsed = JsonParserDynamic.parse(unparsedNr, Boolean::class) as Boolean
        assertTrue(parsed)
    }

    @Test
    fun `Parse false boolean value works`() {
        val unparsedNr = "false,"
        val parsed = JsonParserDynamic.parse(unparsedNr, Boolean::class) as Boolean
        assertFalse(parsed)
    }

    @Test
    fun `Parse null value works`() {
        val unparsedNr = "null,"
        val nr = JsonParserDynamic.parse(unparsedNr, Int::class) as Int?
        assertEquals(null, nr)
    }

    @Test
    fun `Parse value with wrong klass throws`() {
        val unparsedNr = "true,"
        assertFailsWith<ParseException> {
            JsonParserDynamic.parse(unparsedNr, Int::class)
        }
    }

    @Test
    fun `Parse non-boolean value as boolean throws`() {
        val unparsedNr = "1243,"
        assertFailsWith<ParseException> {
            JsonParserDynamic.parse(unparsedNr, Boolean::class)
        }
    }

    @Test
    fun `Parse unknown value (therefore with wrong klass) throws`() {
        val unparsedValue = "Hello,"
        assertFailsWith<ParseException> {
            JsonParserDynamic.parse(unparsedValue, String::class)
        }
    }
}
