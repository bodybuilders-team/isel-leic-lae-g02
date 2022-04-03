package pt.isel

import pt.isel.jsonParser.JsonParserReflect
import pt.isel.sample.Account
import pt.isel.sample.Bike
import pt.isel.sample.BikeWithOwner
import pt.isel.sample.Competition
import pt.isel.sample.Employee
import pt.isel.sample.Person
import pt.isel.sample.Transaction
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonParserTest {

    @Test
    fun `parse object with an array (Account) works`() {
        val json = "{ id:\"andrepascoa\", balance: 9000," +
            " transactions: [{ fromId:\"andrepascoa\", toId:\"andrejesus\", amount: 1000 }," +
            " { fromId:\"andrejesus\", toId:\"nyckollasbrandao\", amount: 1000 }," +
            " { fromId:\"nyckollasbrandao\", toId:\"andrepascoa\", amount: 1000 }]}"

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

        val json = "{ name: \"Cabo da roca GP\"," +
            " competitors: [" +
            "{ bike: { plate: \"0D-45-33\", model: \"Yamaha MT07\" }," +
            " owner: { id: 0, name: \"André Páscoa\" }" +
            "}," +
            " { bike: { plate: \"66-XX-33\", model: \"Yamaha R1\" }," +
            " owner: { id: 1, name: \"Nyckollas Brandão\" }" +
            "}," +
            " {bike: { plate: \"33-DD-33\", model: \"Casal Boss 1989\" }," +
            " owner: { id: 2, name: \"André Páscoa\" }" +
            "}" +
            "], winner: 0 }"

        val competition = JsonParserReflect.parse(json, Competition::class) as Competition
        assertEquals(mockCompetition.name, competition.name)

        assertEquals(mockCompetition.competitors.size, competition.competitors.size)
        for (index in competition.competitors.indices) {
            assertEquals(mockCompetition.competitors[index].owner, competition.competitors[index].owner)
            assertEquals(mockCompetition.competitors[index].bike, competition.competitors[index].bike)
        }

        assertEquals(mockCompetition.winner, competition.winner)
    }

    // JsonProperty annotation (different property name)

    @Test
    fun `Parse object with other property name (Employee) works`() {
        val json = "{ name: \"Ze Manel\", birth_date: { year: 1999, month: 9, day: 19 }, salary: 9999 }"
        val employee = JsonParserReflect.parse(json, Employee::class) as Employee

        assertEquals("Ze Manel", employee.name)
        assertEquals(19, employee.birth?.day)
        assertEquals(9, employee.birth?.month)
        assertEquals(1999, employee.birth?.year)
        assertEquals(9999, employee.salary)
    }

    // TODO: 01/04/2022 Test not parseable
}
