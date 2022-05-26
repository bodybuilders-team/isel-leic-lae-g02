package pt.isel.dynamic

import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic
import pt.isel.testfunctions.ArrayTestsFunctions
import kotlin.test.Test

class ArrayTests {

    @Test
    fun `Parse array works`() {
        ArrayTestsFunctions.`Parse array works`(JsonParserDynamic)
    }

    @Test
    fun `Parse empty array works`() {
        ArrayTestsFunctions.`Parse empty array works`(JsonParserDynamic)
    }
}
