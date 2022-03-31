package pt.isel.sample

import pt.isel.JsonProperty

data class Employee(val name: String, @JsonProperty("birth_date") val birth: Date? = null, val salary: Int)
