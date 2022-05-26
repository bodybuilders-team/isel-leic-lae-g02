package pt.isel.dynamic

import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic
import pt.isel.testfunctions.JsonParserTestsFunctions
import kotlin.test.Test

class JsonParserTest {

    @Test
    fun `parse object with an array (Account) works`() {
        JsonParserTestsFunctions.`parse object with an array (Account) works`(JsonParserDynamic)
    }

    @Test
    fun `parse object with an array (Competition) works`() {
        JsonParserTestsFunctions.`parse object with an array (Competition) works`(JsonParserDynamic)
    }

    // JsonProperty annotation (different property name)

    @Test
    fun `Parse object with other property name (Employee) works`() {
        JsonParserTestsFunctions.`Parse object with other property name (Employee) works`(JsonParserDynamic)
    }

    @Test
    fun `Parse object with param that is not a property throws ParseException`() {
        JsonParserTestsFunctions.`Parse object with param that is not a property throws ParseException`(
            JsonParserDynamic
        )
    }
}
