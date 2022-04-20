package pt.isel.sample.parseWithJsonProperty

import pt.isel.jsonProperty.JsonProperty
import pt.isel.sample.generalTests.Date2

/**
 * Used to test parsing with JsonProperty annotations.
 * All optional parameters.
 * All properties are "var".
 */
data class Employee2(
    var name: String = "Bob",
    @JsonProperty("birth_date") var birth: Date2? = null,
    var salary: Int = 1000
)
