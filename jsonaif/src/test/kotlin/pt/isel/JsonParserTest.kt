package pt.isel

import pt.isel.sample.*
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

	@Test
	fun `Parse Classroom`() {
		val json =
			"{ name: \"LAE\", students: [{name: \"André Páscoa\"}, {name: \"André Jesus\"}, {name: \"Nyckollas Brandão\"}]}"
		val c = JsonParserReflect.parse(json, Classroom::class) as Classroom
		assertEquals("LAE", c.name)
		assertEquals(3, c.students.size)
		assertEquals("André Páscoa", c.students[0].name)
		assertEquals("André Jesus", c.students[1].name)
		assertEquals("Nyckollas Brandão", c.students[2].name)
	}

	@Test
	fun `Parse Account`() {
		val json =
			"{id:\"andrepascoa\", balance: 9000," +
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
	fun `Parse Competition`() {
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
	fun `Parse Employee`() {
		val json =
			"{ name: \"Ze Manel\", birth_date: { year: 1999, month: 9, day: 19}, salary: 9999}"
		val p = JsonParserReflect.parse(json, Employee::class) as Employee

		assertEquals("Ze Manel", p.name)
		assertEquals(19, p.birth?.day)
		assertEquals(9, p.birth?.month)
		assertEquals(1999, p.birth?.year)
		assertEquals(9999, p.salary)
	}

	@Test
	fun `Parse Cake`(){
		val json =
			"{ expDate: \"1998-11-17\" }"

		val c = JsonParserReflect.parse(json, Cake::class) as Cake

		assertEquals(c.expDate, Date(17, 11, 1998))
		assertEquals(c.mainFlavor, "Cocoa")
	}

}
