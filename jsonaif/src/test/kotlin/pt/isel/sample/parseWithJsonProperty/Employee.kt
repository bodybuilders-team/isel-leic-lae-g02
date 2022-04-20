package pt.isel.sample.parseWithJsonProperty

import pt.isel.jsonProperty.JsonProperty
import pt.isel.sample.generalTests.Date

/**
 * Used to test parsing with JsonProperty annotations.
 * One non-option parameter.
 * One parameter annotated with @JsonProperty ([birth]).
 */
data class Employee(
    val name: String,
    @JsonProperty("birth_date") val birth: Date? = null,
    val salary: Int
)
