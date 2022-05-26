package pt.isel.sample.parseObjectWithArray.competition

/**
 * Used to test parsing an object (Competition) with an array (list os BikeWithOwner).
 * All optional parameters.
 * All properties are "var".
 */
data class Competition(
    var name: String = "Portimao Open",
    var competitors: List<BikeWithOwner2> = emptyList(),
    var winner: Int = 1
)
