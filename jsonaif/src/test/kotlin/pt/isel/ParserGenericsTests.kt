package pt.isel

import pt.isel.jsonParser.JsonParser
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parse
import pt.isel.jsonParser.parseArray
import pt.isel.jsonParser.parseNotNull
import pt.isel.sample.generalTests.Person
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ParserGenericsTests {

    object ArrayMockParser : JsonParser {
        override fun parse(source: String, klass: KClass<*>): Any {
            return listOf(Person(0, "Joe", null, null), Person(1, "John", null, null), Person(2, "Jack", null, null))
        }

        override fun parseSequence(json: String, klass: KClass<*>): Sequence<Any?> {
            return sequenceOf()
        }
    }

    object MockParser : JsonParser {
        override fun parse(source: String, klass: KClass<*>): Any? {
            if (source == "{null}")
                return null

            return Person(0, "Joe", null, null)
        }

        override fun parseSequence(json: String, klass: KClass<*>): Sequence<Any?> {
            return sequenceOf()
        }
    }

    @Test
    fun `parse with generics yields same result as calling parse and casting`() {
        val json = "{}"

        @Suppress("UNCHECKED_CAST")
        val normalValue = MockParser.parse(json, Person::class) as Person?

        val genericsValue = MockParser.parse<Person>(json)

        assertEquals(normalValue, genericsValue)
    }

    @Test
    fun `parseNotNull yields same result as calling parse and casting`() {
        val json = "{}"

        val normalValue = MockParser.parse(json, Person::class) as Person

        val genericsValue = MockParser.parseNotNull<Person>(json)

        assertEquals(normalValue, genericsValue)
    }

    @Test
    fun `parseNotNull throws ParseException if the value is null`() {
        val json = "{null}"

        assertFailsWith<ParseException> {
            MockParser.parseNotNull<Person>(json)
        }
    }

    @Test
    fun `parseArray yields same result as calling parse and casting to List`() {
        val json = "{}"

        @Suppress("UNCHECKED_CAST")
        val normalValue = ArrayMockParser.parse(json, Person::class) as List<Person>

        val genericsValue = ArrayMockParser.parseArray<Person>(json)

        assertEquals(normalValue, genericsValue)
    }
}
