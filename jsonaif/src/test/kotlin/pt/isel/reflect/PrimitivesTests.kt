package pt.isel.reflect

import pt.isel.jsonParser.parsers.reflect.JsonParserReflect
import pt.isel.testfunctions.PrimitivesTestsFunctions
import kotlin.test.Test

/**
 * In these tests, primitives in the unparsed strings are followed by a comma because JsonTokens.kt forces
 * a primitive to end in a comma, object end, or array end.
 */
class PrimitivesTests {

    @Test
    fun `Parse string works`() {
        PrimitivesTestsFunctions.`Parse string works`(JsonParserReflect)
    }

    @Test
    fun `Parse integer number works`() {
        PrimitivesTestsFunctions.`Parse integer number works`(JsonParserReflect)
    }

    @Test
    fun `Parse long number works`() {
        PrimitivesTestsFunctions.`Parse long number works`(JsonParserReflect)
    }

    @Test
    fun `Parse short number works`() {
        PrimitivesTestsFunctions.`Parse short number works`(JsonParserReflect)
    }

    @Test
    fun `Parse byte number works`() {
        PrimitivesTestsFunctions.`Parse byte number works`(JsonParserReflect)
    }

    @Test
    fun `Parse floating-point number with 'f' character specified works`() {
        PrimitivesTestsFunctions.`Parse floating-point number with 'f' character specified works`(JsonParserReflect)
    }

    @Test
    fun `Parse floating-point number works`() {
        PrimitivesTestsFunctions.`Parse floating-point number works`(JsonParserReflect)
    }

    @Test
    fun `Parse double precision floating-point number works`() {
        PrimitivesTestsFunctions.`Parse double precision floating-point number works`(JsonParserReflect)
    }

    @Test
    fun `Parse floating-point number requiring double precision as Float reduces precision`() {
        PrimitivesTestsFunctions.`Parse floating-point number requiring double precision as Float reduces precision`(
            JsonParserReflect
        )
    }

    @Test
    fun `Parse true boolean value works`() {
        PrimitivesTestsFunctions.`Parse true boolean value works`(JsonParserReflect)
    }

    @Test
    fun `Parse false boolean value works`() {
        PrimitivesTestsFunctions.`Parse false boolean value works`(JsonParserReflect)
    }

    @Test
    fun `Parse null value works`() {
        PrimitivesTestsFunctions.`Parse null value works`(JsonParserReflect)
    }

    @Test
    fun `Parse value with wrong klass throws`() {
        PrimitivesTestsFunctions.`Parse value with wrong klass throws`(JsonParserReflect)
    }

    @Test
    fun `Parse non-boolean value as boolean throws`() {
        PrimitivesTestsFunctions.`Parse non-boolean value as boolean throws`(JsonParserReflect)
    }

    @Test
    fun `Parse unknown value (therefore with wrong klass) throws`() {
        PrimitivesTestsFunctions.`Parse unknown value (therefore with wrong klass) throws`(JsonParserReflect)
    }
}
