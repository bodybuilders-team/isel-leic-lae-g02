package pt.isel.sample.parseObjectWithArray.competition

/**
 * Used to test parsing an object (Competition) with an array (list os BikeWithOwner).
 * No optional parameters.
 */
data class Competition(
    val name: String,
    val competitors: List<BikeWithOwner>,
    val winner: Int
)
