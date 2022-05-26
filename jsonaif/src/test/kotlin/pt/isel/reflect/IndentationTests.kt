package pt.isel.reflect

import pt.isel.jsonParser.parsers.reflect.JsonParserReflect
import pt.isel.testfunctions.IndentationTestsFunctions
import kotlin.test.Test

class IndentationTests {

    @Test
    fun `Parse empty object with no characters between brackets`() {
        IndentationTestsFunctions.`Parse empty object with no characters between brackets`(JsonParserReflect)
    }

    @Test
    fun `Parse empty object with spaces between brackets`() {
        IndentationTestsFunctions.`Parse empty object with spaces between brackets`(JsonParserReflect)
    }

    @Test
    fun `Parse empty object with spaces before opening brackets`() {
        IndentationTestsFunctions.`Parse empty object with spaces before opening brackets`(JsonParserReflect)
    }

    @Test
    fun `Parse empty object with spaces after closing brackets`() {
        IndentationTestsFunctions.`Parse empty object with spaces after closing brackets`(JsonParserReflect)
    }

    @Test
    fun `Parse empty array with no characters between brackets`() {
        IndentationTestsFunctions.`Parse empty array with no characters between brackets`(JsonParserReflect)
    }

    @Test
    fun `Parse empty array with spaces between brackets`() {
        IndentationTestsFunctions.`Parse empty array with spaces between brackets`(JsonParserReflect)
    }

    @Test
    fun `Parse empty array with spaces before opening brackets`() {
        IndentationTestsFunctions.`Parse empty array with spaces before opening brackets`(JsonParserReflect)
    }

    @Test
    fun `Parse empty array with spaces after closing brackets`() {
        IndentationTestsFunctions.`Parse empty array with spaces after closing brackets`(JsonParserReflect)
    }
}
