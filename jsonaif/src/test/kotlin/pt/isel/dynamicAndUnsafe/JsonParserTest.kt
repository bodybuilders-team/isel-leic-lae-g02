package pt.isel.dynamicAndUnsafe

import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamicAndUnsafe
import pt.isel.testfunctions.JsonParserTestsFunctions
import kotlin.test.Test

class JsonParserTest {

    @Test
    fun `parse object with an array (Account) works`() {
        JsonParserTestsFunctions.`parse object with an array (Account) works`(JsonParserDynamicAndUnsafe)
    }

    @Test
    fun `parse object with an array (Competition) works`() {
        JsonParserTestsFunctions.`parse object with an array (Competition) works`(JsonParserDynamicAndUnsafe)
    }

    // JsonProperty annotation (different property name)

    @Test
    fun `Parse object with other property name (Employee) works`() {
        JsonParserTestsFunctions.`Parse object with other property name (Employee) works`(JsonParserDynamicAndUnsafe)
    }

    @Test
    fun `Parse object with param that is not a property throws ParseException`() {
        JsonParserTestsFunctions.`Parse object with param that is not a property throws ParseException`(
            JsonParserDynamicAndUnsafe
        )
    }
}
