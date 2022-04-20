package pt.isel.sample.generalTests

/**
 * Created to general test purposes.
 * All properties are "val".
 * One non-optional parameter ([name]).
 */
data class Person(
    val id: Int = 0,
    val name: String,
    val birth: Date? = null,
    val sibling: Person? = null
)
