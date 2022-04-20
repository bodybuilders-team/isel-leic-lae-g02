package pt.isel.sample.parseObjectWithArray.competition

import pt.isel.sample.generalTests.Person

/**
 * Used in Competition.
 * No optional parameters.
 */
data class BikeWithOwner(
    val bike: Bike,
    val owner: Person
)
