package pt.isel.sample.parseObjectWithArray.competition

import pt.isel.sample.generalTests.Person2

/**
 * Used in Competition.
 * All optional parameters.
 * All properties are "var".
 */
data class BikeWithOwner2(
    var bike: Bike2 = Bike2(),
    var owner: Person2 = Person2()
)
