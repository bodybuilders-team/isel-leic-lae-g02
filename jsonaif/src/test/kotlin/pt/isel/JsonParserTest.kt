package pt.isel

import pt.isel.sample.Person
import pt.isel.sample.Student
import kotlin.test.*

class JsonParserTest {

	@Test
	fun `parse simple object via properties works`() {
		val json = "{ name: \"Ze Manel\", nr: 7353}"
		val student = JsonParserReflect.parse(json, Student::class) as Student
		assertEquals("Ze Manel", student.name)
		assertEquals(7353, student.nr)
	}

	@Test
	fun `parse simple object without primitives works`() {
		val json = "{ name: \"Ze Manel\"}"
		val student = JsonParserReflect.parse(json, Student::class) as Student
		assertEquals("Ze Manel", student.name)
		assertEquals(0, student.nr)
	}

	@Test
	fun `parse simple object with null property works`() {
		val json = "{ name: null }"
		val student = JsonParserReflect.parse(json, Student::class) as Student
		assertNull(student.name)
	}

	@Test
	fun `parse simple object with non existent property throws`() {
		val json = "{ age: \"Ze Manel\" }"
		assertFailsWith<ParseException> {
			JsonParserReflect.parse(json, Student::class) as Student
		}
	}

	@Test
	fun `parse simple object with wrong property type throws`() {
		val json = "{ name: 123 }"
		assertFailsWith<ParseException> {
			JsonParserReflect.parse(json, Student::class) as Student
		}
	}

	@Test
	fun `parse simple object via constructor works`() {
		val json = "{ id: 94646, name: \"Ze Manel\"}"
		val p = JsonParserReflect.parse(json, Person::class) as Person
		assertEquals(94646, p.id)
		assertEquals("Ze Manel", p.name)
	}

	@Test
	fun `parse compose object works`() {
		val json =
			"{ id: 94646, name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19}, sibling: { name: \"Kata Badala\"}}"
		val p = JsonParserReflect.parse(json, Person::class) as Person
		assertEquals(94646, p.id)
		assertEquals("Ze Manel", p.name)
		assertEquals(19, p.birth?.day)
		assertEquals(9, p.birth?.month)
		assertEquals(1999, p.birth?.year)
	}

	@Test
	fun `parse array works`() {
		val json = "[{name: \"Ze Manel\"}, {name: \"Candida Raimunda\"}, {name: \"Kata Mandala\"}]";
		val ps = JsonParserReflect.parse(json, Person::class) as List<Person>
		assertEquals(3, ps.size)
		assertEquals("Ze Manel", ps[0].name)
		assertEquals("Candida Raimunda", ps[1].name)
		assertEquals("Kata Mandala", ps[2].name)
	}
}
