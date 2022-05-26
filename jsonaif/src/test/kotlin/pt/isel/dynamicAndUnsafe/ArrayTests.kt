package pt.isel.dynamicAndUnsafe

import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamicAndUnsafe
import pt.isel.testfunctions.ArrayTestsFunctions
import kotlin.test.Test

class ArrayTests {

    @Test
    fun `Parse array works`() {
        ArrayTestsFunctions.`Parse array works`(JsonParserDynamicAndUnsafe)
    }

    @Test
    fun `Parse empty array works`() {
        ArrayTestsFunctions.`Parse empty array works`(JsonParserDynamicAndUnsafe)
    }
}
