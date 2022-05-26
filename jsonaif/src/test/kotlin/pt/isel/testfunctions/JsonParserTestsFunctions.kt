package pt.isel.testfunctions

import pt.isel.jsonParser.JsonParser
import pt.isel.jsonParser.ParseException
import pt.isel.jsonParser.parseNotNull
import pt.isel.sample.generalTests.Person2
import pt.isel.sample.notParseable.Car
import pt.isel.sample.parseObjectWithArray.account.Account
import pt.isel.sample.parseObjectWithArray.account.Transaction
import pt.isel.sample.parseObjectWithArray.competition.Bike2
import pt.isel.sample.parseObjectWithArray.competition.BikeWithOwner2
import pt.isel.sample.parseObjectWithArray.competition.Competition
import pt.isel.sample.parseWithJsonProperty.Employee2
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

object JsonParserTestsFunctions {

    fun `parse object with an array (Account) works`(jsonParser: JsonParser) {
        val json = "{ id:\"andrepascoa\", balance: 9000," +
            " transactions: [{ fromId:\"andrepascoa\", toId:\"andrejesus\", amount: 1000 }," +
            " { fromId:\"andrejesus\", toId:\"nyckollasbrandao\", amount: 1000 }," +
            " { fromId:\"nyckollasbrandao\", toId:\"andrepascoa\", amount: 1000 }]}"

        val account = jsonParser.parseNotNull<Account>(json)
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

    fun `parse object with an array (Competition) works`(jsonParser: JsonParser) {
        val bikesWithOwners = listOf(
            BikeWithOwner2(
                Bike2("0D-45-33", "Yamaha MT07"),
                Person2(0, "André Páscoa")
            ),
            BikeWithOwner2(
                Bike2("66-XX-33", "Yamaha R1"),
                Person2(1, "Nyckollas Brandão")
            ),
            BikeWithOwner2(
                Bike2("33-DD-33", "Casal Boss 1989"),
                Person2(2, "André Jesus")
            )
        )

        val mockCompetition = Competition("Cabo da roca GP", bikesWithOwners, 0)

        val json = "{ name: \"Cabo da roca GP\"," +
            " competitors: [" +
            "{ bike: { plate: \"0D-45-33\", model: \"Yamaha MT07\" }," +
            " owner: { id: 0, name: \"André Páscoa\" }" +
            "}," +
            " { bike: { plate: \"66-XX-33\", model: \"Yamaha R1\" }," +
            " owner: { id: 1, name: \"Nyckollas Brandão\" }" +
            "}," +
            " {bike: { plate: \"33-DD-33\", model: \"Casal Boss 1989\" }," +
            " owner: { id: 2, name: \"André Jesus\" }" +
            "}" +
            "], winner: 0 }"

        val competition = jsonParser.parseNotNull<Competition>(json)
        assertEquals(mockCompetition.name, competition.name)

        assertEquals(mockCompetition.competitors.size, competition.competitors.size)
        for (index in competition.competitors.indices) {
            assertEquals(mockCompetition.competitors[index].owner, competition.competitors[index].owner)
            assertEquals(mockCompetition.competitors[index].bike, competition.competitors[index].bike)
        }

        assertEquals(mockCompetition.winner, competition.winner)
    }

    // JsonProperty annotation (different property name)

    fun `Parse object with other property name (Employee) works`(jsonParser: JsonParser) {
        val json = "{ name: \"Ze Manel\", birth_date: { year: 1999, month: 9, day: 19 }, salary: 9999 }"
        val employee = jsonParser.parseNotNull<Employee2>(json)

        assertEquals("Ze Manel", employee.name)
        assertEquals(19, employee.birth?.day)
        assertEquals(9, employee.birth?.month)
        assertEquals(1999, employee.birth?.year)
        assertEquals(9999, employee.salary)
    }

    fun `Parse object with param that is not a property throws ParseException`(jsonParser: JsonParser) {
        val json = "{ license_plate: \"11-MB-11\", price: 10000 }"

        assertFailsWith<ParseException> {
            jsonParser.parseNotNull<Car>(json)
        }
    }
}
