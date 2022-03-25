package pt.isel

import pt.isel.json_parser.JsonParserReflect
import pt.isel.json_parser.ParseException
import pt.isel.sample.*
import kotlin.test.*


class JsonParserTest {

	// ----------- Simple Objects -----------

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
	fun `parse simple object only with primitives works`() {
		val json = "{ nr: 1234 }"
		val student = JsonParserReflect.parse(json, Student::class) as Student
		assertEquals(1234, student.nr)
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
		val person = JsonParserReflect.parse(json, Person::class) as Person

		assertEquals(94646, person.id)
		assertEquals("Ze Manel", person.name)
	}

	@Test
	fun `parse empty object works`() {
		val json = "{ }"
		val student = JsonParserReflect.parse(json, Student::class) as Student

		assertEquals(0, student.nr)
		assertNull(student.name)
	}

	// ----------- Primitives -----------

	@Test
	fun `parse string works`() {
		val unparsedStr = "\"hello world\" "
		val parsedStr = JsonParserReflect.parse(unparsedStr, String::class)
		assertEquals("hello world", parsedStr)
	}

	@Test
	fun `parse number works`() {
		val unparsedNr = "123,"
		val nr = JsonParserReflect.parse(unparsedNr, Int::class)
		assertEquals(123, nr)
	}

	@Test
	fun `parse true value works`() {
		val unparsedNr = "true,"
		val parsed = JsonParserReflect.parse(unparsedNr, Boolean::class) as Boolean
		assertTrue(parsed)
	}

	@Test
	fun `parse false value works`() {
		val unparsedNr = "false,"
		val parsed = JsonParserReflect.parse(unparsedNr, Boolean::class) as Boolean
		assertFalse(parsed)
	}

	@Test
	fun `parse null value works`() {
		val unparsedNr = "null,"
		val nr = JsonParserReflect.parse(unparsedNr, Int::class)
		assertEquals(null, nr)
	}

	@Test
	fun `parse value with wrong klass throws`() {
		val unparsedNr = "true,"
		assertFailsWith<ParseException> {
			JsonParserReflect.parse(unparsedNr, Int::class)
		}
	}

	// ----------- Other tests -----------

	@Test
	fun `parse compose object works`() {
		val json =
			"{ id: 94646, name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19}, sibling: { name: \"Kata Badala\"}}"
		val person = JsonParserReflect.parse(json, Person::class) as Person

		assertEquals(94646, person.id)
		assertEquals("Ze Manel", person.name)
		assertEquals(19, person.birth?.day)
		assertEquals(9, person.birth?.month)
		assertEquals(1999, person.birth?.year)
	}

	@Test
	fun `parse array works`() {
		val json = "[{name: \"Ze Manel\"}, {name: \"Candida Raimunda\"}, {name: \"Kata Mandala\"}]"
		val persons = JsonParserReflect.parse(json, Person::class) as List<Person>

		assertEquals(3, persons.size)
		assertEquals("Ze Manel", persons[0].name)
		assertEquals("Candida Raimunda", persons[1].name)
		assertEquals("Kata Mandala", persons[2].name)
	}

	@Test
	fun `parse empty array works`() {
		val json = "[ ]"
		val persons = JsonParserReflect.parse(json, Person::class) as List<Person>

		assertEquals(0, persons.size)
	}

	@Test
	fun `parse object with no optional parameters (Classroom) works`() {
		val json =
			"{ name: \"LAE\", students: [{name: \"André Páscoa\"}, {name: \"André Jesus\"}, {name: \"Nyckollas Brandão\"}]}"
		val classroom = JsonParserReflect.parse(json, Classroom::class) as Classroom

		assertEquals("LAE", classroom.name)
		assertEquals(3, classroom.students.size)
		assertEquals("André Páscoa", classroom.students[0].name)
		assertEquals("André Jesus", classroom.students[1].name)
		assertEquals("Nyckollas Brandão", classroom.students[2].name)
	}

	@Test
	fun `parse object with an array (Account) works`() {
		val json = "{id:\"andrepascoa\", balance: 9000," +
				" transactions: [{fromId:\"andrepascoa\", toId:\"andrejesus\", amount: 1000}," +
				" {fromId:\"andrejesus\", toId:\"nyckollasbrandao\", amount: 1000}," +
				" {fromId:\"nyckollasbrandao\", toId:\"andrepascoa\", amount: 1000}]}"

		val account = JsonParserReflect.parse(json, Account::class) as Account
		val mockTransactions = listOf(
			Transaction("andrepascoa", "andrejesus", 1000.0),
			Transaction("andrejesus", "nyckollasbrandao", 1000.0),
			Transaction("nyckollasbrandao", "andrepascoa", 1000.0)
		)

		assertEquals("andrepascoa", account.id)
		assertEquals(9000.0, account.balance)
		assertEquals(3, account.transactions.size)
		for (index in account.transactions.indices)
			assertEquals(mockTransactions[index], account.transactions[index])
	}


	@Test
	fun `parse object with an array (Competition) works`() {
		val bikesWithOwners = listOf(
			BikeWithOwner(
				Bike("0D-45-33", "Yamaha MT07"),
				Person(0, "André Páscoa")
			),

			BikeWithOwner(
				Bike("66-XX-33", "Yamaha R1"),
				Person(1, "Nyckollas Brandão")
			),
			BikeWithOwner(
				Bike("33-DD-33", "Casal Boss 1989"),
				Person(2, "André Páscoa")
			)
		)

		val mockCompetition = Competition("Cabo da roca GP", bikesWithOwners, 0)

		val json = "{name: \"Cabo da roca GP\"," +
				" competitors: [" +
				"{bike: {plate: \"0D-45-33\", model: \"Yamaha MT07\"}," +
				" owner: {id: 0, name: \"André Páscoa\"}" +
				"}," +
				" { bike: {plate: \"66-XX-33\", model: \"Yamaha R1\"}," +
				" owner: {id: 1, name: \"Nyckollas Brandão\"}" +
				"}," +
				" {bike: {plate: \"33-DD-33\", model: \"Casal Boss 1989\"}," +
				" owner: {id: 2, name: \"André Páscoa\"}" +
				"}" +
				"], winner: 0}"

		val competition = JsonParserReflect.parse(json, Competition::class) as Competition
		assertEquals(mockCompetition.name, competition.name)

		assertEquals(mockCompetition.competitors.size, competition.competitors.size)
		for (index in competition.competitors.indices) {
			assertEquals(mockCompetition.competitors[index].owner, competition.competitors[index].owner)
			assertEquals(mockCompetition.competitors[index].bike, competition.competitors[index].bike)
		}

		assertEquals(mockCompetition.winner, competition.winner)
	}

	@Test
	fun `parse object with other property mame (Employee) works`() {
		val json = "{ name: \"Ze Manel\", birth_date: { year: 1999, month: 9, day: 19}, salary: 9999 }"
		val employee = JsonParserReflect.parse(json, Employee::class) as Employee

		assertEquals("Ze Manel", employee.name)
		assertEquals(19, employee.birth?.day)
		assertEquals(9, employee.birth?.month)
		assertEquals(1999, employee.birth?.year)
		assertEquals(9999, employee.salary)
	}

	@Test
	fun `parse object with converter (Cake) works`() {
		val json = "{ expDate: \"1998-11-17\" }"
		val cake = JsonParserReflect.parse(json, Cake::class) as Cake

		assertEquals(cake.expDate, Date(17, 11, 1998))
		assertEquals(cake.mainFlavor, "Cocoa")
	}

}
