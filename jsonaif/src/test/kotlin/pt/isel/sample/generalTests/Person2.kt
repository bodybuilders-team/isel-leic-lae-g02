package pt.isel.sample.generalTests

/**
 * Created to general test purposes.
 * All optional parameters.
 * All properties are "var".
 */
data class Person2(
    var id: Int = 0,
    var name: String = "Bob",
    var birth: Date? = null,
    var sibling: Person? = null
)
