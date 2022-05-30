package pt.isel

import pt.isel.jsonParser.JsonParser
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parse
import pt.isel.jsonParser.parseNotNull
import pt.isel.sample.generalTests.Person
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ParserGenericsTests {

    object ArrayMockParser : JsonParser {
        override fun <T : Any> parse(source: String, klass: KClass<T>): T? {
            return null
        }

        override fun <T : Any> parseSequence(json: String, klass: KClass<T>): Sequence<T?> {
            return sequenceOf()
        }

        override fun <T : Any> parseArray(source: String, klass: KClass<T>): List<T?>? {
            return listOf(
                Person(0, "Joe", null, null),
                Person(1, "John", null, null),
                Person(2, "Jack", null, null)
            ) as List<T?>?
        }
    }

    object MockParser : JsonParser {
        override fun <T : Any> parse(source: String, klass: KClass<T>): T? {
            if (source == "{null}")
                return null

            return Person(0, "Joe", null, null) as T
        }

        override fun <T : Any> parseSequence(json: String, klass: KClass<T>): Sequence<T?> {
            return sequenceOf()
        }

        override fun <T : Any> parseArray(source: String, klass: KClass<T>): List<T?>? {
            return arrayListOf()
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
}
