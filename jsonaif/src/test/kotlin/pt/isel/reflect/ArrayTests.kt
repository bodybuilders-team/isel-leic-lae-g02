package pt.isel.reflect

import pt.isel.jsonParser.parsers.reflect.JsonParserReflect
import pt.isel.testfunctions.ArrayTestsFunctions
import kotlin.test.Test

class ArrayTests {

    @Test
    fun `Parse array works`() {
        ArrayTestsFunctions.`Parse array works`(JsonParserReflect)
    }

    @Test
    fun `Parse empty array works`() {
        ArrayTestsFunctions.`Parse empty array works`(JsonParserReflect)
    }
}
